package com.example.bdget.service;

import com.example.bdget.model.GuiaDespacho;
import com.example.bdget.repository.GuiaDespachoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GuiaDespachoServiceImpl implements GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository repository;

    @Autowired
    private AWSS3Service s3Service;

    @Override
    public GuiaDespacho registrarYDesplegarGuia(GuiaDespacho guia) {
        // 1. Definir la ruta temporal del EFS en la EC2 (mapeada en Docker como /app/efs)
        String rutaEfs = "/app/efs/";
        File carpetaTemporal = new File(rutaEfs);
        if (!carpetaTemporal.exists()) {
            carpetaTemporal.mkdirs();
        }

        String nombreArchivo = "guia_" + guia.getNumeroGuia() + ".pdf";
        File archivoPdf = new File(carpetaTemporal, nombreArchivo);

        // 2. Generar el PDF físico usando OpenPDF
        try (FileOutputStream fos = new FileOutputStream(archivoPdf)) {
            Document documento = new Document();
            PdfWriter.getInstance(documento, fos);
            documento.open();
            documento.add(new Paragraph("GUÍA DE DESPACHO ELECTRÓNICA"));
            documento.add(new Paragraph("Número: " + guia.getNumeroGuia()));
            documento.add(new Paragraph("Transportista: " + guia.getTransportista()));
            documento.add(new Paragraph("Fecha: " + guia.getFecha().toString()));
            documento.add(new Paragraph("Monto Total: $" + guia.getMonto()));
            documento.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF en el EFS: " + e.getMessage());
        }

        // 3. Subir el archivo generado del EFS hacia AWS S3 de forma estructurada
        String fechaString = guia.getFecha().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String rutaFinalS3 = s3Service.subirGuia(archivoPdf, guia.getTransportista(), fechaString);

        // 4. Guardar la información y la ruta de S3 en la base de datos Oracle
        guia.setRutaS3(rutaFinalS3);
        return repository.save(guia);
    }

    @Override
    public List<GuiaDespacho> listarPorFiltros(String transportista, String fecha) {
        LocalDate fechaParsed = LocalDate.parse(fecha);
        return repository.findByTransportistaAndFecha(transportista, fechaParsed);
    }

    @Override
    public GuiaDespacho actualizar(Long id, GuiaDespacho datosNuevos) {
        GuiaDespacho existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        existente.setMonto(datosNuevos.getMonto());
        existente.setTransportista(datosNuevos.getTransportista());
        return repository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
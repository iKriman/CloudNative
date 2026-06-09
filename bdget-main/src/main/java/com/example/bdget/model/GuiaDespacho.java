package com.example.bdget.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "GUIAS_DESPACHO")
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO_GUIA", nullable = false, unique = true)
    private String numeroGuia;

    @Column(name = "TRANSPORTISTA", nullable = false)
    private String transportista;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MONTO")
    private Double monto;

    @Column(name = "RUTA_S3")
    private String rutaS3; // Guarda la ubicación del PDF en AWS S3

    // Constructor vacío
    public GuiaDespacho() {}

    // Constructor
    public GuiaDespacho(String numeroGuia, String transportista, LocalDate fecha, Double monto) {
        this.numeroGuia = numeroGuia;
        this.transportista = transportista;
        this.fecha = fecha;
        this.monto = monto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getRutaS3() { return rutaS3; }
    public void setRutaS3(String rutaS3) { this.rutaS3 = rutaS3; }
}
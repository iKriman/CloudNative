package com.example.bdget.controller;

import com.example.bdget.model.GuiaDespacho;
import com.example.bdget.service.GuiaDespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
public class GuiaController {

    @Autowired
    private GuiaDespachoService service;

    // Crear guías, generar PDF en EFS y subir a S3
    @PostMapping
    public ResponseEntity<GuiaDespacho> crearGuia(@RequestBody GuiaDespacho guia) {
        return ResponseEntity.ok(service.registrarYDesplegarGuia(guia));
    }

    // Consultar guías por transportista y fecha
    @GetMapping("/buscar")
    public ResponseEntity<List<GuiaDespacho>> buscarGuias(
            @RequestParam String transportista,
            @RequestParam String fecha) {
        return ResponseEntity.ok(service.listarPorFiltros(transportista, fecha));
    }

    // Modificar o actualizar guías
    @PutMapping("/{id}")
    public ResponseEntity<GuiaDespacho> actualizar(@PathVariable Long id, @RequestBody GuiaDespacho guia) {
        return ResponseEntity.ok(service.actualizar(id, guia));
    }

    // Eliminar guías específicas
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok("Guía eliminada correctamente");
    }
}
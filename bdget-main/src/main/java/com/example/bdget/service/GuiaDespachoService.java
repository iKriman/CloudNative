package com.example.bdget.service;

import com.example.bdget.model.GuiaDespacho;
import java.time.LocalDate;
import java.util.List;

public interface GuiaDespachoService {
    GuiaDespacho registrarYDesplegarGuia(GuiaDespacho guia);
    List<GuiaDespacho> listarPorFiltros(String transportista, String fecha);
    GuiaDespacho actualizar(Long id, GuiaDespacho guia);
    void eliminar(Long id);
}
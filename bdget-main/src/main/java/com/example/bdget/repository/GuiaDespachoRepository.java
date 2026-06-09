package com.example.bdget.repository;

import com.example.bdget.model.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {

    List<GuiaDespacho> findByTransportistaAndFecha(String transportista, LocalDate fecha);
}
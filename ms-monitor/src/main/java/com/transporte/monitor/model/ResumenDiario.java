package com.transporte.monitor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumen_diario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String vehiculoId;

    @Column(nullable = false)
    private String linea;

    @Column(nullable = false)
    private String paradero;

    @Column(nullable = false)
    private LocalDateTime primeraLlegada;

    @Column(nullable = false)
    private LocalDateTime ultimaLlegada;

    @Column(nullable = false)
    private Integer totalVisitas;

    @Column(nullable = false)
    private Long demoraPromedioMinutos;

    @Column(nullable = false)
    private String estadoMasFrecuente;
}

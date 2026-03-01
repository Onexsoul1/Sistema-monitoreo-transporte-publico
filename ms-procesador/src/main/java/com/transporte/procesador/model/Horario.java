package com.transporte.procesador.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehiculoId;

    @Column(nullable = false)
    private String linea;

    @Column(nullable = false)
    private String paradero;

    @Column(nullable = false)
    private LocalDateTime horaLlegada;

    @Column(nullable = false)
    private LocalDateTime horaSalida;

    @Column
    private Long demoraMinutos;

    @Column(nullable = false)
    private String estado; // EN_HORARIO, DEMORADO, ADELANTADO
}

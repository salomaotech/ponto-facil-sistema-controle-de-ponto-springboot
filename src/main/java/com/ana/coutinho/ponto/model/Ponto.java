package com.ana.coutinho.ponto.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_registro;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionarios funcionarios;

    private LocalDate data;
    private LocalTime horarioEntrada;
    private LocalTime horarioPausa;
    private LocalTime horarioRetorno;
    private LocalTime horarioSaida;

    @Transient
    private double horasTrabalhadas;

    @Transient // Não será mapeado para o banco de dados
    private double horasExtras;

    public Long getId_registro() {
        return id_registro;
    }

    public void setId_registro(Long id_registro) {
        this.id_registro = id_registro;
    }

    public Funcionarios getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(LocalTime horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public LocalTime getHorarioPausa() {
        return horarioPausa;
    }

    public void setHorarioPausa(LocalTime horarioPausa) {
        this.horarioPausa = horarioPausa;
    }

    public LocalTime getHorarioRetorno() {
        return horarioRetorno;
    }

    public void setHorarioRetorno(LocalTime horarioRetorno) {
        this.horarioRetorno = horarioRetorno;
    }

    public LocalTime getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(LocalTime horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(double horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(double horasExtras) {
        this.horasExtras = horasExtras;
    }

}

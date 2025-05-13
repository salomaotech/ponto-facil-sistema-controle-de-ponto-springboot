package com.ana.coutinho.ponto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class Turnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_turno;

    private LocalTime entradaPadrao;
    private LocalTime pausaPadrao;
    private LocalTime retornoPadrao;
    private LocalTime saidaPadrao;

    public Long getId_turno() {
        return id_turno;
    }

    public void setId_turno(Long id_turno) {
        this.id_turno = id_turno;
    }

    public LocalTime getEntradaPadrao() {
        return entradaPadrao;
    }

    public void setEntradaPadrao(LocalTime entradaPadrao) {
        this.entradaPadrao = entradaPadrao;
    }

    public LocalTime getPausaPadrao() {
        return pausaPadrao;
    }

    public void setPausaPadrao(LocalTime pausaPadrao) {
        this.pausaPadrao = pausaPadrao;
    }

    public LocalTime getRetornoPadrao() {
        return retornoPadrao;
    }

    public void setRetornoPadrao(LocalTime retornoPadrao) {
        this.retornoPadrao = retornoPadrao;
    }

    public LocalTime getSaidaPadrao() {
        return saidaPadrao;
    }

    public void setSaidaPadrao(LocalTime saidaPadrao) {
        this.saidaPadrao = saidaPadrao;
    }

}

package com.ana.coutinho.ponto.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Justificativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_justificativa;

    private LocalDate dataFalta;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionarios funcionarios;

    public Long getId_justificativa() {
        return id_justificativa;
    }

    public void setId_justificativa(Long id_justificativa) {
        this.id_justificativa = id_justificativa;
    }

    public LocalDate getDataFalta() {
        return dataFalta;
    }

    public void setDataFalta(LocalDate dataFalta) {
        this.dataFalta = dataFalta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Funcionarios getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

}

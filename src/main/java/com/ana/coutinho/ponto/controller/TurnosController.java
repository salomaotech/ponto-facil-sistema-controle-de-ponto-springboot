package com.ana.coutinho.ponto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ana.coutinho.ponto.model.Turnos;
import com.ana.coutinho.ponto.repository.TurnosRepository;

@Controller
@RequestMapping("/turno")
public class TurnosController {

    @Autowired
    private TurnosRepository repository;

    @PostMapping
    public String save(@ModelAttribute Turnos turnos) {

        // Verifica se já existe um turno registrado
        if (repository.existsBy() && turnos.getId_turno() == null) {

            // Carrega o primeiro turno registrado e redireciona para ele
            Turnos primeiroTurno = repository.findAll().get(0);
            return "redirect:/tela/cadastro_turno/" + primeiroTurno.getId_turno();

        }

        // Caso contrário, salva o novo turno ou atualiza o existente
        repository.save(turnos);
        return "redirect:/tela/cadastro_turno/" + turnos.getId_turno();

    }

}
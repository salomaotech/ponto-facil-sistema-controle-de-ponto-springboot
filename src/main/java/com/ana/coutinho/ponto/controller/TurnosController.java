package com.ana.coutinho.ponto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
            return "redirect:/cadastro_turno/" + primeiroTurno.getId_turno();

        }

        // Caso contrário, salva o novo turno ou atualiza o existente
        repository.save(turnos);
        return "redirect:/cadastro_turno/" + turnos.getId_turno();

    }

    @GetMapping("/cadastro_turno")
    public ModelAndView cadastroTurno() {

        ModelAndView mv = new ModelAndView("cadastro_turno");
        mv.addObject("turno", new Turnos());
        return mv;

    }

    @GetMapping("/cadastro_turno/{id}")
    public ModelAndView abreCadastroTurno(@PathVariable("id") long id) {

        Optional<Turnos> cadastro = repository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_turno");

        if (cadastro.isEmpty()) {

            mv.addObject("turno", new Turnos());

        } else {

            mv.addObject("turno", cadastro.get());

        }

        return mv;

    }

    @GetMapping("/pesquisa_turno")
    public ModelAndView pesquisarTurno() {

        ModelAndView mv = new ModelAndView("pesquisa_turno");
        mv.addObject("turnos", repository.findAll());
        return mv;

    }

}
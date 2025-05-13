package com.ana.coutinho.ponto.controller;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Justificativa;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;
import com.ana.coutinho.ponto.repository.JustificativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/justificativa")
public class JustificativaController {

    @Autowired
    private JustificativaRepository repository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @PostMapping
    public String save(@ModelAttribute Justificativa justificativa, @RequestParam("funcionarios") Long funcionarioId) {

        Funcionarios funcionario = funcionariosRepository.findById(funcionarioId).orElse(null);
        justificativa.setFuncionarios(funcionario);

        repository.save(justificativa);

        return "redirect:/tela/cadastro_justificativa/" + justificativa.getId_justificativa();

    }

}

package com.ana.coutinho.ponto.controller;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Justificativa;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;
import com.ana.coutinho.ponto.repository.JustificativaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

        return "redirect:/justificativa/cadastro_justificativa/" + justificativa.getId_justificativa();

    }

    @GetMapping("/cadastro_justificativa")
    public ModelAndView cadastroJustificativa() {

        ModelAndView mv = new ModelAndView("cadastro_justificativa");
        mv.addObject("justificativa", new Justificativa());
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        return mv;

    }

    @GetMapping("/cadastro_justificativa/{id}")
    public ModelAndView abreCadastroJustificativa(@PathVariable("id") long id) {

        Optional<Justificativa> cadastro = repository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_justificativa");

        // Verificar se a justificativa existe
        if (cadastro.isPresent()) {

            mv.addObject("justificativa", cadastro.get());

        } else {

            mv.addObject("justificativa", new Justificativa());

        }

        // Passar a lista de funcionários para o select
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        return mv;

    }

    @GetMapping("/pesquisa_justificativa")
    public ModelAndView pesquisarJustificativa(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_justificativa");
        List<Justificativa> justificativas = new ArrayList<>();

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {

            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = LocalDate.parse(dataInicioStr);
            }

            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                dataFim = LocalDate.parse(dataFimStr);
            }

            // Se data final for nula, usa a data inicial
            if (dataInicio != null && dataFim == null) {
                dataFim = dataInicio;
            }

        } catch (Exception ignored) {

        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            justificativas = repository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

        }

        mv.addObject("justificativas", justificativas);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

    @GetMapping("/remove/{id}")
    public ModelAndView remover(@PathVariable("id") long id) {

        ModelAndView mv = new ModelAndView("redirect:/justificativa/pesquisa_justificativa");
        repository.deleteById(id);
        return mv;

    }

}

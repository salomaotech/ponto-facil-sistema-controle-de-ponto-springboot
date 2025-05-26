package com.ana.coutinho.ponto.controller;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/funcionario")
public class FuncionariosController {

    @Autowired
    private FuncionariosRepository repository;

    @PostMapping()
    public String save(@ModelAttribute Funcionarios funcionarios) {

        repository.save(funcionarios);
        return "redirect:/cadastro_funcionario/" + funcionarios.getId_funcionario();

    }

    @GetMapping("/cadastro_funcionario")
    public ModelAndView cadastro() {

        ModelAndView mv = new ModelAndView("cadastro_funcionario");
        mv.addObject("funcionario", new Funcionarios());
        return mv;

    }

    @GetMapping("/cadastro_funcionario/{id}")
    public ModelAndView abreCadastro(@PathVariable("id") long id) {

        Optional<Funcionarios> cadastro = repository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_funcionario");

        if (cadastro.isEmpty()) {

            mv.addObject("funcionario", new Funcionarios());

        } else {

            mv.addObject("funcionario", cadastro.get());

        }

        return mv;

    }

    @GetMapping("/pesquisa_funcionario")
    public ModelAndView pesquisar(@RequestParam(value = "cpf", required = false) String cpf) {

        ModelAndView mv = new ModelAndView("pesquisa_funcionario");

        if (cpf != null && !cpf.isEmpty()) {

            // Usando o método com LIKE
            List<Funcionarios> funcionarios = repository.findByCpfContaining(cpf);

            if (!funcionarios.isEmpty()) {

                mv.addObject("resultados", funcionarios);

            } else {

                mv.addObject("resultados", List.of()); // Se não encontrar, retorna lista vazia

            }

        } else {

            mv.addObject("resultados", repository.findAll());

        }

        return mv;

    }

}

package com.ana.coutinho.ponto.controller;

import com.ana.coutinho.ponto.model.Ponto;
import com.ana.coutinho.ponto.repository.PontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ponto")
public class PontoController {

    @Autowired
    private PontoRepository repository;

    @PostMapping
    public String save(@ModelAttribute Ponto ponto) {

        List<Ponto> pontosNaData = repository.verificarPontoHoje(

                ponto.getFuncionarios().getId_funcionario(),
                ponto.getData()

        );

        for (Ponto existente : pontosNaData) {

            // Se o novo ponto for diferente do atual, não permite criar outro, redirecione
            // para o existente
            if (!existente.getId_registro().equals(ponto.getId_registro())) {

                return "redirect:/cadastro_ponto/" + existente.getId_registro();

            }

        }

        repository.save(ponto);
        return "redirect:/cadastro_ponto/" + ponto.getId_registro();

    }

    @GetMapping("/cadastro_ponto")
    public ModelAndView cadastroPonto() {

        ModelAndView mv = new ModelAndView("cadastro_ponto");
        mv.addObject("ponto", new Ponto());
        mv.addObject("listaFuncionarios", repository.findAll());
        return mv;

    }

    @GetMapping("/cadastro_ponto/{id}")
    public ModelAndView abreCadastroPonto(@PathVariable("id") long id) {

        Optional<Ponto> cadastro = repository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_ponto");

        if (cadastro.isPresent()) {

            mv.addObject("ponto", cadastro.get());

        } else {

            mv.addObject("ponto", new Ponto());

        }

        mv.addObject("listaFuncionarios", repository.findAll());
        return mv;

    }

    @GetMapping("/pesquisa_ponto")
    public ModelAndView pesquisarPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_ponto");
        List<Ponto> pontos = new ArrayList<>();

        mv.addObject("listaFuncionarios", repository.findAll());

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = LocalDate.parse(dataInicioStr);
            }
            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                dataFim = LocalDate.parse(dataFimStr);
            }

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {
                dataFim = dataInicio;
            }

        } catch (Exception ignored) {
        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            pontos = repository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

        }

        mv.addObject("pontos", pontos);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

    @GetMapping("/painel_pesquisa_ponto")
    public ModelAndView pesquisarPainelPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("painel_pesquisa_ponto");
        List<Object[]> resultados = new ArrayList<>();

        // Parse das datas
        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        try {

            // Se dataInicio não for informada, pega a data de hoje
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {

                dataInicio = LocalDate.parse(dataInicioStr);

            } else {

                // Atribui a data de hoje
                dataInicio = LocalDate.now();

            }

            if (dataFimStr != null && !dataFimStr.isEmpty()) {

                dataFim = LocalDate.parse(dataFimStr);

            }

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {

                dataFim = dataInicio;

            }

        } catch (Exception ignored) {

        }

        // Buscar funcionários e pontos, usando LEFT JOIN
        resultados = repository.buscarFuncionariosComPontos(dataInicio, dataFim);

        mv.addObject("funcionariosComPonto", resultados);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);
        mv.addObject("dataFim", dataFim);

        return mv;

    }

}

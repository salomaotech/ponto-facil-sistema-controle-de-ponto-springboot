package com.ana.coutinho.ponto.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Ponto;
import com.ana.coutinho.ponto.repository.FuncionariosRepository;
import com.ana.coutinho.ponto.repository.PontoRepository;

@Controller
@RequestMapping("/ponto")
public class PontoController {

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @PostMapping
    public String save(@ModelAttribute Ponto ponto) {

        List<Ponto> pontosNaData = pontoRepository.verificarPontoHoje(ponto.getFuncionarios().getId_funcionario(),
                ponto.getData());

        for (Ponto existente : pontosNaData) {

            /*
             * Se o novo ponto for diferente do atual, não permite criar outro, redirecione
             * para o existente
             */
            if (!existente.getId_registro().equals(ponto.getId_registro())) {

                return "redirect:/ponto/cadastro_ponto/" + existente.getId_registro();

            }

        }

        pontoRepository.save(ponto);
        return "redirect:/ponto/cadastro_ponto/" + ponto.getId_registro();

    }

    @GetMapping("/cadastro_ponto")
    public ModelAndView cadastroPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr) {

        ModelAndView mv = new ModelAndView("cadastro_ponto");
        Ponto ponto = new Ponto();

        // Parse das datas
        LocalDate dataInicio = null;

        try {

            // Se dataInicio não for informada, pega a data de hoje
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {

                dataInicio = LocalDate.parse(dataInicioStr);

            } else {

                // Atribui a data de hoje
                dataInicio = LocalDate.now();

            }

        } catch (Exception ignored) {

        }

        if (idFuncionario != null) {

            Funcionarios funcionario = funcionariosRepository.findById(idFuncionario).orElse(null);
            ponto.setFuncionarios(funcionario);

        }

        mv.addObject("ponto", ponto);
        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        mv.addObject("dataInicio", dataInicio);
        return mv;

    }

    @GetMapping("/cadastro_ponto/{id}")
    public ModelAndView abreCadastroPonto(@PathVariable("id") long id) {

        Optional<Ponto> cadastro = pontoRepository.findById(id);
        ModelAndView mv = new ModelAndView("cadastro_ponto");

        if (cadastro.isPresent()) {

            mv.addObject("ponto", cadastro.get());

        } else {

            mv.addObject("ponto", new Ponto());

        }

        mv.addObject("listaFuncionarios", funcionariosRepository.findAll());
        return mv;

    }

    @GetMapping("/pesquisa_ponto")
    public ModelAndView pesquisarPonto(
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr,
            @RequestParam(value = "dataFim", required = false) String dataFimStr) {

        ModelAndView mv = new ModelAndView("pesquisa_ponto");
        List<Ponto> pontos = new ArrayList<>();

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

            // Se data final for nula, use a mesma da data inicial
            if (dataInicio != null && dataFim == null) {
                dataFim = dataInicio;
            }

        } catch (Exception ignored) {
        }

        // Isto evita consultar todos os registros
        if (idFuncionario != null) {

            pontos = pontoRepository.buscarPorFiltros(idFuncionario, dataInicio, dataFim);

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
            @RequestParam(value = "dataInicio", required = false) String dataInicioStr) {

        ModelAndView mv = new ModelAndView("painel_pesquisa_ponto");
        List<Object[]> resultados = new ArrayList<>();

        // Parse das datas
        LocalDate dataInicio = null;

        try {

            // Se dataInicio não for informada, pega a data de hoje
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {

                dataInicio = LocalDate.parse(dataInicioStr);

            } else {

                // Atribui a data de hoje
                dataInicio = LocalDate.now();

            }

        } catch (Exception ignored) {

        }

        resultados = pontoRepository.buscarFuncionariosComPontos(dataInicio, dataInicio);

        mv.addObject("funcionariosComPonto", resultados);
        mv.addObject("idFuncionario", idFuncionario);
        mv.addObject("dataInicio", dataInicio);

        return mv;

    }

}

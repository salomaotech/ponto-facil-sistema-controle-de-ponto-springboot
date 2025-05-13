package com.ana.coutinho.ponto.controller;

import com.ana.coutinho.ponto.model.Ponto;
import com.ana.coutinho.ponto.repository.PontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

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

                return "redirect:/tela/cadastro_ponto/" + existente.getId_registro();

            }

        }

        repository.save(ponto);
        return "redirect:/tela/cadastro_ponto/" + ponto.getId_registro();

    }

}

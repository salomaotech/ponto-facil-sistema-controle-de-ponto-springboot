package com.ana.coutinho.ponto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class AppController {

    @GetMapping()
    public ModelAndView home(HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {

            return new ModelAndView("redirect:/usuario/pagina_login");

        } else {

            return new ModelAndView("redirect:/painel_pesquisa_ponto");

        }

    }

}

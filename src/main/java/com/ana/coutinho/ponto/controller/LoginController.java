package com.ana.coutinho.ponto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ana.coutinho.ponto.model.Usuario;
import com.ana.coutinho.ponto.repository.UsuarioRepository;
import com.ana.coutinho.ponto.services.CifraSenha;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/login")
    public String logar(@ModelAttribute Usuario usuario, HttpSession session) {

        CifraSenha cifraSenha = new CifraSenha();
        Optional<Usuario> usuarOptional = repository.findByEmailAndPassword(usuario.getEmail(),
                cifraSenha.cifrarSenha(usuario.getPassword()));

        if (!usuarOptional.isEmpty()) {

            session.setAttribute("usuarioLogado", usuarOptional.get());
            session.setAttribute("mensagemLogin", null);
            return "redirect:/home";

        } else {

            session.setAttribute("usuarioLogado", null);
            session.setAttribute("mensagemLogin", "Login incorreto!!!");
            return "redirect:/login";

        }

    }

    @PostMapping("/login/recupera")
    public String recuperar(@ModelAttribute Usuario usuario, HttpSession session) {

        Optional<Usuario> usuarOptional = repository.findByEmailAndKeypass(usuario.getEmail(), usuario.getKeypass());

        if (!usuarOptional.isEmpty()) {

            session.setAttribute("usuarioLogado", usuarOptional.get());
            session.setAttribute("mensagemLogin", null);
            return "redirect:/home";

        } else {

            session.setAttribute("usuarioLogado", null);
            session.setAttribute("mensagemLogin", "Login incorreto!!!");
            return "redirect:/login";

        }

    }

    @GetMapping("/logout")
    public String deslogar(HttpSession session) {

        session.invalidate();
        return "redirect:/login";

    }

}

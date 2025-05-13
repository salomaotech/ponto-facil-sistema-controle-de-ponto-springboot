package com.ana.coutinho.ponto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ana.coutinho.ponto.model.Usuario;
import com.ana.coutinho.ponto.repository.UsuarioRepository;
import com.ana.coutinho.ponto.services.CifraSenha;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ModelAndView cadastrar(@ModelAttribute Usuario usuario, HttpSession session) {

        Optional<Usuario> usuarOptional = repository.findByEmail(usuario.getEmail());
        CifraSenha cifraSenha = new CifraSenha();

        // Verificação inicial de campos obrigatórios
        if (!possuiCamposObrigatoriosPreenchidos(usuario)) {

            session.setAttribute("mensagemUsuarioExiste", "Todos os campos devem ser preenchidos!");
            return new ModelAndView("redirect:/tela/cadastro_usuario");

        }

        // Validação: senhas não conferem
        if (!usuario.getPassword().equals(usuario.getPasswordConfirm())) {

            session.setAttribute("mensagemUsuarioExiste", "As senhas não combinam!");
            return new ModelAndView("redirect:/tela/cadastro_usuario");

        }

        // E-mail já está em uso
        if (usuarOptional.isPresent()) {

            Usuario usuarioExistente = usuarOptional.get();

            // Está atualizando seu próprio cadastro
            if (usuario.getId() != null && usuario.getId().equals(usuarioExistente.getId())) {

                // Isto evita trocar de endereço de e-mail
                usuario.setEmail(usuarioExistente.getEmail());

                // Cifra a senha e atualiza o cadastro
                cifrarSenha(usuario, cifraSenha);
                repository.save(usuario);

                return new ModelAndView("redirect:/logout");

            }

            // Outro usuário já usa este e-mail
            session.setAttribute("mensagemUsuarioExiste", "Já existe um usuário com este e-mail!");
            return new ModelAndView("redirect:/tela/cadastro_usuario");

        }

        // Novo usuário
        cifrarSenha(usuario, cifraSenha);
        repository.save(usuario);
        return new ModelAndView("redirect:/tela/login");

    }

    private void cifrarSenha(Usuario usuario, CifraSenha cifraSenha) {

        String senhaCifrada = cifraSenha.cifrarSenha(usuario.getPassword());
        usuario.setPassword(senhaCifrada);
        usuario.setPasswordConfirm(senhaCifrada);

    }

    public static boolean possuiCamposObrigatoriosPreenchidos(Usuario usuario) {

        return usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty()
                && usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()
                && usuario.getPasswordConfirm() != null && !usuario.getPasswordConfirm().trim().isEmpty()
                && usuario.getKeypass() != null && !usuario.getKeypass().trim().isEmpty();

    }

}

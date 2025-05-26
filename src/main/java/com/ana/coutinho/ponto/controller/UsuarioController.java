package com.ana.coutinho.ponto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ana.coutinho.ponto.model.Usuario;
import com.ana.coutinho.ponto.repository.UsuarioRepository;
import com.ana.coutinho.ponto.services.CifraSenha;
import com.ana.coutinho.ponto.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/login")
    public String logar(@ModelAttribute Usuario usuario, HttpSession session) {

        CifraSenha cifraSenha = new CifraSenha();
        Optional<Usuario> usuarOptional = repository.findByEmailAndPassword(usuario.getEmail(),
                cifraSenha.cifrarSenha(usuario.getPassword()));

        if (!usuarOptional.isEmpty()) {

            session.setAttribute("usuarioLogado", usuarOptional.get());
            session.setAttribute("mensagemErro", null);
            return "redirect:/";

        } else {

            session.setAttribute("usuarioLogado", null);
            session.setAttribute("mensagemErro", "Login incorreto!!!");
            return "redirect:/usuario/pagina_login";

        }

    }

    @PostMapping("/recupera")
    public String recuperar(@ModelAttribute Usuario usuario, HttpSession session) {

        Optional<Usuario> usuarOptional = repository.findByEmailAndKeypass(usuario.getEmail(), usuario.getKeypass());

        if (!usuarOptional.isEmpty()) {

            // Se der certo redireciona para a Home
            session.setAttribute("usuarioLogado", usuarOptional.get());
            session.setAttribute("mensagemErro", null);
            return "redirect:/";

        } else {

            // Se der errado redireciona para a página de recuperação
            session.setAttribute("usuarioLogado", null);
            session.setAttribute("mensagemErro", "Palavra chave incorreta!!!");
            return "redirect:/usuario/recupera_senha_usuario";

        }

    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrar(@ModelAttribute Usuario usuario, HttpSession session) {

        Optional<Usuario> usuarOptional = repository.findByEmail(usuario.getEmail());
        CifraSenha cifraSenha = new CifraSenha();

        // Verificação inicial de campos obrigatórios
        if (!UsuarioService.possuiCamposObrigatoriosPreenchidos(usuario)) {

            session.setAttribute("mensagemErro", "Todos os campos devem ser preenchidos!");
            return new ModelAndView("redirect:/usuario/cadastro_usuario");

        }

        // Validação: senhas não conferem
        if (!usuario.getPassword().equals(usuario.getPasswordConfirm())) {

            session.setAttribute("mensagemErro", "As senhas não combinam!");
            return new ModelAndView("redirect:/usuario/cadastro_usuario");

        }

        // E-mail já está em uso
        if (usuarOptional.isPresent()) {

            Usuario usuarioExistente = usuarOptional.get();

            // Está atualizando seu próprio cadastro
            if (usuario.getId() != null && usuario.getId().equals(usuarioExistente.getId())) {

                // Isto evita trocar de endereço de e-mail
                usuario.setEmail(usuarioExistente.getEmail());

                // Cifra a senha e atualiza o cadastro
                UsuarioService.cifrarSenha(usuario, cifraSenha);
                repository.save(usuario);

                return new ModelAndView("redirect:/usuario/logout");

            }

            // Outro usuário já usa este e-mail
            session.setAttribute("mensagemErro", "Já existe um usuário com este e-mail!");
            return new ModelAndView("redirect:/usuario/cadastro_usuario");

        }

        // Novo usuário
        UsuarioService.cifrarSenha(usuario, cifraSenha);
        repository.save(usuario);
        return new ModelAndView("redirect:/usuario/logout");

    }

    @GetMapping("/pagina_login")
    public ModelAndView paginaLogin(HttpSession session) {

        ModelAndView mv = new ModelAndView("pagina_login");
        mv.addObject("usuario", new Usuario());
        session.setAttribute("mensagemErro", session.getAttribute("mensagemErro"));
        return mv;

    }

    @GetMapping("/logout")
    public String deslogar(HttpSession session) {

        session.invalidate();
        return "redirect:/usuario/pagina_login";

    }

    @GetMapping("/login")
    public ModelAndView login(HttpSession session) {

        ModelAndView mv;

        if (session.getAttribute("usuarioLogado") == null) {

            // Usuário NÃO logado
            mv = new ModelAndView("login");
            session.setAttribute("mensagemErro", session.getAttribute("mensagemErro"));
            mv.addObject("usuario", new Usuario());

        } else {

            // Usuário logado
            mv = new ModelAndView("redirect:/home");

        }

        return mv;

    }

    @GetMapping("/recupera_senha_usuario")
    public ModelAndView recuperarSenha() {

        ModelAndView mv = new ModelAndView("recupera_senha_usuario");
        mv.addObject("usuario", new Usuario());
        return mv;

    }

    @GetMapping("/cadastro_usuario")
    public ModelAndView cadastroUsuario(HttpSession session) {

        ModelAndView mv = new ModelAndView("cadastro_usuario");
        session.setAttribute("mensagemErro", session.getAttribute("mensagemErro"));

        if (session.getAttribute("usuarioLogado") == null) {

            // Novo cadastro
            mv.addObject("usuario", new Usuario());

        } else {

            // Recupera o usuário logado
            mv.addObject("usuario", session.getAttribute("usuarioLogado"));

        }

        return mv;

    }

}

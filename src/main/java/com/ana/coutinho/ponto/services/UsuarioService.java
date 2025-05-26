package com.ana.coutinho.ponto.services;

import com.ana.coutinho.ponto.model.Usuario;

public class UsuarioService {

    public static void cifrarSenha(Usuario usuario, CifraSenha cifraSenha) {

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

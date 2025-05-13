package com.ana.coutinho.ponto.services;

/**
 * Classe responsável por cifrar senhas usando XOR com chave fixa
 * e comparar a senha cifrada do banco com a senha informada.
 * 
 * Sim eu sei a seria melhor um SHA-256 :-)
 */
public class CifraSenha {

    private final String chave = "Chisato Moritaka";

    public String cifrarSenha(String senha) {

        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < senha.length(); i++) {

            char c = (char) (senha.charAt(i) ^ chave.charAt(i % chave.length()));
            resultado.append(c);

        }

        return resultado.toString();

    }

    public boolean compararSenhas(String senhaBanco, String senhaInformada) {

        return senhaBanco.equals(cifrarSenha(senhaInformada));

    }

}

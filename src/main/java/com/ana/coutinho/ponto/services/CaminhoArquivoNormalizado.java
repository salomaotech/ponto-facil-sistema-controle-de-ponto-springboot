package com.ana.coutinho.ponto.services;

import java.io.File;

public class CaminhoArquivoNormalizado {

    public static String obterCaminhoArquivoNormalizado(String path) {

        try {

            if (!path.endsWith(File.separator)) {

                path = new File(path).getPath() + File.separator;

            }

        } catch (Exception ex) {

        }

        return path;

    }

}

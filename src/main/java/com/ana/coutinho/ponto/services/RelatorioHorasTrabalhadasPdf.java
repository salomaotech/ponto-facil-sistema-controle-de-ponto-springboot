package com.ana.coutinho.ponto.services;

import java.math.BigDecimal;
import java.util.List;

import com.ana.coutinho.ponto.model.Funcionarios;
import com.ana.coutinho.ponto.model.Ponto;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

public class RelatorioHorasTrabalhadasPdf {

    private Funcionarios funcionarios;

    public RelatorioHorasTrabalhadasPdf(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    public boolean gerarRelatorioPdf(List<Ponto> pontos, BigDecimal totalHoras, BigDecimal totalHorasExtras,
            String pathDeSaidaDoArquivo) {

        // Criação do PDF
        GerarPdf3 gerarPdf = new GerarPdf3(pathDeSaidaDoArquivo);

        // Adicionando título ao PDF
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

        // Cria o parágrafo e centraliza
        Paragraph titulo = new Paragraph("Relatório de Horas Trabalhadas", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        gerarPdf.addConteudo(titulo);

        // Cria o parágrafo com o nome do funcionário e centraliza
        Paragraph nomeFuncionario = new Paragraph(funcionarios.getNome(), fontTitulo);
        nomeFuncionario.setAlignment(Element.ALIGN_CENTER);
        gerarPdf.addConteudo(nomeFuncionario);

        // Adiciona uma linha em branco
        gerarPdf.addConteudo(new Paragraph("\n"));

        // Adicionando informações gerais
        Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph info = new Paragraph("Total de Horas Trabalhadas: " + String.valueOf(totalHoras)
                + "\nTotal de Horas Extras: " + String.valueOf(totalHorasExtras), fontCorpo);
        gerarPdf.addConteudo(info);
        gerarPdf.addConteudo(new Paragraph("\n"));

        // Adicionando a tabela com os pontos
        String[][] dadosTabela = new String[pontos.size() + 1][6]; // A tabela tem 6 colunas
        dadosTabela[0] = new String[] { "Data", "Entrada", "Saída", "Horas Trabalhadas", "Horas Extras" };

        int i = 1;
        for (Ponto ponto : pontos) {

            dadosTabela[i] = new String[] {

                    Datas.localDateParaStringBr(ponto.getData()),
                    String.valueOf(ponto.getHorarioEntrada()),
                    String.valueOf(ponto.getHorarioSaida()),
                    String.valueOf(ponto.getHorasTrabalhadas()),
                    String.valueOf(ponto.getHorasExtras())

            };

            i++;

        }

        Font fontTabela = FontFactory.getFont(FontFactory.HELVETICA, 10);
        gerarPdf.addTabela(dadosTabela, fontTabela);

        // Gerando o arquivo PDF
        return gerarPdf.gerar();

    }

}

package com.ana.coutinho.ponto.services;

import java.time.Duration;
import java.time.LocalTime;

public class CalculaHoras {

    public static double calcularHorasTrabalhadas(LocalTime entradaPadrao,
            LocalTime pausaPadrao,
            LocalTime retornoPadrao,
            LocalTime saidaPadrao) {

        double totalHoras = 0.0;

        // Calcula horas ANTES da pausa (entrada → pausa)
        if (entradaPadrao != null && pausaPadrao != null) {
            long minutosAntesPausa = Duration.between(entradaPadrao, pausaPadrao).toMinutes();
            totalHoras += minutosAntesPausa / 60.0;
        }

        // Calcula horas DEPOIS da pausa (retorno → saída)
        if (retornoPadrao != null && saidaPadrao != null) {
            long minutosDepoisPausa = Duration.between(retornoPadrao, saidaPadrao).toMinutes();
            totalHoras += minutosDepoisPausa / 60.0;
        }

        return totalHoras;
    }

    public static double calcularHorasTrabalhadasFuncionario(LocalTime entrada,
            LocalTime pausa,
            LocalTime retorno,
            LocalTime saida) {

        // --- Caso 1: Funcionário não fez pausa (pausa e retorno são null) ---
        if (pausa == null && retorno == null) {

            if (entrada != null && saida != null && entrada.isBefore(saida)) {

                long minutosTrabalhados = Duration.between(entrada, saida).toMinutes();
                return minutosTrabalhados / 60.0;

            }

            return 0.0;

        }

        // --- Caso 2: Funcionário fez pausa (cálculo normal) ---
        double horasTrabalhadas = 0.0;

        // Horas antes da pausa (entrada → pausa)
        if (entrada != null && pausa != null && entrada.isBefore(pausa)) {
            long minutosAntesPausa = Duration.between(entrada, pausa).toMinutes();
            horasTrabalhadas += minutosAntesPausa / 60.0;
        }

        // Horas depois da pausa (retorno → saída)
        if (retorno != null && saida != null && retorno.isBefore(saida)) {
            long minutosDepoisPausa = Duration.between(retorno, saida).toMinutes();
            horasTrabalhadas += minutosDepoisPausa / 60.0;
        }

        return horasTrabalhadas;

    }

}

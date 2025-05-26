package com.ana.coutinho.ponto.services;

import com.toedter.calendar.JCalendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Datas {

    /**
     * Converte um Calendar para uma String no formato dd/MM/yyyy
     *
     * @param calendar Parâmetro
     * @return String no formato dd/MM/yyyy
     */
    public static String calendarParaStringBr(Calendar calendar) {

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(calendar.getTime());

        } catch (Exception ex) {

            return null;

        }

    }

    /**
     * Converte um LocalDate para uma String no formato dd/MM/yyyy
     *
     * @param localDate
     * @return
     */
    public static String localDateParaStringBr(LocalDate localDate) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);

        } catch (Exception ex) {

            return null;

        }

    }

    /**
     * Adiciona meses a um Calendar
     *
     * @param calendar Parâmetro
     * @param numeroMesesAdicionar Número de meses a serem adicionados
     * @return Novo Calendar atualizado
     */
    public static Calendar adicionarMesCalendar(Calendar calendar, int numeroMesesAdicionar) {

        try {

            Calendar novoCalendar = (Calendar) calendar.clone();
            novoCalendar.add(Calendar.MONTH, numeroMesesAdicionar);
            return novoCalendar;

        } catch (Exception ex) {

            return null;

        }

    }

    /**
     * Retorna se um Calendar é válida entre 01/01/0001 até 31/12/9998
     *
     * @param calendar Calendar
     * @return true se a data for válida
     */
    public static boolean isCalendarioValido(Calendar calendar) {

        try {

            boolean isAnoValido = calendar.get(Calendar.YEAR) >= 1 && calendar.get(Calendar.YEAR) <= 9998;
            boolean isMesValido = calendar.get(Calendar.MONTH) >= 0 && calendar.get(Calendar.MONTH) <= 11;
            boolean isDiaValido = calendar.get(Calendar.DAY_OF_MONTH) >= 1 && calendar.get(Calendar.DAY_OF_MONTH) <= 31;
            return isAnoValido && isMesValido && isDiaValido;

        } catch (Exception ex) {

            return false;

        }

    }

    /**
     * Retorna se o objeto é um tipo que armazena data (JCalendar, Calendar,
     * Date etc)
     *
     * @param object Objeto
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isObjetoData(Object object) {

        List tiposValidos = new ArrayList();
        tiposValidos.add("com.toedter.calendar.JCalendar");
        tiposValidos.add("java.util.GregorianCalendar");
        tiposValidos.add("java.util.Date");
        return tiposValidos.contains(object.getClass().getName());

    }

    /**
     * Converte um Calendar para uma String no formato yyyy-MM-dd
     *
     * @param valor Parâmetro
     * @return String no formato yyyy-MM-dd
     */
    public static String calendarParaStringAnoMesDia(Object valor) {

        Date data = null;

        switch (valor.getClass().getName()) {

            case "com.toedter.calendar.JCalendar":
                JCalendar jCalendar = (JCalendar) valor;
                data = jCalendar.getCalendar().getTime();
                break;

            case "java.util.GregorianCalendar":
                Calendar calendar = (Calendar) valor;
                data = calendar.getTime();
                break;

            case "java.util.Date":
                data = (Date) valor;
                break;

        }

        try {

            return new SimpleDateFormat("yyyy-MM-dd").format(data);

        } catch (Exception ex) {

            return null;

        }

    }

    /**
     * Converte um LocalTime para uma String no formato HH:mm
     *
     * @param localTime
     * @return
     */
    public static String localTimeParaStringBr(LocalTime localTime) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return localTime.format(formatter);

        } catch (Exception ex) {

            return null;

        }

    }

}

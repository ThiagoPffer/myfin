package com.thpiffer.myfin.app.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class CompetenceUtils {

    public static void validateCompetenceString(String competence) {
        if (competence == null || competence.isEmpty()) {
            throw new IllegalArgumentException("A competência não pode estar vazia");
        }

        if (competence.length() != 7) {
            throw new IllegalArgumentException("Competência informada em formato inválido");
        }

        try {
            LocalDate.parse(competence + "-01");
        } catch (Exception e) {
            throw new IllegalArgumentException("Competência informada em formato inválido");
        }
    }

    public static LocalDate getStartDateFromCompetenceString(String competence) {
        validateCompetenceString(competence);
        return LocalDate.parse(competence + "-01");
    }

    public static LocalDate getEndDateFromCompetenceString(String competence) {
        validateCompetenceString(competence);
        LocalDate firstDayOfTheMonth = LocalDate.parse(competence + "-01");
        return firstDayOfTheMonth.with(TemporalAdjusters.lastDayOfMonth());
    }

}

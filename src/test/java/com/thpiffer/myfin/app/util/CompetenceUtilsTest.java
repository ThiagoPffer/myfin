package com.thpiffer.myfin.app.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompetenceUtilsTest {

    @Test
    void validateCompetenceString_receivesValidCompetenceString_doesNotThrowException() {
        String competence = "2024-04";
        assertDoesNotThrow(() -> CompetenceUtils.validateCompetenceString(competence));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validateCompetenceString_receivesNullAndEmptyCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.validateCompetenceString(competence));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid", "2024", "2024-", "2024-1", "1-2024", "2024-1-1", "2024-01-01", "01-01-2024", "1-1-2024"
    })
    void validateCompetenceString_receivesInvalidCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.validateCompetenceString(competence));
    }

    @Test
    void getStartDateFromCompetenceString_receivesValidCompetenceString_returnsStartLocalDate() {
        String competence = "2024-04";
        LocalDate result = assertDoesNotThrow(() ->
                CompetenceUtils.getStartDateFromCompetenceString(competence));
        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 4, 1), result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void getStartDateFromCompetenceString_receivesNullAndEmptyCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.getStartDateFromCompetenceString(competence));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid", "2024", "2024-", "2024-1", "1-2024", "2024-1-1", "2024-01-01", "01-01-2024", "1-1-2024"
    })
    void getStartDateFromCompetenceString_receivesInvalidCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.getStartDateFromCompetenceString(competence));
    }

    @Test
    void getEndDateFromCompetenceString_receivesValidCompetenceString_returnsEndLocalDate() {
        String competence = "2024-02";
        LocalDate result = assertDoesNotThrow(() ->
                CompetenceUtils.getEndDateFromCompetenceString(competence));
        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 2, 29), result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void getEndDateFromCompetenceString_receivesNullAndEmptyCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.getEndDateFromCompetenceString(competence));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid", "2024", "2024-", "2024-1", "1-2024", "2024-1-1", "2024-01-01", "01-01-2024", "1-1-2024"
    })
    void getEndDateFromCompetenceString_receivesInvalidCompetenceString_throwsException(String competence) {
        assertThrows(IllegalArgumentException.class,
                () -> CompetenceUtils.getEndDateFromCompetenceString(competence));
    }

}
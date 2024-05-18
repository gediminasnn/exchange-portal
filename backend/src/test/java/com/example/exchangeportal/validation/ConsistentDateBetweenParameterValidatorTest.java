package com.example.exchangeportal.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsistentDateBetweenParameterValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConsistentDateBetweenParameters constraintAnnotation;

    private ConsistentDateBetweenParameterValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ConsistentDateBetweenParameterValidator();
    }

    @Test
    void testIsValid_ValidDateRange() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {LocalDate.now().minusDays(5), LocalDate.now()};
        assertTrue(validator.isValid(dates, context));
    }

    @Test
    void testIsValid_InvalidDateRangeFromDateAfterToDate() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {LocalDate.now().plusDays(5), LocalDate.now()};
        assertFalse(validator.isValid(dates, context));
    }

    @Test
    void testIsValid_InvalidDateRangeFromDateInFuture() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)};
        assertFalse(validator.isValid(dates, context));
    }

    @Test
    void testIsValid_NullDates() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {null, null};
        assertTrue(validator.isValid(dates, context));
    }

    @Test
    void testIsValid_OneNullDate() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {LocalDate.now(), null};
        assertThrows(IllegalArgumentException.class, () -> validator.isValid(dates, context));
    }

    @Test
    void testIsValid_InvalidDateType() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(2);
        validator.initialize(constraintAnnotation);

        Object[] dates = {"not a date", LocalDate.now()};
        assertThrows(IllegalArgumentException.class, () -> validator.isValid(dates, context));
    }

    @Test
    void testIsValid_DatePositionOutOfBounds() {
        when(constraintAnnotation.fromDatePosition()).thenReturn(1);
        when(constraintAnnotation.toDatePosition()).thenReturn(3);
        validator.initialize(constraintAnnotation);

        Object[] dates = {LocalDate.now(), LocalDate.now().minusDays(5)};
        assertThrows(IllegalArgumentException.class, () -> validator.isValid(dates, context));
    }
}

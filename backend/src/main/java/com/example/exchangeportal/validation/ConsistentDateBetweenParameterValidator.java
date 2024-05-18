package com.example.exchangeportal.validation;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ConsistentDateBetweenParameterValidator
        implements ConstraintValidator<ConsistentDateBetweenParameters, Object[]> {

    private int fromDatePosition;
    private int toDatePosition;

    @Override
    public void initialize(ConsistentDateBetweenParameters constraintAnnotation) {
        fromDatePosition = constraintAnnotation.fromDatePosition();
        toDatePosition = constraintAnnotation.toDatePosition();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        int fromDateIndex = fromDatePosition - 1;
        int toDateIndex = toDatePosition - 1;

        if (value.length <= fromDateIndex || value.length <= toDateIndex) {
            throw new IllegalArgumentException("Uncorrect dates positions in annotation.");
        }

        Object fromDateObj = value[fromDateIndex];
        Object toDateObj = value[toDateIndex];

        if (fromDateObj == null && toDateObj == null) {
            value[fromDateIndex] = LocalDate.now().minusMonths(1);
            value[toDateIndex] = LocalDate.now();
            return true;
        }

        if (fromDateObj != null && toDateObj == null || fromDateObj == null && toDateObj != null) {
            throw new IllegalArgumentException("Expected both dates to be either null or LocalDate value.");
        }

        if (!(fromDateObj instanceof LocalDate) || !(toDateObj instanceof LocalDate)) {
            throw new IllegalArgumentException("Expected both dates of type LocalDate.");
        }

        LocalDate fromDate = (LocalDate) fromDateObj;
        LocalDate toDate = (LocalDate) toDateObj;

        return toDate.isBefore(LocalDate.now().plusDays(1)) && fromDate.isBefore(toDate);
    }
}

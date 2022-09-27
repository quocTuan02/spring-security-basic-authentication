package com.tuannq.authentication.annotation;


import com.tuannq.authentication.util.ConverterUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDate, Object> {
    private String startDate;
    private String endDate;
    private String message;

    @Override
    public void initialize(final StartBeforeEndDate constraintAnnotation) {
        startDate = constraintAnnotation.startDate();
        endDate = constraintAnnotation.endDate();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid;
        try {
            final String startDateStr = BeanUtils.getProperty(value, startDate);
            final String endDateStr = BeanUtils.getProperty(value, endDate);

            var date1 = ConverterUtils.formatStringToDate(startDateStr);
            var date2 = ConverterUtils.formatStringToDate(endDateStr);
            if (date1 != null && date2 != null)
                valid = !date1.after(date2);
            else {
                var instant1 = ConverterUtils.formatStringToInstant(startDateStr);
                var instant2 = ConverterUtils.formatStringToInstant(endDateStr);
                if (instant1 != null && instant2 != null)
                    valid = !instant1.isAfter(instant2);
                else return true;
            }
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(startDate)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}

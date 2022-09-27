package com.tuannq.authentication.annotation;


import com.tuannq.authentication.util.ConverterUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class PastOrPresentValidator implements ConstraintValidator<PastOrPresent, String> {

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext constraintValidatorContext) {
        if (dateStr == null || dateStr.isEmpty()) return true;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date today = calendar.getTime();

            Date date = ConverterUtils.formatStringToDate(dateStr);
            return !date.after(today);
        } catch (Exception e) {
            return false;
        }

    }
}

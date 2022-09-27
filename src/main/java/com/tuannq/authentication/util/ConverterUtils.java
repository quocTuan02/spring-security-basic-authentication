package com.tuannq.authentication.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.tuannq.authentication.model.type.RegexType.*;

@Component
public class ConverterUtils {
    public static String formatDateToString(Date date) {
        if (date == null) return null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static String formatDateToDatetimeString(Date date) {
        if (date == null) return null;

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formatDateToDatetimeString(Instant date) {
        if (date == null) return null;
        var DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return DATE_TIME_FORMATTER.format(date);
    }

    /**
     * format date string to date
     *
     * @param str example: 20/2/2021, 20-2-2021
     */
    public static Date formatStringToDate(String str) {
        if (str == null) return null;

        if (!DATE_PATTERN.matcher(str).matches())
            return null;

        var arr = Arrays.stream(str.split("[/-]"))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        if (arr.length != 3) return null;

        var day = arr[0];
        var month = arr[1];
        var year = arr[2];

        var calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Instant formatStringToInstant(String str) {
        try {
            var f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ROOT);
            var ldt = LocalDateTime.parse(str, f);
            var zdt = ldt.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            return zdt.toInstant();
        } catch (Exception e) {
            var date = formatStringToDate(str);
            if (date == null)
                return null;
            return Instant.ofEpochMilli(date.getTime());
        }

    }


    public static String convertInstantToString(Instant instant) {
        var DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                .withZone(ZoneId.systemDefault());

        return DATE_TIME_FORMATTER.format(instant);
    }

    public static String generateRandomPassword() {
        final String s1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String s2 = "abcdefghijklmnopqrstuvwxyz";
        final String s3 = "0123456789";
        final String s4 = "!@#&()–{}:;,?/*~$^+=<>.";
        final String chars = s1 + s2 + s3 + s4;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        sb.append(s1.charAt(random.nextInt(s1.length())));
        sb.append(s2.charAt(random.nextInt(s2.length())));
        sb.append(s3.charAt(random.nextInt(s3.length())));
        sb.append(s4.charAt(random.nextInt(s4.length())));

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static Integer convertStringToNumber(String number) {
        try {
            if (Objects.equals(number, "") || number == null) return null;
            return Integer.parseInt(number);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Long convertStringToLong(String number) {
        try {
            if (Objects.equals(number, "") || number == null) return null;
            return Long.parseLong(number);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Integer convertStringToInt(String number) {
        try {
            if (Objects.equals(number, "") || number == null) return null;
            return Integer.parseInt(number);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static List<Long> convertStringToLong(List<String> number) {
        var rs = new ArrayList<Long>();
        for (var n : number) {
            try {
                rs.add(Long.parseLong(n));
            } catch (Exception ignored) {
                return null;
            }
        }
        return rs;
    }

    public static String formatNumberToString(int number) {
        String pattern = "###,###,###";
        var decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public static String formatNumberToString(long number) {
        String pattern = "###,###,###,###.##";
        var decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public static String formatDecimalToString(double number) {
        String pattern = "###,###,###,###.#";
        var decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public static String formatDecimalToString(double number, int digitLength) {
        var a = String.valueOf(number).split("\\.");
        if (digitLength == 0)
            return formatNumberToString((long) number);

        StringBuilder i = new StringBuilder(".");
        if (a.length == 1)
            i.append("0".repeat(digitLength));
        else {
            if (a[1].length() >= digitLength)
                i.append(a[1], 0, digitLength);
            else
                i.append(a[1]).append("0".repeat(digitLength - a[1].length()));
        }
        return formatNumberToString((long) number).concat(i.toString());
    }

    public static String roundToString(double number, int digitLength) {
        if (digitLength < 0) return null;
        int temp = Integer.parseInt("1".concat("0".repeat(digitLength)));
        double rs = Math.round(number * temp) * 1.0 / temp;
        return formatDecimalToString(rs, digitLength);
    }

    public static String ceilToString(double number, int digitLength) {
        if (digitLength < 0) return null;
        int temp = Integer.parseInt("1".concat("0".repeat(digitLength)));
        double rs = Math.ceil(number * temp) / temp;
        return formatDecimalToString(rs, digitLength);
    }

    public static String floorToString(double number, int digitLength) {
        if (digitLength < 0) return null;
        int temp = Integer.parseInt("1".concat("0".repeat(digitLength)));
        double rs = Math.floor(number * temp) / temp;
        return formatDecimalToString(rs, digitLength);
    }

    public static String toSlug(String input) {
        var nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        var normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        var slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public String formatPrice(double number) {
        String pattern = "###,###,###,###.#";
        var decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public String formatPrice(long number) {
        String pattern = "###,###,###,###.#";
        var decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }
}

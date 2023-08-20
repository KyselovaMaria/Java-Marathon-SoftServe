package jom.com.softserve.s6.task2;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

class MyUtils {
    public static String getDateAfterToday(int years, int months, int days) {
        return LocalDate.now().plusYears(years).plusMonths(months).plusDays(days).toString();
    }
}


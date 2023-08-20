package jom.com.softserve.s6.task1;

import java.time.LocalDate;

public class MyUtils {

    public static boolean isLeapYear(int year) {
        LocalDate dt = LocalDate.parse(year + "-01-01");
        return dt.isLeapYear();
    }

}

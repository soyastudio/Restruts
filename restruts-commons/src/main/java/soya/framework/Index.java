package soya.framework;

import soya.framework.util.DateTimeUtils;

public class Index {
    public static void main(String[] args) {
        System.out.println(DateTimeUtils.calendarBuilder().setDate(2023, 0, 25).build().getTime());
    }
}

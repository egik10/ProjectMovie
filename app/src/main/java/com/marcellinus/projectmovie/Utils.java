package com.marcellinus.projectmovie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final String INDONESIA_ISO_3166_1_COUNTRY_CODE = "ID";

    public static final int STATUS_SUCCESS = 1, STATUS_ERROR = 2;
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd", DATE_FORMAT_2 = "dd MMMM yyyy";

    public static int getRatingColor(double rating) {
        int color;

        if (rating == 0) color = R.color.colorTMDbNotRated;
        else if (rating < 5.0) color = R.color.colorTMDbNegative;
        else if (rating < 7.0) color = R.color.colorTMDbMixed;
        else color = R.color.colorTMDbPositive;

        return color;
    }

    public static Date toDate(String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_1, Locale.US).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static Date toDate2(String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_2, Locale.US).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String toStringFormat1(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_1, Locale.US).format(date);
    }

    public static String toStringFormat2(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_2, Locale.US).format(date);
    }
}

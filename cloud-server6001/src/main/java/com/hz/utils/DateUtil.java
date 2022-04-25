package com.hz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
    public final static SimpleDateFormat SDF_YEAR = new SimpleDateFormat("yyyy");

    public final static SimpleDateFormat SDF_YEAR_MONTH = new SimpleDateFormat("yyyyMM");

    public final static SimpleDateFormat SDY_DAY = new SimpleDateFormat("dd");

    public final static SimpleDateFormat SDY_YEAR_MONTH_DAY_HOUR = new SimpleDateFormat("yyyyMMddHH");


    /**
     * 获取DD格式
     *
     * @return
     */
    public static String getSimpleDay() {
        return SDY_DAY.format(new Date());
    }

    /**
     * 获取YYYYMM格式
     *
     * @return
     */
    public static String getDayMonth() {
        return SDF_YEAR_MONTH.format(new Date());
    }


    public static String getSimpleHour() {
        return SDY_YEAR_MONTH_DAY_HOUR.format(new Date());
    }


}
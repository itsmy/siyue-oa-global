package com.oa.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDate(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    public static int dateDiffer(Date recordDate, Date dateNow) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            long dateNowNum = Long.parseLong(sf.format(dateNow));
            long recordNum = Long.parseLong(sf.format(recordDate));
            return (int) (dateNowNum - recordNum);
        } catch (Exception e) {
            e.getMessage();
            return -1;
        }
    }
}

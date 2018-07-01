package com.acacia.iotken.acacia.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

    /**
     * UNIX時間から,yyyyMMdd形式の日付を返します.
     *
     * @param unixTime UNIX時間（ミリ秒）
     * @return yyyyMMdd形式の文字列
     */
    public static String toDateString(long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
}

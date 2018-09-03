package com.sk.cnaps.domain.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import com.ibm.icu.util.ChineseCalendar;

public class LunarCalendar {
    public static LocalDate convertLunarToSolar(int year, int month, int dayOfMonth) {
        ChineseCalendar cc = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();
         
        cc.set(ChineseCalendar.EXTENDED_YEAR, year + 2637);
        cc.set(ChineseCalendar.MONTH, month - 1);
        cc.set(ChineseCalendar.DAY_OF_MONTH, dayOfMonth);
         
        cal.setTimeInMillis(cc.getTimeInMillis());
        
        Date date = cal.getTime();
        
        Instant instant = date.toInstant();
        System.out.println("instant : " + instant); //Zone : UTC+0

        //2. Instant + system default time zone + toLocalDate() = LocalDate
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return instant.atZone(defaultZoneId).toLocalDate();   
    }
}

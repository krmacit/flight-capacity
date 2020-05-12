package com.cargis.flightcapacity.util;

import lombok.experimental.UtilityClass;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UtilityClass
public class DateUtils {

    private final ZoneId ZONE_TR = ZoneId.of("Turkey");
    private final ZoneId ZONE_UTC = ZoneId.of("UTC");

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getTomorrowDate() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date tomorrow = Date.from(LocalDate.now().plusDays(1).atStartOfDay().atZone(ZONE_TR).toInstant());
        return tomorrow;
    }

    public static Date epochToDate(Integer epoch){
        Date date = null;
        if (epoch != null) {
            date = new Date(Long.parseLong(epoch.toString())*1000);
        }
        return date;
    }

}

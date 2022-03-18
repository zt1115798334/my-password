package com.zt.mypassword.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/7/16 9:48
 * description:时间工具类
 */
public class DateUtils {


    public static final String DATE_FORMAT_YEAR = "yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_1 = "MM/dd";
    public static final String DATE_FORMAT_2 = "MMdd";
    public static final String DATE_FORMAT_3 = "yyyyMMdd";
    public static final String DATE_FORMAT_4 = "MM月dd日";
    public static final String DATE_FORMAT_5 = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_6 = "yyyy.MM.dd";


    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_1 = "MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_2 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_3 = "yyyy-MM-dd HH";
    public static final String DATE_TIME_FORMAT_4 = "yyyyMMddHHmmssSSS";
    public static final String DATE_TIME_FORMAT_5 = "yyyyMMddHH";
    public static final String DATE_TIME_FORMAT_CHN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATE_TIME_FORMAT_CHN_2 = "yyyy年MM月dd日 HH:mm";

    public static final String GREENWICH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String GREENWICH_DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_TIME_CS = "yyyy年MM月dd日 HH:mm:ss";

    public static final String TIME_FORMAT_3 = "HH:mm";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_FORMAT_2 = "HH";

    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 查询两个时间内的list
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return list
     */
    public static List<LocalDate> dateRangeList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> result = Lists.newArrayList();
        while (startDate.isBefore(endDate)) {
            result.add(startDate);
            startDate = startDate.plusDays(1);
        }
        result.add(endDate);
        return result;
    }

    /**
     * 查询两个时间内的list
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return list
     */
    public static List<LocalDateTime> dateTimeRangeList(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        startDateTime = startDateTime.withMinute(0).withSecond(0);
        endDateTime = endDateTime.withMinute(0).withSecond(0);
        List<LocalDateTime> result = Lists.newArrayList();
        while (startDateTime.isBefore(endDateTime)) {
            result.add(startDateTime);
            startDateTime = startDateTime.plusHours(1);
        }
        result.add(endDateTime);
        return result;
    }


    @Setter
    @Getter
    @ToString
    public static class DateRange {
        private String startDateTimeStr;
        private String endDateTimeStr;

        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;

        private LocalDate startDate;
        private LocalDate endDate;

        static DateRange getIns(String startDateTimeStr,
                                String endDateTimeStr) {
            DateRange dr = new DateRange();
            if (StringUtils.isNotEmpty(startDateTimeStr) && StringUtils.isNotEmpty(endDateTimeStr)) {

                LocalDateTime startDateTime = LocalDateTimeUtil.parse(startDateTimeStr, DATE_TIME_FORMAT);
                LocalDateTime endDateTime = LocalDateTimeUtil.parse(endDateTimeStr, DATE_TIME_FORMAT);
                dr.startDateTimeStr = startDateTimeStr;
                dr.endDateTimeStr = endDateTimeStr;
                dr.startDateTime = startDateTime;
                dr.endDateTime = endDateTime;
                dr.startDate = startDateTime.toLocalDate();
                dr.endDate = endDateTime.toLocalDate();
            }
            return dr;
        }

        static DateRange getIns(LocalDate startDate,
                                LocalDate endDate) {
            DateRange dr = new DateRange();
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = startDate.atTime(LocalTime.of(0, 0, 0));
                LocalDateTime endDateTime = endDate.atTime(LocalTime.of(23, 59, 59));
                dr.startDateTimeStr = LocalDateTimeUtil.format(startDateTime, DateUtils.DATE_TIME_FORMAT);
                dr.endDateTimeStr = LocalDateTimeUtil.format(endDateTime, DateUtils.DATE_TIME_FORMAT);
                dr.startDate = startDate;
                dr.endDate = endDate;
                dr.startDateTime = startDateTime;
                dr.endDateTime = endDateTime;
            }
            return dr;
        }

        static DateRange getIns(LocalDateTime startDateTime,
                                LocalDateTime endDateTime) {
            DateRange dr = new DateRange();
            if (startDateTime != null && endDateTime != null) {
                dr.startDateTimeStr = LocalDateTimeUtil.format(startDateTime, DateUtils.DATE_TIME_FORMAT);
                ;
                dr.endDateTimeStr = LocalDateTimeUtil.format(endDateTime, DateUtils.DATE_TIME_FORMAT);
                ;
                dr.startDate = startDateTime.toLocalDate();
                dr.endDate = endDateTime.toLocalDate();
                dr.startDateTime = startDateTime;
                dr.endDateTime = endDateTime;
            }
            return dr;
        }
    }

    public static String printHourMinuteSecond(Long second) {
        long h = second / 3600;
        long m = second % 3600 / 60;
        long s = second % 60; //不足60的就是秒，够60就是分
        return h + "小时" + m + "分钟" + s + "秒";
    }

    public static void main(String[] args) {

    }
}

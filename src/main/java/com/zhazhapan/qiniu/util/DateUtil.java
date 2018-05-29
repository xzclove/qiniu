package com.zhazhapan.qiniu.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2016/9/1.
 */
public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private final static Long twoHour = 7200000L;
    private final static Long eightHour = 28800000L;
    private final static Long oneDay = 86400000L;
    private final static Long twoDay = 172800000L;
    private final static Long sevenDay = 604800000L;
    private final static Long month = 2592000000L;

    private final static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
    private final static SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static final SimpleDateFormat dft2 = new SimpleDateFormat("yyyy/MM/dd/");

    /**
     * 返回当前时间 yyyy年MM月dd日 HH时mm分ss秒
     *
     * @return
     */
    public static String currentDateYMD() {
        Date currentTime = new Date();
        String dateString = dft2.format(currentTime);
        return dateString;
    }

    /**
     * 返回当前时间戳 1443588357153
     *
     * @return
     */
    public static String currentTime() {
        String dateString = String.valueOf(System.currentTimeMillis());
        return dateString;
    }

    /**
     * 返回当前 小时数
     */
    public static String currentHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 返回当前 分钟数
     */
    public static String currentMinute() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 日期 转换成 字符串
     */
    public static String dateToString(Date date) {
        String dateString = formatter.format(date);
        return dateString;
    }

    public static void main(String[] args) {

        System.out.println(currentDateHMS());
        System.out.println(getDateYMD2(0));


        Timestamp createTime = new Timestamp(System.currentTimeMillis());


        Timestamp newTime = new Timestamp(createTime.getTime() + eightHour);

        System.out.println(newTime);

        System.out.println("-------------------------");

        System.out.println(new Timestamp(System.currentTimeMillis()));

        System.out.println(getTimestamp("2016-11-28", 0));


        System.out.println(getFewDaysAgoTimestamp(0));
        System.out.println(getFewDaysAgoTimestamp(7));
        System.out.println(getFewDaysAgoTimestamp(30));

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsStr = "";
        try {
            //方法一
            tsStr = df2.format(ts);
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到时间戳
     * ：原因是有些场景需要精确到每天起始的时间
     *
     * @param date 2016-09-22
     * @param flag 0  1
     * @return
     */
    public static Timestamp getTimestamp(String date, Integer flag) {
        Timestamp timestamp = null;
        if (StringUtils.isNotEmpty(date)) {
            try {
                if (flag == 0) {
                    //凌晨时间戳
                    timestamp = new Timestamp(sdf.parse(date + " 00:00:00").getTime());
                } else {
                    //当天最晚时间戳
                    timestamp = new Timestamp(sdf.parse(date + " 23:59:59").getTime());
                }
            } catch (ParseException e) {
                System.out.println(e);
            }
        }
        return timestamp;
    }

    /**
     * 得到时间戳
     *
     * @param date 格式为 2017/04/01 00:00
     * @return
     */
    public static Timestamp getTimestamp2(String date) {
        Timestamp timestamp = null;
        if (StringUtils.isNotEmpty(date)) {
            try {
                timestamp = new Timestamp(formatter.parse(date).getTime());
            } catch (ParseException e) {
                System.out.println(e);
            }
        }
        return timestamp;
    }

    /**
     * 得到当前时间戳
     */
    public static long getNow() {
        return System.currentTimeMillis();
    }

    /**
     * 得到1天前时间戳
     */
    public static long getOneDayAgo() {
        return System.currentTimeMillis() - oneDay;
    }

    /**
     * 得到1小时前时间戳
     */
    public static long getTwoHourAgo() {
        return System.currentTimeMillis() - twoHour;
    }

    /**
     * 得到2天前时间戳
     */
    public static long getTwoDayAgo() {
        return System.currentTimeMillis() - twoDay;
    }

    /**
     * 得到7天前时间戳
     */
    public static long getSevenDayAgo() {
        return System.currentTimeMillis() - sevenDay;
    }

    /**
     * 得到30天前时间戳
     */
    public static long getMonthAgo() {
        return System.currentTimeMillis() - month;
    }


    public static String timestampToDate(Timestamp ts) {
        if (null == ts) {
            return null;
        }
        String tsStr = "";
        try {
            tsStr = df.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public static Timestamp getTimestamp(String date) {
        Timestamp timestamp = null;
        if (StringUtils.isNotEmpty(date) && date != "") {
            try {
                timestamp = new Timestamp(sdf.parse(date + " 12:00:00").getTime());
            } catch (ParseException e) {
                System.out.println(e);
            }
        }
        // 2016-09-22  12:00:00
        return timestamp;
    }

    /**
     * 时间戳判断,大于某个时间戳
     *
     * @param startDate，起始时间
     * @param queryDate，查询时间
     * @return
     */
    public static boolean isBigger(Timestamp startDate, Timestamp queryDate) {
        if (startDate == null || queryDate == null) {
            return false;
        }
        // 如果查询时间大于 起始时间 则满足条件
        if (startDate.compareTo(queryDate) < 0) {
            return true;
        }
        return false;
    }

    /**
     * 时间戳判断,小于某个时间戳
     *
     * @param endDate，结束时间
     * @param queryDate，查询时间
     * @return
     */
    public static boolean isSmall(Timestamp endDate, Timestamp queryDate) {
        if (endDate == null || queryDate == null) {
            return false;
        }
        // 如果查询时间x小于 结束时间 则满足条件
        if (endDate.compareTo(queryDate) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 返回几天前的 起始 Timestamp
     * i == 0 表示 当前Timestamp
     * i == 1 表示 1天前的Timestamp
     */
    public static Timestamp getFewDaysAgoTimestamp(int i) {
        if (i == 0) {
            return new Timestamp(System.currentTimeMillis());
        }
        return getTimestamp(getDateYMD(i), 0);
    }

    /**
     * 返回几天前的日期（格式 2016-09-22）
     *
     * @param i == 0 表示 1天前的日期
     * @return
     */
    public static String getDateYMD(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        return df.format(cal.getTime());
    }

    /**
     * 返回几天前的日期（格式 20160922）
     *
     * @param i == 0 表示 1天前的日期
     * @return
     */
    public static String getDateYMD2(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        return df2.format(cal.getTime());
    }

    /**
     * 比较两个时间大小
     *
     * @param date1
     * @param date2 date1大于date2 true
     */
    public static boolean compareDate(String date1, String date2) {
        return Integer.valueOf(date1.replace("-", "")) > Integer.valueOf(date2.replace("-", ""));
    }

    /**
     * 返回当前时间 HH时mm分ss秒
     *
     * @return
     */
    public static String currentDateHMS() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param sDate 开始时间  20170510
     * @param eDate 结束时间  20170513
     * @return List<String> ： <20170510,20170511，20170512，20170513 >
     */
    public static List<String> getDatesBetweenTwoDate(String sDate, String eDate) {
        logger.debug("###  getDatesBetweenTwoDate beginDate '{}',endDate '{}'", sDate, eDate);

        List<String> listDate = new LinkedList();

        if (sDate.equals(eDate)) {
            listDate.add(sDate);
            return listDate;
        }

        Date beginDate = null;
        Date endDate = null;
        try {
            if (null == sDate || "".equals(sDate)) {
                return null;
            }
            if (null == eDate || "".equals(eDate)) {
                return null;
            }
            beginDate = df2.parse(sDate);
            endDate = df2.parse(eDate);
        } catch (ParseException e) {
            logger.info("!!!!!! 日期解析异常 ParseException , beginDate '{}',endDate '{}'", sDate, eDate);
            return null;
        }


        listDate.add(df2.format(beginDate));//把开始时间加入集合

        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                //  时间间隔最大 60 天
                if (listDate.size() > 60) {
                    break;
                }
                listDate.add(df2.format(cal.getTime()));
            } else {
                break;
            }
        }
        listDate.add(df2.format(endDate));//把结束时间加入集合
        return listDate;
    }

}

/**
 * <pre>
 * 날짜 관련.
 * </pre>
 *
 * @ClassName   : gscDateUtil.java
 * @Description : 클래스 설명을 기술합니다.
 * @author      : HyungJin, Ju
 * @since       : 2023-01-26
 * @version     : 1.0
 * @see
 * @Modification Information
 * <pre>
 * since          author         description
 * ============   ============   ===================================
 * 2023-01-26     HyungJin, Ju   최초 생성
 * </pre>
 */

package com.gsc.util;

import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.Context;
import org.apache.commons.lang3.time.FastDateFormat;
import org.hsqldb.lib.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class gscDateUtil {
    public static final String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_DATE_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
    public static final String FORMAT_DATE_YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";

    public static SimpleDateFormat FORMAT_DATE_CHK = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

    public static SimpleDateFormat FORMAT_DATE_COMMON = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);

    /**
     * <pre>
     * 입력 Date 의 Format을 "MM/dd/yyyy hh:mm:ss a" Format 으로 변경 후, String 으로 Return 한다.
     * </pre>
     *
     * @param InputDate
     * @return OutputDate
     * @throws Exception
     */
    public static String getCommonDateFormat(Date InputDate) throws Exception {
        try {
            String OutputDate;
            OutputDate = FORMAT_DATE_COMMON.format(InputDate);

            return OutputDate;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean isDateFormat(String InputDate, String dateFormat){
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat(dateFormat);
            dateFormatParser.setLenient(false);
            dateFormatParser.parse(InputDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <pre>
     * Context의 Clitent Time Off Set을 Return한다.
     * </pre>
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static double getClientTZOffset(Context context) throws Exception {
        double iClientTimeOffset = -9.0d;
        try {
            TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
            double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
            iClientTimeOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return iClientTimeOffset;

    }

    /**
     * <pre>
     * 현재 일자를 리턴한다.
     * </pre>
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getCurrentDate(Context context, boolean isDisplay) throws Exception {
        String strReturnValue = "";
        try {
            Calendar cal = new GregorianCalendar();

            strReturnValue = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), context.getLocale()).format(cal.getTime());
            if (isDisplay) {
                TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
                double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
                double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
                strReturnValue = eMatrixDateFormat.getFormattedDisplayDate(strReturnValue, clientTZOffset, context.getLocale());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strReturnValue;
    }

    /**
     * <PRE>
     * 현재 날짜를 argument의 format 값으로 치환 함.
     *</PRE>
     * @param format 날짜 format
     * @return
     * @throws Exception
     */
    public static String getCurrentDateFormatString(String format) {
        String strReturnValue = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date();
            strReturnValue = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturnValue;
    }

    /**
     * <pre>
     * 현재 일자에 입력받은 일수를 가감하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param intAddCount
     * @return
     * @throws Exception
     */
    public static String addDay(Context context, int intAddCount, boolean isDisplay) throws Exception {
        String strReturnValue = "";
        try {
            strReturnValue = addDate(context, intAddCount, true);

            if (isDisplay) {
                TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
                double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
                double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();

                strReturnValue = eMatrixDateFormat.getFormattedDisplayDate(strReturnValue, clientTZOffset, context.getLocale());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strReturnValue;
    }

    /**
     * <pre>
     * 현재 일자에 입력받은 달수를 가감하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param intAddCount
     * @return
     * @throws Exception
     */
    public static String addMonth(Context context, int intAddCount, boolean isDisplay) throws Exception {
        String strReturnValue = "";
        try {
            strReturnValue = addDate(context, intAddCount, false);

            if (isDisplay) {
                TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
                double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
                double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();

                strReturnValue = eMatrixDateFormat.getFormattedDisplayDate(strReturnValue, clientTZOffset, context.getLocale());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strReturnValue;
    }

    /**
     * <pre>
     * 현재 일자에 가감하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param intAddCount
     * @param isDay
     * @return
     * @throws Exception
     */
    public static String addDate(Context context, int intAddCount, boolean isDay) throws Exception {
        String strReturnValue = "";
        try {
            int iDate = 0;
            int iMonth = 0;
            if (isDay) {
                iDate = intAddCount;
            } else {
                iMonth = intAddCount;
            }

            strReturnValue = addDate(context, iMonth, iDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strReturnValue;
    }

    /**
     * <pre>
     * 현재 일자에 가감하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param month
     * @param date
     * @return
     * @throws Exception
     */
    public static String addDate(Context context, int month, int date) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, date);
        return new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), context.getLocale()).format(cal.getTime());
    }

    /**
     * <pre>
     * 넘겨 받은 Date String에 월 혹은 일을 가감하여 리턴한다.
     * </pre>
     *
     * @param context
     * @param month
     * @param date
     * @return
     * @throws Exception
     */
    public static Calendar addDateForMatrixFormat(Context context, String dateString, int month, int date) throws Exception {
        Date javaDate = eMatrixDateFormat.getJavaDate(dateString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(javaDate);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, date);

        return cal;
    }

    /**
     * <pre>
     * 날짜 비교하는 함수. 같으면 0 Start가 End보다 크면 1 Start가 End 보다 작으면 -1 을 리턴한다.
     * </pre>
     *
     * @param strDateFormat
     * @param strCompareDateStart
     * @param strCompareDateEnd
     * @param bleCompareTime
     * @return
     * @throws Exception
     */
    public static int compareTo(String strDateFormat, String strCompareDateStart, String strCompareDateEnd, boolean bleCompareTime) throws Exception {
        int intResult = 0;
        try {
            FastDateFormat fstDateFormat = null;

            if (StringUtil.isEmpty(strDateFormat)) {
                fstDateFormat = FastDateFormat.getInstance(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            } else {
                fstDateFormat = FastDateFormat.getInstance(strDateFormat);
            }

            Calendar dtCompareDateStart = Calendar.getInstance();
            Calendar dtCompareDateEnd = Calendar.getInstance();

            dtCompareDateStart.setTime(fstDateFormat.parse(strCompareDateStart));
            dtCompareDateEnd.setTime(fstDateFormat.parse(strCompareDateEnd));
            if (!bleCompareTime) {
                dtCompareDateStart.set(dtCompareDateStart.get(Calendar.YEAR), dtCompareDateStart.get(Calendar.MONTH), dtCompareDateStart.get(Calendar.DATE), 0, 0, 0);
                dtCompareDateStart.set(Calendar.MILLISECOND, 0);

                dtCompareDateEnd.set(dtCompareDateEnd.get(Calendar.YEAR), dtCompareDateEnd.get(Calendar.MONTH), dtCompareDateEnd.get(Calendar.DATE), 0, 0, 0);
                dtCompareDateEnd.set(Calendar.MILLISECOND, 0);
            }

            intResult = dtCompareDateStart.compareTo(dtCompareDateEnd);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return intResult;
    }

    /**
     * <pre>
     * 날짜 비교하는 함수. 같으면 0 현재날짜가 Compare Date보다 크면 1 현재날짜가 Compare Date 보다 작으면 -1 을 리턴한다.
     * </pre>
     *
     * @param strDateFormat
     * @param strCompareDate
     * @param bleCompareTime
     * @return
     * @throws Exception
     */
    public static int compareCurrentDate(String strDateFormat, String strCompareDate, boolean bleCompareTime) throws Exception {
        int intResult = 0;
        try {
            FastDateFormat fstDateFormat = null;

            if (StringUtil.isEmpty(strDateFormat)) {
                fstDateFormat = FastDateFormat.getInstance(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            } else {
                fstDateFormat = FastDateFormat.getInstance(strDateFormat);
            }

            Calendar dtCompareCurrentDate = Calendar.getInstance();
            Calendar dtCompareDate = Calendar.getInstance();

            dtCompareDate.setTime(fstDateFormat.parse(strCompareDate));
            if (!bleCompareTime) {
                dtCompareCurrentDate.set(dtCompareCurrentDate.get(Calendar.YEAR), dtCompareCurrentDate.get(Calendar.MONTH), dtCompareCurrentDate.get(Calendar.DATE), 0, 0, 0);
                dtCompareCurrentDate.set(Calendar.MILLISECOND, 0);

                dtCompareDate.set(dtCompareDate.get(Calendar.YEAR), dtCompareDate.get(Calendar.MONTH), dtCompareDate.get(Calendar.DATE), 0, 0, 0);
                dtCompareDate.set(Calendar.MILLISECOND, 0);
            }

            intResult = dtCompareCurrentDate.compareTo(dtCompareDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return intResult;
    }

    /**
     * <pre>
     * eMatrixDateFormat으로 날짜 비교하는 함수. 같으면 0 Start가 End보다 크면 1 Start가 End 보다 작으면 -1 을 리턴한다.
     * </pre>
     *
     * @param strCompareDateStart
     * @param strCompareDateEnd
     * @param bleCompareTime
     * @return
     * @throws Exception
     */
    public static int compareTo(String strCompareDateStart, String strCompareDateEnd, boolean bleCompareTime) throws Exception {
        int intResult = 0;
        try {

            intResult = compareTo(null, strCompareDateStart, strCompareDateEnd, bleCompareTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return intResult;

    }

    /**
     * <PRE>
     * MatrixDateFormat 날짜를  format 형태로 반환하여 준다.
     * </PRE>
     * @param inputDate MatrixDate
     * @param inputFormat 변환할 format
     * @return String date
     * @throws Exception
     *
     */
    public static String convertDateType(String inputDate, String inputFormat) throws Exception {
        String strDate = "";
        try {
            SimpleDateFormat sdfInputDate = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            SimpleDateFormat sdfOutputDate = new SimpleDateFormat(inputFormat, Locale.US);
            strDate = sdfOutputDate.format(sdfInputDate.parse(inputDate));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return strDate;
    }

    /**
     * <PRE>
     * format 형태 날짜를 MatrixDateFormat 날짜 형태로 반환하여 준다.
     * </PRE>
     * @param inputDate Date
     * @param inputFormat input format
     * @return String date
     * @throws Exception
     *
     */
    public static String convertDateFormat(String inputDate, String inputFormat) {
        String strDate = "";
        try {
            SimpleDateFormat sdfInputDate = new SimpleDateFormat(inputFormat, Locale.US);
            SimpleDateFormat sdfOutputDate = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            strDate = sdfOutputDate.format(sdfInputDate.parse(inputDate));
        } catch (Exception e) {
        }
        return strDate;
    }

    /**
     * <pre>
     * 에노비아 Date format 형태로 반환.
     * </pre>
     * @param context the eMatrix <code>Context</code> object
     * @param strDate date
     * @param strFormat format
     * @param isTo To
     * @return String (Format Date)
     */
    public static String transformDateFormat(Context context, String strDate, String strFormat, boolean isTo) {
        String strResult = "";
        try {
            if (!strDate.equals("")) {
                strResult = transformDate(context, strDate, isTo);
                strResult = gscDateUtil.convertDateType(strResult, strFormat);
            }
        } catch (Exception e) {
        }
        return strResult;
    }

    /**
     * <pre>
     * Diplay Date 에노비아 Date로 포맷
     * </pre>
     *
     * @param context the eMatrix <code>Context</code> object
     * @param strDisplayDate Display date
     * @return String (eMatrixDateFormat Date)
     */
    public static String transformDate(Context context, String strDisplayDate) {
        return transformDate(context, strDisplayDate, false);
    }

    /**
     * <pre>
     * Diplay Date 에노비아 Date로 포맷
     * </pre>
     * @param context the eMatrix <code>Context</code> object
     * @param strDisplayDate Display date
     * @param isTo To
     * @return String (eMatrixDateFormat Date)
     */
    public static String transformDate(Context context, String strDisplayDate, boolean isTo) {
        String formatDate = "";
        try {
            if (UIUtil.isNullOrEmpty(strDisplayDate)) {
                return strDisplayDate;
            }
            TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
            double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
            double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
            formatDate = eMatrixDateFormat.getFormattedInputDate(context, strDisplayDate, clientTZOffset, context.getLocale());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formatDate;
    }

    /**
     * <pre>
     * 현재 일자/시간을 리턴한다.
     * </pre>
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getCurrentDateTime(Context context, boolean isDisplay) throws Exception {
        String strReturnValue = "";
        try {
            Calendar cal = new GregorianCalendar();

            strReturnValue = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), context.getLocale()).format(cal.getTime());
            TimeZone tz = TimeZone.getTimeZone(context.getSession().getTimezone());
            double dbMilisecondsOffset = (double) (-1) * tz.getRawOffset();
            double clientTZOffset = (new Double(dbMilisecondsOffset / (1000 * 60 * 60))).doubleValue();
            if (isDisplay) {
                strReturnValue = eMatrixDateFormat.getFormattedDisplayDateTime(strReturnValue, clientTZOffset, context.getLocale());
            } else {
                strReturnValue = eMatrixDateFormat.getFormattedDisplayDate(strReturnValue, clientTZOffset, context.getLocale());
                SimpleDateFormat actual = new SimpleDateFormat("HH:mm:ss a", new Locale("en")); // AM, PM
                Date date = new Date();
                String time = actual.format(date);
                strReturnValue = eMatrixDateFormat.getFormattedInputDateTime(context, strReturnValue, time, clientTZOffset, context.getLocale());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strReturnValue;
    }
}

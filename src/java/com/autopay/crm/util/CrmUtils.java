/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autopay.crm.util;

import com.autopay.crm.util.CrmConstants.CustomerType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Judy
 */
public class CrmUtils {

    public static double getNumber(String input) throws Exception {
        //initialize variables (constants)
        final char NULL = Character.UNASSIGNED;
        final char[] data = input.toCharArray();
        final int N = data.length;

        //initialize variables (transients)
        double scale = 1;

        //process input
        boolean replaceDollarSequence = true;
        boolean lookForDollar = true;
        boolean lookForParentheses = true;

        int index_dollar = -1;
        int index_left = -1;
        int index_right = -1;
        for (int i = 0; i < N; i++) {
            //remove commas
            if (data[i] == ',') {
                data[i] = NULL;
            }

            //replace dollar sequence
            if (replaceDollarSequence) {
                if (i + 2 < N) {
                    if ((data[i] == '\"') && (data[i + 1] == '$') && (data[i + 2] == '\"')) {
                        data[i] = data[i + 2] = NULL;
                        replaceDollarSequence = false;
                    }
                }
            }

            //look for dollar
            if (lookForDollar) {
                if (data[i] == '$') {
                    if (index_dollar == -1) {
                        index_dollar = i;
                    } else {
                        index_dollar = -1;
                        lookForDollar = false;
                    }
                }
            }

            //look for parentheses
            if (lookForParentheses) {
                if (data[i] == '(') {
                    if (index_left == -1) {
                        index_left = i;
                    } else {
                        index_left = -1;
                        lookForParentheses = false;
                    }
                } else if (data[i] == ')') {
                    if (index_right == -1) {
                        index_right = i;
                    } else {
                        index_right = -1;
                        lookForParentheses = false;
                    }
                }
            }
        }

        if (index_dollar != -1) {
            data[index_dollar] = NULL;
        }
        if ((index_left != -1) && (index_right != -1)) {
            data[index_left] = data[index_right] = NULL;
            scale = -scale;
        }

        //trim input
        {
            for (int i = 0; i < N; i++) {
                if (data[i] == ' ') {
                    data[i] = NULL;
                } else {
                    i = N;
                }
            }
            for (int i = (N - 1); i >= 0; i--) {
                if (data[i] == ' ') {
                    data[i] = NULL;
                } else {
                    i = -1;
                }
            }
        }

        //see if there is a %
        for (int i = (N - 1); i >= 0; i--) {
            if (data[i] != -1) {
                if (data[i] == '%') {
                    scale *= .01;
                    data[i] = NULL;
                }
            }
        }

        //create new input
        int index_current = 0;
        for (int i = 0; i < N; i++) {
            if (data[i] != NULL) {
                data[index_current++] = data[i];
            }
        }

        return scale * Double.parseDouble(new String(data, 0, index_current));
    }

    public static double getNumberForExcelImport(String input) throws Exception {
        //remove ""
        if (input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1, input.length() - 1);
        }
        if (input.startsWith("$")) {
            if (input.length() >= 3) {
                int pos = input.length() - 3;
                String thisChar = input.substring(pos, pos + 1);
                if (thisChar.equals(",") || thisChar.equals(";")) {
                    input = input.substring(0, pos) + "." + input.substring(pos + 1);
                }
            }
        }

        return getNumber(input);
    }

    public static String trimName(final String name) {
        String result = name.trim();
        //convert to uppercase
        result = result.toUpperCase();

        //remove content between (), {}, []
        result = removeContentBetween(result, "(", ")");
        result = removeContentBetween(result, "{", "}");
        result = removeContentBetween(result, "[", "]");

        //remove special characters like ".", ",", "'", "&", "$", "#", "%", "!", "\\"
        result = removeCharacters(result, CrmConstants.SPECIAL_CHARACTERS);

        //remove surfix "LLC", "INC", "LTD"
        if (result.endsWith(" LLC") || result.endsWith(" INC") || result.endsWith(" LTD")) {
            result = result.substring(0, result.length() - 4);
        }

        //remove " AND ", " OR ", " NO "
        if (result.indexOf(" AND ") >= 0) {
            result = result.replaceAll(" AND ", " ");
        }
        if (result.indexOf(" OR ") >= 0) {
            result = result.replaceAll(" OR ", " ");
        }
        if (result.indexOf(" NO ") >= 0) {
            result = result.replaceAll(" NO ", " ");
        }


        //remove all spaces
        result = removeCharacters(result, new String[]{" "});

        return result;
    }

    private static String removeCharacters(final String name, final String[] characters) {
        String result = "";
        final char[] charArray = name.toCharArray();
        List<String> characterList = Arrays.asList(characters);
        for (char c : charArray) {
            if (!characterList.contains(String.valueOf(c))) {
                result = result + c;
            }
        }
        return result.trim();
    }

    private static String removeContentBetween(final String name, final String start, final String end) {
        String result = name;
        int pos1 = name.indexOf(start);
        int pos2 = name.indexOf(end);
        if (pos1 >= 0 && pos2 >= 0 && pos2 > pos1) {
            result = result.substring(0, pos1) + result.substring(pos2 + 1);
        }
        return result.trim();
    }

    public static int getMonthPosition(final int lastNumOfMonths, final Date dealDate) {
        Calendar now = Calendar.getInstance();
        int curyear = now.get(Calendar.YEAR);
        int curmonth = now.get(Calendar.MONTH); // zero based
        curmonth++;
        
        Calendar then = Calendar.getInstance();
        then.setTime(dealDate);
        int thenyear = then.get(Calendar.YEAR);
        int thenmonth = then.get(Calendar.MONTH); // zero based
        thenmonth++;
        
        return (thenmonth - ((curyear-thenyear)*12+curmonth-lastNumOfMonths+1));
    }
    
    public static String[] getMonthNames(final int lastNumOfMonths) {
        Calendar now = Calendar.getInstance();
        int curyear = now.get(Calendar.YEAR);
        int curmonth = now.get(Calendar.MONTH); //zero based
        
        String[] result = new String[lastNumOfMonths];
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
        int yearDiff = 0;
        for (int i = lastNumOfMonths-1; i >= 0; i--) {
            if ((curmonth-i) < 0) {
                yearDiff = 1;
            }
            now.set(curyear-yearDiff, (yearDiff*12 + curmonth - i), 1);
            String monthStr = sdf.format(new Date(now.getTimeInMillis()));
            result[lastNumOfMonths-1-i] = monthStr;
        }
        return result;
    }
    
    public static int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int curyear = now.get(Calendar.YEAR);
        return curyear;
    }
    
    public static Date getDate(final int year, final int month) {
        Calendar now = Calendar.getInstance(); //month is zero based in Calendar
        now.set(year, month-1, 1);
        return new Date(now.getTimeInMillis());
    }
    
    public static String getDateString(final Date date, final String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }
    
    public static Date getDate(final String dateStr, final String formatStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date result = null;
        //try {
            result = sdf.parse(dateStr);
//        } catch (ParseException ex) {
//            Logger.getLogger(CrmUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return result;
    }
    
    public static Date getLastMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH); // zero based
        if (m-1 <= 0) {
            calendar.set(y-1, 12+m-1, 1);
        } else {
            calendar.set(y, m-1, 1);
        }
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date getLastMonthEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH); // zero based
        if (m-1 <= 0) {
            calendar.set(y-1, 12+m-1, 30);
        } else {
            calendar.set(y, m-1, 30);
        }
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date convertToFirstDayOfMonth(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH); // zero based
        calendar.set(y, m, 1);
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Date converToLastDayOfMonth(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH); // zero based
        if (m == 1) {
            calendar.set(y, m, 28);
        } else {
            calendar.set(y, m, 30);
        }
        return new Date(calendar.getTimeInMillis());
    }
    
    public static List<Integer> getItemsPerPageList() {
        List<Integer> result = new ArrayList<Integer>();
        result.add(25);
        result.add(50);
        result.add(100);
        return result;
    }
    
    public static List<String> getStateList() {
        List<String> result = new ArrayList<String>();
        result.add("AK");
        result.add("AL");
        result.add("AR");
        result.add("AZ");
        result.add("CA");
        result.add("CO");
        result.add("CT");
        result.add("DC");
        result.add("DE");
        result.add("FL");
        result.add("GA");
        result.add("HI");
        result.add("IA");
        result.add("ID");
        result.add("IL");
        result.add("IN");
        result.add("KS");
        result.add("KY");
        result.add("LA");
        result.add("MA");
        result.add("MD");
        result.add("ME");
        result.add("MI");
        result.add("MN");
        result.add("MO");
        result.add("MS");
        result.add("MT");
        result.add("NC");
        result.add("ND");
        result.add("NE");
        result.add("NH");
        result.add("NJ");
        result.add("NM");
        result.add("NV");
        result.add("NY");
        result.add("OH");
        result.add("OK");
        result.add("OR");
        result.add("PA");
        result.add("RI");
        result.add("SC");
        result.add("SD");
        result.add("TN");
        result.add("TX");
        result.add("UT");
        result.add("VA");
        result.add("WA");
        result.add("WI");
        result.add("WV");
        result.add("WY");
        result.add("ZZ");
        return result;
    }
    
    public static String getTaskCalendarContent(final long uid, final long sequence, final Date date, final String organizerEmail, final String attendeeEmail, final String taskName, final String taskDesc, final boolean cancelled) {
        SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 8);
        Date start = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 8);
        Date end = cal.getTime();
        String calendarContent =
                    "BEGIN:VCALENDAR\n"
                    + (cancelled ? "METHOD:CANCEL\n" : "METHOD:REQUEST\n")
                    + "PRODID: " + taskName + "\n"
                    + "VERSION:2.0\n"
                    + "BEGIN:VEVENT\n"
                    + "DTSTAMP:" + iCalendarDateFormat.format(start) + "\n"
                    + "DTSTART:" + iCalendarDateFormat.format(start) + "\n"
                    + "DTEND:" + iCalendarDateFormat.format(end) + "\n"
                    + "SUMMARY:Task Assigned To You\n"
                    + "UID:" + uid + "\n"
                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:" + attendeeEmail + "\n"
                    + "ORGANIZER:MAILTO:" + organizerEmail + "\n"
                    + "LOCATION:Your office\n"
                    + "DESCRIPTION:" + (cancelled ? "Task has been cancelled: " + taskDesc : taskDesc) + "\n"
                    + "SEQUENCE:" + sequence + "\n"
                    + "PRIORITY:5\n"
                    + "CLASS:PUBLIC\n"
                    + (cancelled ? "STATUS:CANCELLED\n" : "STATUS:CONFIRMED\n")
                    + "TRANSP:OPAQUE\n"
                    + "BEGIN:VALARM\n"
                    + "ACTION:DISPLAY\n"
                    + "DESCRIPTION:REMINDER\n"
                    + "TRIGGER;RELATED=START:-PT00H15M00S\n"
                    + "END:VALARM\n"
                    + "END:VEVENT\n"
                    + "END:VCALENDAR";
        
        return calendarContent;
    }
    
    public static String getCustomerTypeAbbr(final String type) {
        if (type.equalsIgnoreCase(CustomerType.DEALER.name())) {
            return "D";
        } else if (type.equalsIgnoreCase(CustomerType.FINANCE_COMPANY.name())) {
            return "F";
        } else if (type.equalsIgnoreCase(CustomerType.BOTH.name())) {
            return "B";
        } else {
            return type;
        }
    }
}

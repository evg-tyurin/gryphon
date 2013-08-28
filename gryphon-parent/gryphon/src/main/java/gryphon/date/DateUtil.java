package gryphon.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class DateUtil
{
	private static final String EAPO_DATE_FORMAT = "yyyy.MM.dd";

	public static final long MINUTE = 60 * 1000L;

	public static final long HOUR = 60 * 60 * 1000L;

	public static final long DAY = 24 * 60 * 60 * 1000L;

	public static final long WEEK = 7 * 24 * 60 * 60 * 1000L;

	public static final long MONTH = 30 * 24 * 60 * 60 * 1000L;

	/** —писок праздничных дней в формате d.m,d.mm,dd.m, */
	private static String holidays;

    /**
     * @param date
     * @return дата в формате yyyy.MM.dd
     */
    public static String formatToEapoDate(Date date)
    {
        try{
        	return new SimpleDateFormat(EAPO_DATE_FORMAT).format(date);
        }catch(Exception e){
        	return null;
        }
    }
    /**
     * @param date
     * @return дата в формате yyyyMMdd
     */
    public static String formatToXMLDate(Date date)
    {
        try{
        	return new SimpleDateFormat("yyyyMMdd").format(date);
        }catch(Exception e){
        	return null;
        }
    }
    /**
     * 
     * @param date дата в формате yyyy.MM.dd
     * @return дата в формате yyyyMMdd
     */
    public static String formatToXMLDate(String date) {
    	try{
    		Date d = new SimpleDateFormat(EAPO_DATE_FORMAT).parse(date);
        	return new SimpleDateFormat("yyyyMMdd").format(d);
        }catch(Exception e){
        	return null;
        }
	}
    /*--- From Apache Lang ---*/
    /**
     * Adds a number of days to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }
    /**
     * Adds a number of hours to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }
    /**
     * From Apache Lang.
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     * @deprecated Will become privately scoped in 3.0
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
    /**
     * ѕарсит дату из строки в формате yyyy.MM.dd
     * @param date строка
     * @return дата
     * @throws ParseException
     */
	public static Date parseEapoDate(String date)
	{
		try
		{
			return new SimpleDateFormat(EAPO_DATE_FORMAT).parse(date);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	/** 
	 * —колько календарных дней между датами
	 * @param date бќльша€ дата
	 * @param otherDate меньша€ дата 
	 * @throws Exception 
	 */
	public static int getDaysBetween(Date date, Date otherDate) throws Exception
	{
		if (isSameDays(date, otherDate))
			return 0;
		int sign = date.after(otherDate) ? 1 : -1;
		Date minDate = date.after(otherDate) ? otherDate : date;
		Date maxDate = date.after(otherDate) ? date : otherDate;
		int days = 0;
		for (days = 1; days <= 1000; days++)
		{
			Date newDate = DateUtil.addDays(minDate, days);
			if (isSameDays(newDate,maxDate))
				break;
		}
		return days * sign;
	}
	/** 
	 * —колько рабочих дней между датами
	 * @param date бќльша€ дата
	 * @param otherDate меньша€ дата 
	 * @throws Exception 
	 */
	public static int getWorkingDaysBetween(Date date, Date otherDate) throws Exception
	{
		if (isSameDays(date, otherDate))
			return 0;
		int sign = date.after(otherDate) ? 1 : -1;
		Date minDate = date.after(otherDate) ? otherDate : date;
		Date maxDate = date.after(otherDate) ? date : otherDate;
		int workingDays = 0;
		for (int days = 1; days <= 1000; days++)
		{
			Date newDate = DateUtil.addDays(minDate, days);
			if (isWorkingDay(newDate))
				workingDays++;
			if (isSameDays(newDate,maxDate))
				break;
		}
		return workingDays * sign;
	}
	/** ѕриходитс€ ли дата на рабочий день */
	public static boolean isWorkingDay(Date date) throws Exception
	{
		int dayOfWeek = date.getDay();
		String ddmm = date.getDate()+"."+(date.getMonth()+1);
		String holidays = getHolidays();
		if (holidays.indexOf(ddmm)>=0)
			return false;
		return dayOfWeek != 0 && dayOfWeek != 6;
	}
	private static String getHolidays() throws Exception
	{
		if (holidays==null){
			holidays = ResourceBundle.getBundle("dateUtil").getString("holidays");
		}
		return holidays;
	}
	/** ќтнос€тс€ ли обе даты к одному календарному дню */
	public static boolean isSameDays(Date date1, Date date2)
	{
		return date1.getYear()==date2.getYear() && date1.getMonth()==date2.getMonth() && date1.getDate()==date2.getDate();
	}

}

/**
 * 
 */
package net.rickcee.tradematcher.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author rickcee
 *
 */
public class RCConstants {
	private static final String YYYY_MM_DD = "yyyyMMdd";
	public static final String BUY = "B";
	public static final String SELL = "S";
	public static final String EURO = "EUR";
	public static final String USD = "USD";
	public static final String LE1 = "LE1";
	public static final String LE2 = "LE2";
	public static final String SRC1 = "SRC1";
	public static final String SRC2 = "SRC2";

	private static SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);

	public static String getDate(Date date) {
		synchronized (sdf) {
			return sdf.format(date);
		}
	}
	
	public static String getDate(LocalDate date) {
		//return LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
		return date.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
	}
}

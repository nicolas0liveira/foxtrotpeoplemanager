/**
 * 
 */
package test.foxtrotpeoplemanager.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class goals is a time manipulate.
 * Its responsible to do operations with timestamps and so on.
 * 
 * @version: 1.0
 * @author: Nicolas Oliveira
 **/
public class TimerHandler {
	
	private Calendar calendar = Calendar.getInstance();

	
	/**
	 * This method receives a timestamp as String and return a object
	 * sql.Timestamp.
	 * 
	 * @param timestamp
	 *            The timestamp as String (Probably from a mysql database)
	 * @return sql.Timestamp based on timestampString
	 * 
	 * @throws ParseException
	 *             on the conversion
	 */
	public Timestamp timeStringToTimestamp(String timestamp){
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Timestamp ts = null;
		Date date;
		
		try {
			date = timeFormat.parse(timestamp);
			ts = new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		
		return ts;
			
	}
	

}

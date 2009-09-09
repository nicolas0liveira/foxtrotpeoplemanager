package test.foxtrotpeoplemanager.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TesteMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date d = new Date();
		Timestamp t = new Timestamp(d.getTime());
		
		Date d1 = new Date(t.getTime());
		

		Calendar c = Calendar.getInstance(new Locale("pt","BR"));
		
		
		DateFormat df = DateFormat.getInstance();
		
		System.out.println(df.format(c.getTime()));
		c.roll(Calendar.DAY_OF_MONTH, -2);
		System.out.println(df.format(c.getTime()));
		
		System.out.println(d);
		System.out.println(t);
		System.out.println(d1);
		
		
		
		
		

	}

}

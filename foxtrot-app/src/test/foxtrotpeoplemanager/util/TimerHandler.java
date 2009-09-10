/**
 * 
 */
package test.foxtrotpeoplemanager.util;

import java.util.Date;

/**
 * This class goals is a time manipulate.
 * Its responsible to do operations with timestamps and so on.
 * 
 * @version: 1.0
 * @author: Nicolas Oliveira
 **/
public class TimerHandler {
	
	public long getDifYear(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}
		
		return dayAfter.getYear() - dayBefore.getYear();
		 
	}
	
	public long getDifMonth(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}
		
		
		return dayAfter.getMonth() - dayBefore.getMonth() + 
			this.getDifYear(dayAfter, dayBefore)*12;  
	}
	
	public long getDifDay(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}
		
		 return (dayAfter.getTime() - dayBefore.getTime()) / (1000*60*60*24);
	}
	
	public long getDifHour(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}	
		
		return (dayAfter.getTime() - dayBefore.getTime()) / (1000*60*60);
	}
	
	public long getDifMin(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}	
		
		return (dayAfter.getTime() - dayBefore.getTime()) / (1000*60);
	}
	
	public long getDifSec(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}		
		return (dayAfter.getTime() - dayBefore.getTime()) / (1000);
	}

	public long getDifMiliSec(Date time1, Date time2){
		Date dayAfter = null;
		Date dayBefore = null;
		
		if(time1.getTime() >= time2.getTime()){
			dayAfter = time1;
			dayBefore = time2;
			
		} else{
			dayAfter = time2;
			dayBefore = time1;
		}
		
		return dayAfter.getTime() - dayBefore.getTime();
	}
}

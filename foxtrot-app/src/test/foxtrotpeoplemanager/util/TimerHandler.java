/**
 * 
 */
package test.foxtrotpeoplemanager.util;

import java.util.Date;

/**
 * Esta classe é responsável por todas as manipulações e tratamentos referentes a horas. 
 * 
 * @version: 1.0
 * @author: Nicolas Oliveira
 **/
public class TimerHandler {
	
	/**
	 * String que representa a data para o banco MySql.
	 */
	public static final String MYSQL_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * String pattern para representar a data para o usuario final na Web.
	 */
	public static final String SIMPLE_WEB_FORMAT = "dd/MM/yyyy";
	
	/**
	 * String pattern para representar a datacompleta  para o usuario final na Web.
	 */
	public static final String FULL_WEB_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	/**
	 * Representa o tempo de um dia em long.
	 */
	public static final long DAY_TIME = 24L * 60L * 60L * 1000L;
	
	
	/**
	 * Retorna a diferença, em anos, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em anos entre as duas datas
	 */
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
	
	/**
	 * Retorna a diferença, em meses, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em meses entre as duas datas
	 */
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
	
	/**
	 * Retorna a diferença, em dias, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em dias entre as duas datas
	 */
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
	
	/**
	 * Retorna a diferença, em horas, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em horas entre as duas datas
	 */
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
	
	/**
	 * Retorna a diferença, em minutos, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em minutos entre as duas datas
	 */
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
	
	/**
	 * Retorna a diferença, em segundos, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em segundos entre as duas datas
	 */
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

	/**
	 * Retorna a diferença, em milisegundos, entre duas datas. 
	 * @param time1 primeira data
	 * @param time2 segunda data
	 * @return diferença em milisegundos entre as duas datas
	 */
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

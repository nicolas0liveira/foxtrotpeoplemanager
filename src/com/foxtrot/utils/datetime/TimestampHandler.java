/* 
 * Copyright 2009 FoxtrotSystem.
 * */

package com.foxtrot.utils.datetime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of timestamp handler functions.
 * 
 * @version: 1.0
 * @author: Matheus Mota
 **/

public final class TimestampHandler {

	/**
	 * This method receives a timestamp as String and return a object
	 * sql.Timestamp.
	 * 
	 * @param timestampString
	 *            The timestamp as String (Probably from a mysql database)
	 * @return sql.Timestamp based on timestampString
	 * 
	 * @throws ParseException
	 *             on the conversion
	 */
	public static Timestamp newTimestampFromMysql(final String timestampString)
			throws ParseException {
		SimpleDateFormat mysqlTsFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = mysqlTsFormat.parse(timestampString);

		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}

	public static String formatTimestampAsHour(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / (1000 * 60 * 60 * 24);
		time = time % (1000 * 60 * 60 * 24);

		// verifica quantidade de horas.
		long horas = time / (1000 * 60 * 60);
		time = time % (1000 * 60 * 60);

		if (horas + (dias * 24) < 10) {
			resultado.append("0");
		}
		resultado.append(horas + (dias * 24));
		resultado.append(":");

		// verifica quantidade de minutos.
		long minutos = time / (1000 * 60);
		time = time % (1000 * 60);

		if (minutos < 10) {
			resultado.append("0");
		}
		resultado.append(minutos);
		resultado.append(":");

		// verifica quantidade de segundos.
		long segundos = time / 1000;
		time = time % 1000;

		if (segundos < 10) {
			resultado.append("0");
		}
		resultado.append(segundos);

		return resultado.toString();
	}

	public static String formatTimestampAsHourDay(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / (1000 * 60 * 60 * 24);
		time = time % (1000 * 60 * 60 * 24);

		if (dias == 1) {
			resultado.append(dias);
			resultado.append(" Dia ");
		} else if (dias > 0) {
			resultado.append(dias);
			resultado.append(" Dias ");
		}

		// verifica quantidade de horas.
		long horas = time / (1000 * 60 * 60);
		time = time % (1000 * 60 * 60);

		if (horas < 10) {
			resultado.append("0");
		}
		resultado.append(horas);
		resultado.append(":");

		// verifica quantidade de minutos.
		long minutos = time / (1000 * 60);
		time = time % (1000 * 60);

		if (minutos < 10) {
			resultado.append("0");
		}
		resultado.append(minutos);
		resultado.append(":");

		// verifica quantidade de segundos.
		long segundos = time / 1000;
		time = time % 1000;

		if (segundos < 10) {
			resultado.append("0");
		}
		resultado.append(segundos);

		return resultado.toString();
	}

	// A fazer
	public static String formatarTimestampAsHourDayMonth(final long time) {
		return "";
	}

	// A fazer
	public static String formatarTimestampAsHourDayMonthYear(final long time) {
		return "";
	}
}

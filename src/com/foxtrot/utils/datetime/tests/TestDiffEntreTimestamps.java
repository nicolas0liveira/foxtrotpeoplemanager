package com.foxtrot.utils.datetime.tests;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.foxtrot.utils.datetime.TimestampHandler;

public class TestDiffEntreTimestamps {

	public static void main(String[] a) {

		// Formato timestamp para mysql
		SimpleDateFormat mysqlTimestampFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		// Definindo um formato qualquer...
		SimpleDateFormat BrasilTimestampFormat = new SimpleDateFormat(
				"dd 'de' MMMM 'de' yyyy 'às' HH:mm:ss ", new Locale("pt",
						"BR"));
		try {
			// Simulando informa��o que chega pelo BD (entrada e sa�da)
			Date date1 = mysqlTimestampFormat.parse("2009-09-07 13:42:07");
			Date date2 = mysqlTimestampFormat.parse("2009-09-07 17:52:07");

			Timestamp ts1 = new Timestamp(date1.getTime());
			Timestamp ts2 = new Timestamp(date2.getTime());

			System.out.println("ts1: " + ts1);
			System.out.println("ts2: " + ts2);

			System.out.println("ts1 formatado: "
					+ BrasilTimestampFormat.format(date1));
			System.out.println("ts2 formatado: "
					+ BrasilTimestampFormat.format(date2));

			// Testando se posso fazer a diferen�a
			if (ts2.after(ts1)) {
				System.out
						.println("\n--\nRealizando cálculo da diferença (ts2-ts1):");
				Timestamp tsResultado = new Timestamp(ts2.getTime()
						- ts1.getTime());

				System.out.println("Resultado: " + tsResultado.getTime());
				System.out.println("Resultado timestamp: " + tsResultado);
				System.out
						.println("Resultado timstamp formatado (dias e horas): "
								+ TimestampHandler
										.formatTimestampAsHourDay(tsResultado
												.getTime()));
				System.out.println("Resultado timstamp formatado (horas): "
						+ TimestampHandler.formatTimestampAsHour(tsResultado
								.getTime()));

			} else {
				System.out.println("TS1 é maior que TS2. Hora negativa?");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

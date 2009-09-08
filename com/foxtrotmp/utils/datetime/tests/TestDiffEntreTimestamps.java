package com.foxtrotmp.utils.datetime.tests;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDiffEntreTimestamps {

	public static void main(String a[]) {
		SimpleDateFormat staticFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			// Simulando informação que chega pelo BD (entrada e saída)
			Date date1 = staticFormat.parse("2009-09-07 13:42:07");
			Date date2 = staticFormat.parse("2009-09-09 17:52:07");

			Timestamp ts1 = new Timestamp(date1.getTime());
			Timestamp ts2 = new Timestamp(date2.getTime());

			System.out.println("ts1: " + ts1);
			System.out.println("ts2: " + ts2);

			// Testando se posso fazer a diferença
			if (ts2.after(ts1)) {
				System.out.println("Realizando cálculo da diferença:");
				Timestamp tsResultado = new Timestamp(ts2.getTime()
						- ts1.getTime());

				System.out.println("Resultado timestamp: " + tsResultado);
				System.out
						.println("Resultado timstamp formatado (dias e horas): "
								+ TimestampHandler
										.formatarTimestampHorasDias(tsResultado
												.getTime()));
				System.out.println("Resultado timstamp formatado (horas): "
						+ TimestampHandler.formatarTimestampHoras(tsResultado
								.getTime()));

			} else {
				System.out.println("TS1 é maio que TS2. Hora negativa?");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

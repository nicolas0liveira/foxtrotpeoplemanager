package com.foxtrotmp.utils.datetime.tests;

public class TimestampHandler {
	public static String formatarTimestampHoras(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / (1000 * 60 * 60 * 24);
		time = time % (1000 * 60 * 60 * 24);

		

		// verifica quantidade de horas.
		long horas = time / (1000 * 60 * 60);
		time = time % (1000 * 60 * 60);

		if (horas < 10) {
			resultado.append("0");
		}
		resultado.append(horas+(dias*24));
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

	public static String formatarTimestampHorasDias(long time) {
		StringBuffer resultado = new StringBuffer();

		// verifica quantidade de dias.
		long dias = time / (1000 * 60 * 60 * 24);
		time = time % (1000 * 60 * 60 * 24);

		if (dias > 0) {
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

	// terminar...
	public static String formatarTimestampHorasDiasMeses(long time) {
		return "";
	}

	// terminar...
	public static String formatarTimestampHorasDiasMesesAnos(long time) {
		return "";
	}
}

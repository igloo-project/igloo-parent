/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.openwide.springmvc.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <p>Utilitaire de formatage des dates suivant différents formats.</p>
 * 
 * @author Open Wide
 */
public final class DateFormatUtils {

//	public static final String REALLY_SHORT_DATE = "date.format.really.short.date";
//	public static final String REALLY_SHORT_DATETIME = "date.format.really.short.datetime";
//	public static final String SHORT_DATE = "date.format.short.date";
//	public static final String SHORT_DATETIME = "date.format.short.datetime";
//	public static final String SHORT_TIME = "date.format.short.time";
//	public static final String TIME = "date.format.time";
//	public static final String MONTH_YEAR = "date.format.month-year";
//	public static final String VERY_LONG_DATETIME = "date.format.very.long.datetime";
//	public static final String LITERAL_DATE = "date.format.literal.date";
	
	public static final String TO_STRING_DATETIME = "dd/MM/yyyy HH:mm";
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static final String DOJO_DATE = "yyyy-MM-dd";
	public static final String DOJO_TIME = "'T'HH:mm:ss";
	
	public static final String SYSTEM_DATE = "yyyy-MM-dd HH:mm:ss";
	
	private static final char TIME_SEPARATOR_CHAR = ':';
	public static final String TIME_FORMAT = "HH" + TIME_SEPARATOR_CHAR + "mm";
	public static final String LONG_TIME_FORMAT = "HH" + TIME_SEPARATOR_CHAR + "mm" + TIME_SEPARATOR_CHAR + "ss";
	
	/**
	 * Formatte la date suivant le format fourni.
	 * 
	 * @param date date
	 * @param format format à respecter
	 * @return date formattée
	 */
	public static String format(Date date, String format) {
		String formattedDate = "";
		
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			formattedDate = formatter.format(date);
		}
		
		return formattedDate;
	}
	
	/**
	 * Formatte la date suivant le format et la locale fourni.
	 * 
	 * @param date date
	 * @param format format à respecter
	 * @param locale locale à utiliser
	 * @return date formattée
	 */
	public static String format(Date date, String format, Locale locale) {
		String formattedDate = "";
		
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
			formattedDate = formatter.format(date);
		}
		
		return formattedDate;
	}
	
	/**
	 * Formatte un nombre de minutes en m'ss".
	 * 
	 * @param secondes
	 * @return m'ss"
	 */
	public static String secondesToTimeString(int secondes) {
		int minutes = secondes / 60;
		int sec = secondes % 60;
		StringBuilder time = new StringBuilder();
		time.append(minutes);
		time.append('\'');
		if (sec < 10) {
			time.append('0');
		}
		time.append(sec);
		time.append('\"');
		return time.toString();
	}
	
	/**
	 * Formatte un nombre de minutes en HH:mm.
	 * 
	 * @param minutes
	 * @return HH:mm
	 */
	public static String minutesToTimeString(int minutes) {
		int heures = minutes / 60;
		int min = minutes % 60;
		StringBuilder time = new StringBuilder();
		time.append(heures);
		time.append(TIME_SEPARATOR_CHAR);
		if (min < 10) {
			time.append('0');
		}
		time.append(min);
		return time.toString();
	}
	
	/**
	 * Convertit un temps HH:mm en nombre de minutes.
	 * 
	 * @param time HH:mm
	 * @return nombre de minutes
	 * @throws ParseException si problème d'analyse de la chaîne
	 */
	public static int timeToMinutes(String time) throws ParseException {
		// appel au parsing de SimpleDateFormat pour vérifier le format
		// mais la Date en retour n'est pas utilisée car les heures au
		// dessus de 24 sont transformées en jours, semaines, etc.
		new SimpleDateFormat(TIME_FORMAT).parse(time);
		int separatorIndex = time.indexOf(TIME_SEPARATOR_CHAR);
		int heures = Integer.parseInt(time.substring(0, separatorIndex));
		int minutes = Integer.parseInt(time.substring(separatorIndex + 1));
		return heures * 60 + minutes;
	}
	
	/**
	 * <p>Convertit une chaîne dans le format donné en une Date.</p>
	 * <p>Remarque : le mois et le jour doivent être en anglais.</p>
	 * 
	 * @param dateString date
	 * @param format format de la date
	 * @return date
	 * @throws ParseException
	 */
	public static Date parseFromFormat(String dateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		return sdf.parse(dateString);
	}
	
	/**
	 * <p>Convertit une chaîne dans le format donné en une Calendar.</p>
	 * @see #parseFromFormat(String, String)
	 * 
	 * @param dateString date
	 * @param format format de la date
	 * @return calendar
	 * @throws ParseException
	 */
	public static Calendar getCalendarFromFormat(String dateString, String format) throws ParseException {
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(parseFromFormat(dateString, format));
		return calendarDate;
	}
	
	/**
	 * @return date un mois auparavant
	 */
	public static Date getDateIlYAUnMois() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	
	/**
	 * @return date décalé de X jours (dans le passé si négatif, dans l'avenir si positif)
	 */
	public static Date getDateDecaleeJours(int nombreJours) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, nombreJours);
		return calendar.getTime();
	}
	
	/**
	 * Met à zéro les champs HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND.
	 * @param calendar le calendrier
	 */
	public static void resetTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Constructeur privé utilisé pour empêcher une instanciation accidentelle de l'objet.
	 */
	private DateFormatUtils() {
	}


}

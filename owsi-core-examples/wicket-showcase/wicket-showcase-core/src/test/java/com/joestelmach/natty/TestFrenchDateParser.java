package com.joestelmach.natty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TestFrenchDateParser extends AbstractDateParserTest {
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	@Test
	public void testExplicitDateTime() throws ParseException {
		Date reference = DATE_FORMAT.parse("31/01/2012 17:00");
		CalendarSource.setBaseDate(reference);
		
		validateDate("12/02/2010", 2, 12, 2010);
		validateDate("22-8-1988", 8, 22, 1988);
		validateDate("21 mai 2001", 5, 21, 2001);
		validateDate("le 11 novembre 2022", 11, 11, 2022);
		validateDate("le treize juin 2010", 6, 13, 2010);
		
		// Ca ne fonctionne pas très bien dans la version originale
//		validateDate("le dernier mardi de janvier", 1, 17, 2012);
		
		validateDateTime("le 8 octobre 1987 à 15:31", 10, 8, 1987, 15, 31, 0);
		validateDateTime("le 15 avril à 15 heures 30 minutes 45 secondes", 4, 15, 2012, 15, 30, 45);
		
		List<Date> dates;
		DateGroup dateGroup;
		
		dateGroup = _parser.parse("le 30 janvier 2012 et le 3 février 2012").get(0);
		Assert.assertFalse(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 1, 30, 2012);
		validateDate(dates.get(1), 2, 3, 2012);
		
		dateGroup = _parser.parse("le 16 février ou 2 jours après").get(0);
		Assert.assertFalse(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 2, 16, 2012);
		validateDate(dates.get(1), 2, 18, 2012);
		
		dateGroup = _parser.parse("13/08/2007 ou 18/08/2008").get(0);
		Assert.assertFalse(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 8, 13, 2007);
		validateDate(dates.get(1), 8, 18, 2008);
	}
	
	@Test
	public void testRelativeDateTime() throws Exception {
		Date reference = DATE_FORMAT.parse("31/01/2012 17:00");
		CalendarSource.setBaseDate(reference);
		
		validateDateTime("aujourd'hui à 19:30:20", 1, 31, 2012, 19, 30, 20);
		validateDateTime("jeudi après-midi", 2, 2, 2012, 12, 0, 0);
		validateDateTime("douze ans plus tôt a 12:20", 1, 31, 2000, 12, 20, 0);
		validateDateTime("10 heures avant", 1, 31, 2012, 7, 0, 0);
		validateDateTime("demain matin", 2, 1, 2012, 8, 0, 0);
		validateDateTime("après-demain soir", 2, 2, 2012, 19, 0, 0);
		validateDateTime("dans 8 heures", 2, 1, 2012, 1, 0, 0);
		validateDateTime("hier à 20 heures", 1, 30, 2012, 20, 0, 0);
		validateDateTime("avant-hier dans la nuit", 1, 29, 2012, 20, 0, 0);
		
		validateDate("dans vingt-deux jours", 2, 22, 2012);
		validateDate("dans 15 jours", 2, 15, 2012);
		validateDate("dans trois semaines", 2, 21, 2012);
		validateDate("dans 5 ans", 1, 31, 2017);
		
		validateDate("il y a dix huit jours", 1, 13, 2012);
		validateDate("il y a 9 jours", 1, 22, 2012);
		validateDate("il y a huit semaines", 12, 6, 2011);
		validateDate("il y a 2 ans", 1, 31, 2010);
		
		validateDate("ce jeudi", 2, 2, 2012);
		validateDate("le 28 du mois dernier", 12, 28, 2011);
		validateDate("jeudi de la semaine passée", 1, 26, 2012);
		validateDate("mercredi passé", 1, 25, 2012);
		
		// Années totalement incohérentes
//		validateDate("début juin 2013", 6, 1, 2013);
//		validateDate("fin août 1998", 8, 31, 1998);
		
		List<Date> dates;
		DateGroup dateGroup;
		
		dateGroup = _parser.parse("demain et après-demain").get(0);
		Assert.assertFalse(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 2, 1, 2012);
		validateDate(dates.get(1), 2, 2, 2012);
	}
	
	@Test
	public void testSpelledDate() {
		validateDate("le premier janvier 2011", 1, 1, 2011);
		validateDate("deux janvier 2011", 1, 2, 2011);
		validateDate("le trois janvier 2011", 1, 3, 2011);
		validateDate("quatre janvier 2011", 1, 4, 2011);
		validateDate("le cinq janvier 2011", 1, 5, 2011);
		validateDate("six janvier 2011", 1, 6, 2011);
		validateDate("le sept janvier 2011", 1, 7, 2011);
		validateDate("huit janvier 2011", 1, 8, 2011);
		validateDate("le neuf janvier 2011", 1, 9, 2011);
		
		validateDate("dix mars 2011", 3, 10, 2011);
		validateDate("le onze mars 2011", 3, 11, 2011);
		validateDate("douze mars 2011", 3, 12, 2011);
		validateDate("le treize mars 2011", 3, 13, 2011);
		validateDate("quatorze mars 2011", 3, 14, 2011);
		validateDate("le quinze mars 2011", 3, 15, 2011);
		validateDate("seize mars 2011", 3, 16, 2011);
		validateDate("le dix-sept mars 2011", 3, 17, 2011);
		validateDate("dix huit mars 2011", 3, 18, 2011);
		validateDate("le dix-neuf mars 2011", 3, 19, 2011);
		
		validateDate("vingt octobre 2011", 10, 20, 2011);
		validateDate("le vingt et un octobre 2011", 10, 21, 2011);
		validateDate("vingt-deux octobre 2011", 10, 22, 2011);
		validateDate("le vingt-trois octobre 2011", 10, 23, 2011);
		validateDate("vingt quatre octobre 2011", 10, 24, 2011);
		validateDate("le vingt-cinq octobre 2011", 10, 25, 2011);
		validateDate("vingt-six octobre 2011", 10, 26, 2011);
		validateDate("le vingt-sept octobre 2011", 10, 27, 2011);
		validateDate("vingt huit octobre 2011", 10, 28, 2011);
		validateDate("le vingt neuf octobre 2011", 10, 29, 2011);
		
		validateDate("trente décembre 2011", 12, 30, 2011);
		validateDate("le trente-et-un décembre 2011", 12, 31, 2011);
	}
	
	@Test
	public void testIntervals() throws Exception {
		Date reference = DATE_FORMAT.parse("31/01/2012 17:00");
		CalendarSource.setBaseDate(reference);
		
		List<Date> dates;
		DateGroup dateGroup;
		
		// intervalles 'du ... au ...'
		dateGroup = _parser.parse("du 23 janvier au 12 février 2013").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 1, 23, 2012);
		validateDate(dates.get(1), 2, 12, 2013);
		
		dateGroup = _parser.parse("du 10 janvier au 15 juin").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 1, 10, 2012);
		validateDate(dates.get(1), 6, 15, 2012);
		
		dateGroup = _parser.parse("du 11 juin 2010 au 13 août 2013").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 6, 11, 2010);
		validateDate(dates.get(1), 8, 13, 2013);
		
		dateGroup = _parser.parse("du 13 au 15 mai 2015").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 5, 13, 2015);
		validateDate(dates.get(1), 5, 15, 2015);
		
		dateGroup = _parser.parse("du 1 au 6 novembre").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 11, 1, 2012);
		validateDate(dates.get(1), 11, 6, 2012);
		
		// intervalles '... - ...'
		dateGroup = _parser.parse("25 mai - 13 juillet").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 5, 25, 2012);
		validateDate(dates.get(1), 7, 13, 2012);
		
		dateGroup = _parser.parse("31/03/2032 - 1/05/2033").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 3, 31, 2032);
		validateDate(dates.get(1), 5, 1, 2033);
		
		dateGroup = _parser.parse("aujourd'hui - dans 3 semaines").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 1, 31, 2012);
		validateDate(dates.get(1), 2, 21, 2012);
		
		// préfix 'pendant'
		dateGroup = _parser.parse("pendant trois semaines").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDate(dates.get(0), 1, 31, 2012);
		validateDate(dates.get(1), 2, 21, 2012);
		
		dateGroup = _parser.parse("pendant 2 heures").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 31, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 1, 31, 2012, 19, 0, 0);
		
		// préfix 'ce', 'cet', 'cette'
		dateGroup = _parser.parse("cette semaine").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 30, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 2, 5, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("ce mois").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 1, 31, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("cette année").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 12, 31, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("ce weekend").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 4, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 2, 5, 2012, 17, 0, 0);
		
		// suffix 'suivant', 'prochain'
		dateGroup = _parser.parse("la semaine suivante").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 6, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 2, 12, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("le mois prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 1, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 2, 29, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("l'année prochaine").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2013, 17, 0, 0);
		validateDateTime(dates.get(1), 12, 31, 2013, 17, 0, 0);
		
		dateGroup = _parser.parse("le week-end prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 11, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 2, 12, 2012, 17, 0, 0);
		
		// suffix 'passé'
		dateGroup = _parser.parse("la semaine passée").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 23, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 1, 29, 2012, 17, 0, 0);
		
		dateGroup = _parser.parse("l'année passée").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2011, 17, 0, 0);
		validateDateTime(dates.get(1), 12, 31, 2011, 17, 0, 0);
		
		dateGroup = _parser.parse("mois passé").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 12, 1, 2011, 17, 0, 0);
		validateDateTime(dates.get(1), 12, 31, 2011, 17, 0, 0);
		
		dateGroup = _parser.parse("week-end passé").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 28, 2012, 17, 0, 0);
		validateDateTime(dates.get(1), 1, 29, 2012, 17, 0, 0);
	}
	
	@Test
	public void testRelativeMonthIntervals() throws Exception {
		Date reference = DATE_FORMAT.parse("24/05/2012 12:00");
		CalendarSource.setBaseDate(reference);
		
		List<Date> dates;
		DateGroup dateGroup;
		
		// pour un mois situé avant celui en cours, on passe à l'année suivante
		dateGroup = _parser.parse("janvier prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2013, 12, 0, 0);
		validateDateTime(dates.get(1), 1, 31, 2013, 12, 0, 0);
		
		dateGroup = _parser.parse("février prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 1, 2013, 12, 0, 0);
		validateDateTime(dates.get(1), 2, 28, 2013, 12, 0, 0);
		
		dateGroup = _parser.parse("avril prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 4, 1, 2013, 12, 0, 0);
		validateDateTime(dates.get(1), 4, 30, 2013, 12, 0, 0);
		
		// pour le même mois que celui en cours, on passe à l'année suivante
		dateGroup = _parser.parse("mai prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 5, 1, 2013, 12, 0, 0);
		validateDateTime(dates.get(1), 5, 31, 2013, 12, 0, 0);
		
		// pour un mois situé après celui en cours, on reste dans l'année en cours
		dateGroup = _parser.parse("juin prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 6, 1, 2012, 12, 0, 0);
		validateDateTime(dates.get(1), 6, 30, 2012, 12, 0, 0);
		
		dateGroup = _parser.parse("décembre prochain").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 12, 1, 2012, 12, 0, 0);
		validateDateTime(dates.get(1), 12, 31, 2012, 12, 0, 0);
	}
	
	@Test
	public void testExplicitMonthIntervals() throws Exception {
		Date reference = DATE_FORMAT.parse("08/02/2012 12:00");
		CalendarSource.setBaseDate(reference);
		
		List<Date> dates;
		DateGroup dateGroup;
		
		dateGroup = _parser.parse("juin 1998").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 6, 1, 1998, 12, 0, 0);
		validateDateTime(dates.get(1), 6, 30, 1998, 12, 0, 0);
		
		dateGroup = _parser.parse("janvier 2011").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 1, 1, 2011, 12, 0, 0);
		validateDateTime(dates.get(1), 1, 31, 2011, 12, 0, 0);
		
		dateGroup = _parser.parse("février 2013").get(0);
		Assert.assertTrue(dateGroup.isInterval());
		dates = dateGroup.getDates();
		Assert.assertEquals(2, dates.size());
		validateDateTime(dates.get(0), 2, 1, 2013, 12, 0, 0);
		validateDateTime(dates.get(1), 2, 28, 2013, 12, 0, 0);
	}
}

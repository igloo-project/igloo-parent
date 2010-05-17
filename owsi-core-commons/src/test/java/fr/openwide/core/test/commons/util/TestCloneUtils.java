package fr.openwide.core.test.commons.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import fr.openwide.core.commons.util.CloneUtils;

public class TestCloneUtils {

	@Test
	public void testCloneDate() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2010, 5, 24, 17, 0, 15);

		Date date1 = calendar.getTime();

		Date date2 = CloneUtils.clone(date1);

		assertNotSame(date1, date2);
		assertEquals(date1.getTime(), date2.getTime());

		Date date3 = null;

		Date date4 = CloneUtils.clone(date3);

		assertNull(date4);
	}

	@Test
	public void testCloneArray() {
		String[] strArray1 = { "Chaine1", "Chaine2", "Chaine3" };

		String[] strArray2 = CloneUtils.clone(strArray1);

		assertNotSame(strArray1, strArray2);
		assertArrayEquals(strArray1, strArray2);

		String[] strArray3 = null;

		String[] strArray4 = CloneUtils.clone(strArray3);

		assertNull(strArray4);
	}
}

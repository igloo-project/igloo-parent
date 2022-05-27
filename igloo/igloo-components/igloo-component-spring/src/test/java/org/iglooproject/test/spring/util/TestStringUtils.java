package org.iglooproject.test.spring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.iglooproject.spring.util.StringUtils;
import org.junit.jupiter.api.Test;

class TestStringUtils {

	@Test
	void testSplitAsList() {
		String str1 = "test1-test2-test3";
		List<String> list1 = StringUtils.splitAsList(str1, "-");
		assertEquals("test1", list1.get(0));
		assertEquals("test2", list1.get(1));
		assertEquals("test3", list1.get(2));

		String str2 = "test1,=+-test2";
		List<String> list2 = StringUtils.splitAsList(str2, "+");
		assertEquals("test1,=", list2.get(0));
		assertEquals("-test2", list2.get(1));

		String str3 = "";
		List<String> list3 = StringUtils.splitAsList(str3, "");
		assertTrue(list3.isEmpty());

		String str4 = null;
		List<String> list4 = StringUtils.splitAsList(str4, "");
		assertTrue(list4.isEmpty());

		String str5 = "test test test";
		List<String> list5 = StringUtils.splitAsList(str5, null);
		assertEquals(1, list5.size());
		assertEquals(str5, list5.get(0));
	}

	@Test
	void testUrlize() {
		String cleanStr;

		String str_maj = "ABCDEFG";
		cleanStr = StringUtils.urlize(str_maj);
		assertEquals("abcdefg", cleanStr);

		String str_accent = "éèàçù";
		cleanStr = StringUtils.urlize(str_accent);
		assertEquals("eeacu", cleanStr);

		String str_dashes = "---------";
		cleanStr = StringUtils.urlize(str_dashes);
		assertEquals("", cleanStr);

		String str_spaces = "         ";
		cleanStr = StringUtils.urlize(str_spaces);
		assertEquals("", cleanStr);

		String str_regEx = "[’'`«»\n\r\t\":;,\\.!¡\\?¿&|°_%\\\\©®€²³\\+\\*÷×/%<>()^\\[\\]…–]";
		cleanStr = StringUtils.urlize(str_regEx);
		assertEquals("23", cleanStr);

		String str_trimDash = " --test-test--";
		cleanStr = StringUtils.urlize(str_trimDash);
		assertEquals("test-test", cleanStr);

		String str_null = null;
		cleanStr = StringUtils.urlize(str_null);
		assertNull(cleanStr);
	}
	
	@Test
	void testTagify() {
		String cleanStr;

		String str_maj = "ABCDEFG";
		cleanStr = StringUtils.tagify(str_maj);
		assertEquals("abcdefg", cleanStr);

		String str_accent = "éèàçù";
		cleanStr = StringUtils.tagify(str_accent);
		assertEquals("eeacu", cleanStr);

		String str_dashes = "---------";
		cleanStr = StringUtils.tagify(str_dashes);
		assertEquals("", cleanStr);

		String str_spaces = "         ";
		cleanStr = StringUtils.tagify(str_spaces);
		assertEquals("", cleanStr);

		String str_regEx = "[’'`«»\n\r\t\":;,\\.!¡\\?¿&|°_%\\\\©®€²³\\+\\*÷×/%<>()^\\[\\]…–]";
		cleanStr = StringUtils.tagify(str_regEx);
		assertEquals(".-23", cleanStr);

		String str_trimDash = " --test-test--";
		cleanStr = StringUtils.tagify(str_trimDash);
		assertEquals("test-test", cleanStr);

		String str_null = null;
		cleanStr = StringUtils.tagify(str_null);
		assertNull(cleanStr);
		
		String str_versionTag = "v 2.3.4";
		cleanStr = StringUtils.tagify(str_versionTag);
		assertEquals("v-2.3.4", cleanStr);
	}
	
	@Test
	void testClean() {
		String cleanStr;

		String str_maj = "ABCDEFG";
		cleanStr = StringUtils.clean(str_maj);
		assertEquals("abcdefg", cleanStr);

		String str_accent = "éèàçù";
		cleanStr = StringUtils.clean(str_accent);
		assertEquals("eeacu", cleanStr);

		String str_dashes = "---------";
		cleanStr = StringUtils.clean(str_dashes);
		assertEquals("", cleanStr);

		String str_spaces = "         ";
		cleanStr = StringUtils.clean(str_spaces);
		assertEquals("", cleanStr);
		
		String str_words = " test word   wo-rd";
		cleanStr = StringUtils.clean(str_words);
		assertEquals("test word wo-rd", cleanStr);

		String str_regEx = "[’'`«»\n\r\t\":;,\\.!¡\\?¿&|°_%\\\\©®€²³\\+\\*÷×/%<>()^\\[\\]…–]";
		cleanStr = StringUtils.clean(str_regEx);
		assertEquals("23", cleanStr);

		String str_trimDash = " --test-test--";
		cleanStr = StringUtils.clean(str_trimDash);
		assertEquals("test-test", cleanStr);

		String str_null = null;
		cleanStr = StringUtils.clean(str_null);
		assertNull(cleanStr);
	}
	
	@Test
	void testCleanQuery() {
		String cleanStr;

		String str_maj = "ABCDEFG";
		cleanStr = StringUtils.cleanForQuery(str_maj);
		assertEquals("abcdefg", cleanStr);

		String str_accent = "éèàçù";
		cleanStr = StringUtils.cleanForQuery(str_accent);
		assertEquals("eeacu", cleanStr);

		String str_dashes = "---------";
		cleanStr = StringUtils.cleanForQuery(str_dashes);
		assertEquals("", cleanStr);

		String str_spaces = "         ";
		cleanStr = StringUtils.cleanForQuery(str_spaces);
		assertEquals("", cleanStr);
		
		String str_words = " test word   wo-rd";
		cleanStr = StringUtils.cleanForQuery(str_words);
		assertEquals("test word wo-rd", cleanStr);

		String str_regEx = "[’'`«»\n\r\t\":;,\\.!¡\\?¿&|°_%\\\\©®€²³\\+\\*÷×/%<>()^\\[\\]…–]";
		cleanStr = StringUtils.cleanForQuery(str_regEx);
		assertEquals("23 *", cleanStr);

		String str_trimDash = " --test-test--";
		cleanStr = StringUtils.cleanForQuery(str_trimDash);
		assertEquals("test-test", cleanStr);

		String str_null = null;
		cleanStr = StringUtils.cleanForQuery(str_null);
		assertNull(cleanStr);
	}

	@Test
	void testRemoveAccents() {
		String cleanStr;

		String str1 = "À Á Â Ã Ä Å";
		cleanStr = StringUtils.removeAccents(str1);
		assertEquals("A A A A A A", cleanStr);

		String str2 = "tëßt  TÊST";
		cleanStr = StringUtils.removeAccents(str2);
		assertEquals("tesst  TEST", cleanStr);

		String str3 = "";
		cleanStr = StringUtils.removeAccents(str3);
		assertEquals("", cleanStr);
	}
	
	@Test
	void testCompare() {
		String str1 = "testtest";
		String str2 = "test test";
		String str3 = "test test";
		
		assertTrue(StringUtils.compare(str1, str2) > 0);
		assertTrue(StringUtils.compare(str2, str1) < 0);
		assertTrue(StringUtils.compare(str2, str3) == 0);
		assertTrue(StringUtils.compare(str3, "") > 0);
		assertTrue(StringUtils.compare(str3, "") > 0);
		assertTrue(StringUtils.compare("", "") == 0);
		assertTrue(StringUtils.compare("", null) > 0);
		assertTrue(StringUtils.compare(null, "") < 0 );
		assertTrue(StringUtils.compare(null, null) == 0);
	}
	
	@Test
	void testNormalizeNewLines() {
		String cleanStr;
		
		String str1 = "Ceci \r\n est un test d'uniformisation \r des retours \n à la ligne.";
		cleanStr = StringUtils.normalizeNewLines(str1);
		assertEquals("Ceci \n est un test d'uniformisation \n des retours \n à la ligne.", cleanStr);
		
		String str2 = "\r\n \n\r \n\n \r\n\r \r\r\r\n";
		cleanStr = StringUtils.normalizeNewLines(str2);
		assertEquals("\n \n\n \n\n \n\n \n\n\n", cleanStr);
	}
}

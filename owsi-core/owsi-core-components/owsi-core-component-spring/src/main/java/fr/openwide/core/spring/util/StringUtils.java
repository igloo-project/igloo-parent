/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Large portions Copyright Apache Software Foundation
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

package fr.openwide.core.spring.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>Utilitaire permettant de manipuler les chaînes de caractères.</p>
 * 
 * <p>Il permet de wrapper des méthodes des StringUtils de Spring et des
 * StringUtils de commons-lang et ajoute par ailleurs quelques méthodes
 * complémentaires dont le removeAccents qui provient de Lucene.</p>
 * 
 * @author Open Wide
 */
public final class StringUtils extends org.springframework.util.StringUtils {

	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	public static final String CHARSET_UTF_8 = "UTF-8";
	
	public static final char NON_BREAKABLE_SPACE_CHAR = (char) 160;
	public static final char SPACE_CHAR = ' ';
	public static final String SPACE = " ";
	public static final char DASH_CHAR = '-';
	public static final String DASH = "-";
	public static final String CLEAN_SPECIAL_CHARS_REGEXP;
	public static final String CLEAN_DUPLICATE_DASHES_REGEXP = "--*";
	public static final String CLEAN_DUPLICATE_SPACES_REGEXP = "  *";
	public static final String COLLECTION_DEFAULT_DELIMITER = ", ";
	
	static {
		StringBuilder regExp = new StringBuilder();
		regExp.append('[');
		regExp.append("’'`«»");
		regExp.append("\n\r\t");
		regExp.append('\"');
		regExp.append(":;,\\.");
		regExp.append("!¡\\?¿&|");
		regExp.append("°_%");
		regExp.append("\\\\");
		regExp.append("©®€²³");
		regExp.append("\\+\\*÷×/%");
		regExp.append("<>()^\\[\\]");
		regExp.append('…');
		regExp.append('–');
		regExp.append(']');
		
		CLEAN_SPECIAL_CHARS_REGEXP = regExp.toString();
	}

	/**
	 * @see org.apache.commons.lang.StringUtils#replaceEach(String, String[],String[])
	 */
	public static String replaceEach(String text, String[] searchList, String[] replacementList) {
		return org.apache.commons.lang.StringUtils.replaceEach(text, searchList, replacementList);
	}

	/**
	 * @see org.apache.commons.lang.StringUtils#join(Object[], String)
	 */
	public static String join(String[] array, String separator) {
		return org.apache.commons.lang.StringUtils.join(array, separator);
	}

	/**
	 * @see org.apache.commons.lang.StringUtils#join(java.util.Collection,String)
	 */
	public static String join(Collection<String> collection, String separator) {
			return org.apache.commons.lang.StringUtils.join(collection, separator);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#split(String, String)
	 */
	public static List<String> splitAsList(String str, String separatorChar) {
		return new ArrayList<String>(Arrays.asList(delimitedListToStringArray(str, separatorChar)));
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#capitalize(String)
	 */
	public static String capitalize(String str) {
		return org.apache.commons.lang.StringUtils.capitalize(str);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#lowerCase(String)
	 */
	public static String lowerCase(String str) {
		return org.apache.commons.lang.StringUtils.lowerCase(str);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#upperCase(String)
	 */
	public static String upperCase(String str) {
		return org.apache.commons.lang.StringUtils.upperCase(str);
	}

	/**
	 * @see org.apache.commons.lang.StringUtils#contains(String, String)
	 */
	public static boolean contains(String str, String searchStr) {
		return org.apache.commons.lang.StringUtils.contains(str, searchStr);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#substring(String, int, int)
	 */
	public static String substring(String str, int start, int end) {
		return org.apache.commons.lang.StringUtils.substring(str, start, end);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#isNumeric(String)
	 */
	public static boolean isNumeric(String str) {
		return org.apache.commons.lang.StringUtils.isNumeric(str);
	}
	
	/**
	 * @see org.apache.commons.lang.StringUtils#rightPad(String, int, char)
	 */
	public static String rightPad(String str, int size, char padChar) {
		return org.apache.commons.lang.StringUtils.rightPad(str, size, padChar);
	}
	
	/**
	 * Supprime les caractères spéciaux d'une chaîne de caractères et retourne une chaîne utilisable dans une URL.
	 * 
	 * @param strToClean chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String urlize(String strToClean) {
		if (strToClean == null) {
			return null;
		}
		
		String str = strToClean.trim();
		str = str.replace(NON_BREAKABLE_SPACE_CHAR, SPACE_CHAR);
		str = StringUtils.lowerCase(str);
		str = StringUtils.removeAccents(str);
		
		str = str.replaceAll(CLEAN_SPECIAL_CHARS_REGEXP, DASH);
		str = str.replace(SPACE_CHAR, DASH_CHAR);
		str = str.replaceAll(CLEAN_DUPLICATE_DASHES_REGEXP, DASH);
		
		str = StringUtils.trimLeadingCharacter(str, DASH_CHAR);
		str = StringUtils.trimTrailingCharacter(str, DASH_CHAR);
		
		return str;
	}
	
	/**
	 * Nettoie une chaîne de caractères
	 * 
	 * @param strToClean chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String clean(String strToClean) {
		if (strToClean == null) {
			return null;
		}
		
		String str = strToClean.trim();
		str = str.replace(NON_BREAKABLE_SPACE_CHAR, SPACE_CHAR);
		str = StringUtils.lowerCase(str);
		str = StringUtils.removeAccents(str);
		
		str = str.replaceAll(CLEAN_SPECIAL_CHARS_REGEXP, SPACE);
		str = str.replaceAll(CLEAN_DUPLICATE_SPACES_REGEXP, SPACE);
		
		str = StringUtils.trimLeadingCharacter(str, SPACE_CHAR);
		str = StringUtils.trimTrailingCharacter(str, SPACE_CHAR);
		
		str = StringUtils.trimLeadingCharacter(str, DASH_CHAR);
		str = StringUtils.trimTrailingCharacter(str, DASH_CHAR);
		
		return str;
	}
	
	/**
	 * Supprime les accents d'une chaîne de caractères.
	 * 
	 * @param text chaîne à nettoyer
	 * @return chaîne sans accent
	 * @see org.apache.lucene.analysis.ISOLatin1AccentFilter(char[], int)
	 */
	public static String removeAccents(String text) {
		int length = text.length();
		char[] input = text.toCharArray();
		char[] output = new char[256];

		// Worst-case length required:
		final int maxSizeNeeded = 2 * length;

		int size = output.length;
		while (size < maxSizeNeeded) {
			size *= 2;
		}

		if (size != output.length) {
			output = new char[size];
		}

		int outputPos = 0;
		int pos = 0;

		for (int i = 0; i < length; i++, pos++) {
			final char c = input[pos];

			// Quick test: if it's not in range then just keep
			// current character
			if (c < '\u00c0' || c > '\uFB06') {
				output[outputPos++] = c;
			} else {
				switch (c) {
				case '\u00C0': // À
				case '\u00C1': // Á
				case '\u00C2': // Â
				case '\u00C3': // Ã
				case '\u00C4': // Ä
				case '\u00C5': // Å
					output[outputPos++] = 'A';
					break;
				case '\u00C6': // Æ
					output[outputPos++] = 'A';
					output[outputPos++] = 'E';
					break;
				case '\u00C7': // Ç
					output[outputPos++] = 'C';
					break;
				case '\u00C8': // È
				case '\u00C9': // É
				case '\u00CA': // Ê
				case '\u00CB': // Ë
					output[outputPos++] = 'E';
					break;
				case '\u00CC': // Ì
				case '\u00CD': // Í
				case '\u00CE': // Î
				case '\u00CF': // Ï
					output[outputPos++] = 'I';
					break;
				case '\u0132': // Ĳ
					output[outputPos++] = 'I';
					output[outputPos++] = 'J';
					break;
				case '\u00D0': // Ð
					output[outputPos++] = 'D';
					break;
				case '\u00D1': // Ñ
					output[outputPos++] = 'N';
					break;
				case '\u00D2': // Ò
				case '\u00D3': // Ó
				case '\u00D4': // Ô
				case '\u00D5': // Õ
				case '\u00D6': // Ö
				case '\u00D8': // Ø
					output[outputPos++] = 'O';
					break;
				case '\u0152': // Œ
					output[outputPos++] = 'O';
					output[outputPos++] = 'E';
					break;
				case '\u00DE': // Þ
					output[outputPos++] = 'T';
					output[outputPos++] = 'H';
					break;
				case '\u00D9': // Ù
				case '\u00DA': // Ú
				case '\u00DB': // Û
				case '\u00DC': // Ü
					output[outputPos++] = 'U';
					break;
				case '\u00DD': // Ý
				case '\u0178': // Ÿ
					output[outputPos++] = 'Y';
					break;
				case '\u00E0': // à
				case '\u00E1': // á
				case '\u00E2': // â
				case '\u00E3': // ã
				case '\u00E4': // ä
				case '\u00E5': // å
					output[outputPos++] = 'a';
					break;
				case '\u00E6': // æ
					output[outputPos++] = 'a';
					output[outputPos++] = 'e';
					break;
				case '\u00E7': // ç
					output[outputPos++] = 'c';
					break;
				case '\u00E8': // è
				case '\u00E9': // é
				case '\u00EA': // ê
				case '\u00EB': // ë
					output[outputPos++] = 'e';
					break;
				case '\u00EC': // ì
				case '\u00ED': // í
				case '\u00EE': // î
				case '\u00EF': // ï
					output[outputPos++] = 'i';
					break;
				case '\u0133': // ĳ
					output[outputPos++] = 'i';
					output[outputPos++] = 'j';
					break;
				case '\u00F0': // ð
					output[outputPos++] = 'd';
					break;
				case '\u00F1': // ñ
					output[outputPos++] = 'n';
					break;
				case '\u00F2': // ò
				case '\u00F3': // ó
				case '\u00F4': // ô
				case '\u00F5': // õ
				case '\u00F6': // ö
				case '\u00F8': // ø
					output[outputPos++] = 'o';
					break;
				case '\u0153': // œ
					output[outputPos++] = 'o';
					output[outputPos++] = 'e';
					break;
				case '\u00DF': // ß
					output[outputPos++] = 's';
					output[outputPos++] = 's';
					break;
				case '\u00FE': // þ
					output[outputPos++] = 't';
					output[outputPos++] = 'h';
					break;
				case '\u00F9': // ù
				case '\u00FA': // ú
				case '\u00FB': // û
				case '\u00FC': // ü
					output[outputPos++] = 'u';
					break;
				case '\u00FD': // ý
				case '\u00FF': // ÿ
					output[outputPos++] = 'y';
					break;
				case '\uFB00': // ﬀ
					output[outputPos++] = 'f';
					output[outputPos++] = 'f';
					break;
				case '\uFB01': // ﬁ
					output[outputPos++] = 'f';
					output[outputPos++] = 'i';
					break;
				case '\uFB02': // ﬂ
					output[outputPos++] = 'f';
					output[outputPos++] = 'l';
					break;
				// following 2 are commented as they can break the maxSizeNeeded
				// (and doing *3 could be expensive)
				// case '\uFB03': // ﬃ
				// output[outputPos++] = 'f';
				// output[outputPos++] = 'f';
				// output[outputPos++] = 'i';
				// break;
				// case '\uFB04': // ﬄ
				// output[outputPos++] = 'f';
				// output[outputPos++] = 'f';
				// output[outputPos++] = 'l';
				// break;
				case '\uFB05': // ﬅ
					output[outputPos++] = 'f';
					output[outputPos++] = 't';
					break;
				case '\uFB06': // ﬆ
					output[outputPos++] = 's';
					output[outputPos++] = 't';
					break;
				default:
					output[outputPos++] = c;
					break;
				}
			}
		}
		return new String(output, 0, outputPos);
	}
	
	/**
	 * Compare deux chaînes potentiellement nulles.
	 * @param string1 la première chaîne
	 * @param string2 la seconde chaîne
	 * @return résultat de la comparaison
	 */
	public static int compare(String string1, String string2) {
		if (string1 == null) {
			if (string2 == null) {
				return 0;
			} else {
				return -1;
			}
		}
		if (string2 == null) {
			return 1;
		}
		return string1.compareTo(string2);
	}
	
	private StringUtils() {
	}
}

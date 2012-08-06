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

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;

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
	
	public static final String NEW_LINE_ANTISLASH_N = "\n";
	public static final String NEW_LINE_ANTISLASH_R = "\r";
	public static final String NEW_LINE_ANTISLASH_R_ANTISLASH_N = NEW_LINE_ANTISLASH_R + NEW_LINE_ANTISLASH_N;
	
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
	 * @see org.apache.commons.lang3.StringUtils#replaceEach(String, String[],String[])
	 */
	public static String replaceEach(String text, String[] searchList, String[] replacementList) {
		return org.apache.commons.lang3.StringUtils.replaceEach(text, searchList, replacementList);
	}

	/**
	 * @see org.apache.commons.lang3.StringUtils#join(Object[], String)
	 */
	public static String join(String[] array, String separator) {
		return org.apache.commons.lang3.StringUtils.join(array, separator);
	}

	/**
	 * @see org.apache.commons.lang3.StringUtils#join(java.util.Collection,String)
	 */
	public static String join(Collection<String> collection, String separator) {
			return org.apache.commons.lang3.StringUtils.join(collection, separator);
	}
	
	public static List<String> splitAsList(String str, String separatorChar) {
		return new ArrayList<String>(Arrays.asList(delimitedListToStringArray(str, separatorChar)));
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#capitalize(String)
	 */
	public static String capitalize(String str) {
		return org.apache.commons.lang3.StringUtils.capitalize(str);
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#lowerCase(String)
	 */
	public static String lowerCase(String str) {
		return org.apache.commons.lang3.StringUtils.lowerCase(str);
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#upperCase(String)
	 */
	public static String upperCase(String str) {
		return org.apache.commons.lang3.StringUtils.upperCase(str);
	}

	/**
	 * @see org.apache.commons.lang3.StringUtils#contains(String, String)
	 */
	public static boolean contains(String str, String searchStr) {
		return org.apache.commons.lang3.StringUtils.contains(str, searchStr);
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#substring(String, int, int)
	 */
	public static String substring(String str, int start, int end) {
		return org.apache.commons.lang3.StringUtils.substring(str, start, end);
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#isNumeric(String)
	 */
	public static boolean isNumeric(String str) {
		return org.apache.commons.lang3.StringUtils.isNumeric(str);
	}
	
	/**
	 * @see org.apache.commons.lang3.StringUtils#rightPad(String, int, char)
	 */
	public static String rightPad(String str, int size, char padChar) {
		return org.apache.commons.lang3.StringUtils.rightPad(str, size, padChar);
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
	 * @see org.apache.lucene.analysis.ASCIIFoldingFilter
	 */
	public static String removeAccents(String text) {
		if (text == null) {
			return text;
		}
		
		int length = text.length();
		char[] input = text.toCharArray();
		char[] output = new char[256];

		// Worst-case length required:
		final int maxSizeNeeded = 4 * length;
		
		if (output.length < maxSizeNeeded) {
			output = new char[ArrayUtil.oversize(maxSizeNeeded, RamUsageEstimator.NUM_BYTES_CHAR)];
		}
		
		int outputPos = ASCIIFoldingFilter.foldToASCII(input, 0, output, 0, length);

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
	
	/**
	 * Uniformise les retours à la ligne en transformant les <code>\r\n</code> et les <code>\r</code> en <code>\n</code>.
	 * 
	 * @param string chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String cleanNewLines(String string) {
		if (string == null) {
			return null;
		}
		
		String cleanString = string.replaceAll(NEW_LINE_ANTISLASH_R_ANTISLASH_N, NEW_LINE_ANTISLASH_N);
		cleanString = cleanString.replaceAll(NEW_LINE_ANTISLASH_R, NEW_LINE_ANTISLASH_N);
		
		return cleanString;
	}
	
	private StringUtils() {
	}
}

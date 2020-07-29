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

package org.iglooproject.spring.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;

import com.google.common.base.CharMatcher;

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
	
	public static final char SPACE_CHAR = ' ';
	public static final String SPACE = " ";
	public static final char DASH_CHAR = '-';
	public static final String DASH = "-";
	public static final String ASTERISK = "*";
	public static final char ASTERISK_CHAR = '*';
	
	public static final Pattern CLEAN_SPECIAL_CHARS_REGEXP_PATTERN = Pattern.compile("[^a-z0-9-]");
	public static final Pattern CLEAN_SPECIAL_CHARS_QUERY_REGEXP_PATTERN = Pattern.compile("[^a-z0-9-*]");
	public static final Pattern CLEAN_DUPLICATE_DASHES_REGEXP_PATTERN = Pattern.compile("--*");
	public static final Pattern CLEAN_DUPLICATE_SPACES_REGEXP_PATTERN = Pattern.compile("  *");
	public static final Pattern CLEAN_DUPLICATE_ASTERISKS_REGEXP_PATTERN = Pattern.compile("\\*\\**");
	public static final Pattern TAGIFY_REGEXP_PATTERN = Pattern.compile("[^a-z0-9.]");
	public static final String COLLECTION_DEFAULT_DELIMITER = ", ";
	
	public static final String NEW_LINE_ANTISLASH_N = "\n";
	public static final String NEW_LINE_ANTISLASH_R = "\r";
	public static final String NEW_LINE_ANTISLASH_R_ANTISLASH_N = NEW_LINE_ANTISLASH_R + NEW_LINE_ANTISLASH_N;
	
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
		return new ArrayList<>(Arrays.asList(delimitedListToStringArray(str, separatorChar)));
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
	 * @see org.apache.commons.lang3.StringUtils#leftPad(String, int, char)
	 */
	public static String leftPad(String str, int size, char padChar) {
		return org.apache.commons.lang3.StringUtils.leftPad(str, size, padChar);
	}

	/**
	 * Supprime les caractères spéciaux d'une chaîne de caractères et retourne une chaîne utilisable dans une URL.
	 * 
	 * @param strToClean chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String urlize(String strToClean) {
		return sanitizeString(strToClean, CLEAN_SPECIAL_CHARS_REGEXP_PATTERN);
	}
	
	/**
	 * Supprime les caractères spéciaux d'une chaîne de caractères et retourne une chaîne utilisable pour un tag.
	 * 
	 * La grande différence par rapport à une URL est qu'on autorise les ".", notamment pour pouvoir définir des numéros de version.
	 * 
	 * @param strToClean chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String tagify(String strToClean) {
		return sanitizeString(strToClean, TAGIFY_REGEXP_PATTERN);
	}
	
	private static String sanitizeString(String strToClean, Pattern cleanRegexpPattern) {
		if (strToClean == null) {
			return null;
		}
		
		String str = strToClean.trim();
		str = StringUtils.removeAccents(str);
		str = StringUtils.lowerCase(str);
		
		str = cleanRegexpPattern.matcher(str).replaceAll(DASH);
		str = CLEAN_DUPLICATE_DASHES_REGEXP_PATTERN.matcher(str).replaceAll(DASH);
		
		str = org.apache.commons.lang3.StringUtils.strip(str, DASH);
		
		return str;
	}
	
	/**
	 * Nettoie une chaîne de caractères en conservant les espaces
	 * 
	 * @param strToClean chaîne à nettoyer
	 * @return chaîne nettoyée
	 */
	public static String clean(String strToClean) {
		return clean(strToClean, CLEAN_SPECIAL_CHARS_REGEXP_PATTERN);
	}
	
	/**
	 * Nettoie une chaîne de caractères qui part à destination d'une recherche Lucene. On souhaite autoriser le fait
	 * qu'il puisse y avoir un wildcard.
	 */
	public static String cleanForQuery(String strToClean) {
		return clean(strToClean, CLEAN_SPECIAL_CHARS_QUERY_REGEXP_PATTERN);
	}
	
	private static String clean(String strToClean, Pattern forbiddenCharactersPattern) {
		if (strToClean == null) {
			return null;
		}
		
		String str = strToClean.trim();
		str = StringUtils.removeAccents(str);
		str = StringUtils.lowerCase(str);
		
		str = forbiddenCharactersPattern.matcher(str).replaceAll(SPACE);
		str = CLEAN_DUPLICATE_SPACES_REGEXP_PATTERN.matcher(str).replaceAll(SPACE);
		str = CLEAN_DUPLICATE_ASTERISKS_REGEXP_PATTERN.matcher(str).replaceAll(ASTERISK);
		
		str = org.apache.commons.lang3.StringUtils.strip(str, SPACE + DASH);
		
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
	public static String normalizeNewLines(String string) {
		if (string == null) {
			return null;
		}
		
		String cleanString = string.replace(NEW_LINE_ANTISLASH_R_ANTISLASH_N, NEW_LINE_ANTISLASH_N);
		cleanString = cleanString.replace(NEW_LINE_ANTISLASH_R, NEW_LINE_ANTISLASH_N);
		
		return cleanString;
	}
	
	private StringUtils() {
	}
	
	/**
	 * Convertit les bytes en données interprétables
	 * 
	 * source : http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880
	 * 
	 * @param bytes valeur en bytes
	 * @param si détermine le standard à utiliser (SI ou binary)
	 * @return équivalent en unités SI ou binary
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String emptyTextToNull(String str) {
		return hasText(str) ? str : null;
	}

	public static String capitalizeFully(String str, CharMatcher charMatcher) {
		if (str == null) {
			return null;
		}
		
		str = lowerCase(str);
		return capitalize(str, charMatcher);
	}

	public static String capitalize(String str, CharMatcher charMatcher) {
		if (str == null) {
			return null;
		}
		
		final char[] buffer = str.toCharArray();
		boolean capitalizeNext = true;
		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (charMatcher.matches(ch)) {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				buffer[i] = Character.toTitleCase(ch);
				capitalizeNext = false;
			}
		}
		return new String(buffer);
	}

}

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

package fr.openwide.core.commons;

import java.util.Date;

import org.apache.commons.lang.ArrayUtils;

/**
 * <p>Utilitaires utilisés pour cloner les objets Java.</p>
 * 
 * @author Open Wide
 */
public final class CloneUtils {

	/**
	 * Clône une date.
	 * Cette méthode est null safe.
	 * 
	 * @param date à clôner
	 * @return clône de la date passée en paramètre
	 */
	public static Date clone(Date date) {
		if (date == null) {
			return null;
		} else {
			return (Date) date.clone();
		}
	}

	/**
	 * Clône un tableau d'objets
	 * 
	 * @param <T> classe des objets stockés dans le tableau
	 * @param array tableau
	 * @return clône du tableau
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T[] clone(T[] array) {
		return (T[]) ArrayUtils.clone(array);
	}
	
	/**
	 * Constructeur privé utilisé pour empêcher une instanciation accidentelle de l'objet.
	 */
	private CloneUtils() {
	}

}

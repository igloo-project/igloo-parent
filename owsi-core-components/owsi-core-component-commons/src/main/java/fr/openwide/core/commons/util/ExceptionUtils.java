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

package fr.openwide.core.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>Utilitaire concernant les Exceptions.</p>
 * 
 * @author Open Wide
 */
public final class ExceptionUtils {
	
	/**
	 * Construit une String à partir de la StackTrace d'une Exception.
	 * @param e l'exception concernée
	 * @return la StackTrace
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e != null) {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			return result.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Constructeur privé utilisé pour empêcher une instanciation accidentelle de l'objet.
	 */
	private ExceptionUtils() {
	}

}

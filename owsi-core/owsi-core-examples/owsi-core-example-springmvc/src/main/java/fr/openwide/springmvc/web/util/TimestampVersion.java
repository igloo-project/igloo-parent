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

import java.util.Calendar;

import org.springframework.beans.factory.InitializingBean;

/**
 * Indique quelle version des fichiers statiques utiliser
 * 
 * @author Open Wide
 */
public class TimestampVersion implements InitializingBean {

	private String timestampVersion;

	private String defaultTimestampVersion;
	
	/**
	 * Non utilisé ; on peut passer la valeur à true pour forcer la génération
	 * d'un nouveau timestampVersion à chaque appel.
	 */
	private boolean alwaysUpdateTimestampVersion = false;

	public TimestampVersion() {
		super();
	}

	@Override
	public void afterPropertiesSet() {
		initTimestampVersion();
	}

	private void initTimestampVersion() {
		if (defaultTimestampVersion == null) {
			timestampVersion = DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
		} else {
			defaultTimestampVersion = timestampVersion;
		}
	}

	@Override
	public String toString() {
		if (alwaysUpdateTimestampVersion) {
			initTimestampVersion();
		}
		return timestampVersion;
	}

}

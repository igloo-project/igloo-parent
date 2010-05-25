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
package fr.openwide.springmvc.web.business.common.editor; 

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

import fr.openwide.springmvc.web.util.DateFormatUtils;

	/**
	 * <p>Permet d'éditer une date.</p>
	 * 
	 * @author Open Wide
	 */
	public class DateEditor extends PropertyEditorSupport {
	
	private String format;
	
	/**
	 * Valeur textuelle entrée par l'utilisateur.
	 */
	private String textValue = "";
	
	public DateEditor(String format) {
		this.format = format;
	}
	
	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		if (date == null) {
			// un mauvais format a été entré : on le renvoie tel quel
			return textValue;
		}
		// bon format : on convertit en Date
		return DateFormatUtils.format(date, format);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		this.textValue = text;
		Date date = null;
		try {
			date = DateFormatUtils.parseFromFormat(text, DateFormatUtils.DATE_FORMAT);
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
		setValue(date);
	}

}

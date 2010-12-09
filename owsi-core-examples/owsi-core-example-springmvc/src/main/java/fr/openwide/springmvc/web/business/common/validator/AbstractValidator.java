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
package fr.openwide.springmvc.web.business.common.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.springmvc.web.util.DateFormatUtils;

/**
 * <p>Classe abstraite à la base des validateurs</p>
 * 
 * @author Open Wide
 *
 */
public abstract class AbstractValidator implements Validator {
	
	/**
	 * Teste si un entier est strictement positif.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 */
	protected void ensurePositiveInteger(String field, Errors errors) {
		ensurePositiveInteger(field, errors, "validation.field-doit-etre-positif");
	}
	
	/**
	 * Teste si un entier est strictement positif.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param errorCode code du message d'erreur
	 */
	protected void ensurePositiveInteger(String field, Errors errors, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof Integer) {
			Integer i = (Integer) fieldValue;
			if (i > 0) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode);
		}
	}
	
	/**
	 * Teste si un entier est supérieur ou égal à la valeur fournie.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 */
	protected void ensureMinimumInteger(String field, Errors errors, int min) {
		ensureMinimumInteger(field, errors, min, "validation.field-doit-etre-superieur");
	}
	
	/**
	 * Teste si un entier est supérieur ou égal à la valeur fournie.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 * @param errorCode code du message d'erreur
	 */
	protected void ensureMinimumInteger(String field, Errors errors, int min, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof Integer) {
			Integer i = (Integer) fieldValue;
			if (i >= min) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, new Object[] { min }, null);
		}
	}
	
	/**
	 * Teste si un entier est inférieur ou égal à la valeur fournie.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param max borne supérieure
	 */
	protected void ensureMaximumInteger(String field, Errors errors, int max) {
		ensureMaximumInteger(field, errors, max, "validation.field-doit-etre-inferieur");
	}
	
	/**
	 * Teste si un entier est inférieur ou égal à la valeur fournie.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param max borne supérieure
	 * @param errorCode code du message d'erreur
	 */
	protected void ensureMaximumInteger(String field, Errors errors, int max, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof Integer) {
			Integer i = (Integer) fieldValue;
			if (i <= max) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, new Object[] { max }, null);
		}
	}
	
	/**
	 * Teste si un entier est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 * @param max borne supérieure
	 */
	protected void ensureIntegerRange(String field, Errors errors, int min, int max) {
		ensureIntegerRange(field, errors, min, max, "validation.field-entier-hors-plage");
	}
	
	/**
	 * Teste si un entier est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 * @param max borne supérieure
	 * @param errorCode code du message d'erreur
	 */
	protected void ensureIntegerRange(String field, Errors errors, int min, int max, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof Integer) {
			int i = (Integer) fieldValue;
			if (i >= min && i <= max) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, new Object[] { min, max }, null);
		}
	}
	
	/**
	 * Teste si un réel est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 * @param max borne supérieure
	 */
	protected void ensureFloatRange(String field, Errors errors, float min, float max) {
		ensureFloatRange(field, errors, min, max, "validation.field-reel-hors-plage");
	}
	
	/**
	 * Teste si un réel est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param min borne inférieure
	 * @param max borne supérieure
	 * @param errorCode code du message d'erreur
	 */
	protected void ensureFloatRange(String field, Errors errors, float min, float max, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof Float) {
			float i = (Float) fieldValue;
			if (i >= min && i <= max) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, new Object[] { min, max }, null);
		}
	}
	
	/**
	 * Teste si une chaîne de caractères contient du texte.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @return résultat
	 */
	protected boolean ensureStringHasText(String field, Errors errors) {
		return ensureStringHasText(field, errors, "validation.field-string-vide");
	}
	
	/**
	 * Teste si une chaîne de caractères contient du texte.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param errorCode code du message d'erreur
	 * @return résultat
	 */
	protected boolean ensureStringHasText(String field, Errors errors, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof String) {
			String value = (String) fieldValue;
			if (StringUtils.hasText(value)) {
				ok = true;
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, null);
		}
		
		return ok;
	}
	
	/**
	 * Teste si un champ est au format HH:mm.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @return true si le format est bon
	 */
	protected boolean ensureTime(String field, Errors errors) {
		return ensureTime(field, errors, "validation.field-doit-etre-une-heure");
	}
	
	/**
	 * Teste si un champ est au format HH:mm.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param errorCode code du message d'erreur
	 * @return true si le format est bon
	 */
	protected boolean ensureTime(String field, Errors errors, String errorCode) {
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof String) {
			String value = (String) fieldValue;
			// vérification de la string.length() pour refuser certains formats acceptés par SimpleDateFormat
			if (value.length() >= 3 && value.length() <= DateFormatUtils.TIME_FORMAT.length()) {
				// refuse 08
				// refuse 08:00a
				// accepte 8:1 et 08:01
				
				// appel au parsing de SimpleDateFormat
				try {
					new SimpleDateFormat(DateFormatUtils.TIME_FORMAT).parse(value);
					ok = true;
				} catch (ParseException e) {
					// valeur rejetée plus loin
				}
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode);
		}
		
		return ok;
	}
	
	/**
	 * Teste si un champ est au format HH:mm et qu'il est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param minutesMin borne inférieure
	 * @param minutesMax borne supérieure
	 */
	protected void ensureTimeRange(String field, Errors errors, int minutesMin, int minutesMax) {
		ensureTimeRange(field, errors, minutesMin, minutesMax, "validation.field-duree-hors-plage");
	}
	
	/**
	 * Teste si un champ est au format HH:mm et qu'il est compris entre deux bornes (inclues).
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param minutesMin borne inférieure
	 * @param minutesMax borne supérieure
	 * @param errorCode code du message d'erreur
	 */
	protected void ensureTimeRange(String field, Errors errors, int minutesMin, int minutesMax, String errorCode) {
		// vérification du format
		boolean validTime = ensureTime(field, errors);
		if (!validTime) {
			// pas besoin de rejectValue() : ensureTime() l'a déjà fait
			return;
		}
		
		// vérification de la valeur
		boolean ok = false;
		
		Object fieldValue = errors.getFieldValue(field);
		
		if (fieldValue instanceof String) {
			String time = (String) fieldValue;
			try {
				int minutes = DateFormatUtils.timeToMinutes(time);
				if (minutes >= minutesMin && minutes <= minutesMax) {
				    ok = true;
				}
			} catch (ParseException e) {
				// valeur rejetée plus loin
			}
		}
		
		if (!ok) {
			errors.rejectValue(field, errorCode, 
					new Object[] {
						DateFormatUtils.minutesToTimeString(minutesMin),
						DateFormatUtils.minutesToTimeString(minutesMax)
					},
					null);
		}
	}
	
	
	/**
	 * Indique si un fichier uploadé est renseigné.
	 * @param file le fichier
	 * @return true si le fichier est renseigné
	 */
	public boolean isMultipartFileEmpty(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Teste si le fichier uploadé est renseigné et si son extension est correcte.
	 * @param field le champ à tester
	 * @param errors liste des erreurs
	 * @param file le fichier uploadé
	 * @param extension l'extension désirée
	 */
	protected void ensureMultipartFileIsValid(String field, Errors errors, MultipartFile file, String extension) {
		if (isMultipartFileEmpty(file)) {
			errors.rejectValue(field, "fileupload.erreur.no-file");
			return;
		}
		
		if (!StringUtils.lowerCase(file.getOriginalFilename()).endsWith(StringUtils.lowerCase(extension))) {
			Object[] args = { extension };
			errors.rejectValue(field, "fileupload.erreur.bad-extension", args, null);
			return;
		}
	}
}

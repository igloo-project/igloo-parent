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
package fr.openwide.springmvc.web.resolver.bind;

import java.beans.PropertyEditor;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import fr.openwide.springmvc.web.business.common.editor.DateEditor;
import fr.openwide.springmvc.web.util.DateFormatUtils;

/**
 * 
 * @author Open Wide
 */
public class MyWebBindingInitializer implements WebBindingInitializer {
	/**
	 * Enregistrement des {@link PropertyEditor} qui seront systematiquement 
	 * utilisés pour peupler les objets.
	 * 
	 * @param binder le {@link WebDataBinder} utilisé
	 * @param request la requête
	 */
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(java.util.Date.class, new DateEditor(DateFormatUtils.DATE_FORMAT));
	}
}

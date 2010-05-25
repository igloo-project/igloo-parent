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
package fr.openwide.springmvc.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping("localecontroller")
public class LocaleController {

	private static final String DEFAULT_VIEW = "redirect:/";

	@RequestMapping
	public String changeLocale(Model model, HttpServletRequest request, HttpServletResponse response) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

		String locale = request.getParameter("locale");

		if ("en".equals(locale)) {
			localeResolver.setLocale(request, response, Locale.ENGLISH);
		} else if ("fr".equals(locale)) {
			localeResolver.setLocale(request, response, Locale.FRENCH);
		}

		return DEFAULT_VIEW;
	}
}

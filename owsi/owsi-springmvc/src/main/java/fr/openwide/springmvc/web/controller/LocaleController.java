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

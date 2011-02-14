package fr.openwide.springsec.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.openwide.springsec.web.business.SecuredService;

@Controller
public class DefaultController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);
	
	@Autowired
	protected SecuredService securedService;

	/*
	 * Le RequestMapping est fait de manière absolue
	 * par rapport au servlet appelant ce controlleur.
	 * Le servlet est bindé sur /springmvc donc cette 
	 * méthode sera bindée /<context>/springmvc/preauthorize
	 */
	@RequestMapping("/preauthorize")
	public ModelAndView preAuthorizeMethod() {
		String now = securedService.getDate();
		LOGGER.info("Returning preauthorize view with : " + now);
		return new ModelAndView("preauthorize", "now", now);
	}

	@RequestMapping("/postfilter")
	public ModelAndView preFilterMethod() {
		List<String> list = securedService.getList();
		LOGGER.info("Returning posfilter view with : " + list);
		return new ModelAndView("postfilter", "list", list);
	}

}

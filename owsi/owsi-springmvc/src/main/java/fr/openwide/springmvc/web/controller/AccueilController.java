package fr.openwide.springmvc.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Contrôleur qui redirige le / vers la page d'accueil.</p>
 * 
 * @author Open Wide
 */
@Controller("accueilController")
@RequestMapping("/index.html")
public class AccueilController {
	private static final String DEFAULT_VIEW = "accueil";
	
	private static final Log LOGGER = LogFactory.getLog(AccueilController.class);
	
	@RequestMapping
	public String handleRequest(Model model) {
		LOGGER.info("Accueil!!");
		return DEFAULT_VIEW;
	}
}
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
package fr.openwide.springmvc.web.controller.accueil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccueilController.class);
	
	@RequestMapping
	public String handleRequest(Model model) {
		LOGGER.info("Accueil!!");
		return DEFAULT_VIEW;
	}
}
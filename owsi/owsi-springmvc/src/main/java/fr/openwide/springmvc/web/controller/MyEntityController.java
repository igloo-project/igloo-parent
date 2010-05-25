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

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.openwide.springmvc.web.business.common.editor.DateEditor;
import fr.openwide.springmvc.web.business.myentity.model.MyEntity;
import fr.openwide.springmvc.web.business.myentity.model.MyEntityForm;
import fr.openwide.springmvc.web.business.myentity.service.MyEntityService;
import fr.openwide.springmvc.web.business.myentity.validator.MyEntityValidator;
import fr.openwide.springmvc.web.util.DateFormatUtils;
import fr.openwide.springmvc.web.util.SessionUtils;

@Controller
/*
 * Le RequestMapping est fait de manière absolue par rapport au servlet
 * appelant ce controlleur. Le servlet est bindé sur /springmvc donc ce
 * controlleur sera bindé /<context>/springmvc/mycontroller
 */
@RequestMapping("mycontroller")
public class MyEntityController {

	private static final Log LOGGER = LogFactory.getLog(MyEntityController.class);

	private static final String ENTITY_ATTRIBUTE = "entity";
	private static final String ENTITY_FORM_ATTRIBUTE = "myEntityForm";
	
	private static final String DEFAULT_VIEW = "modifentity";
	private static final String SUCCESS_VIEW = "modifsuccess";

	@Autowired
	MyEntityService myService;

	/*
	 * Initialisation du dataBinder qui peuple le formulaire à partir des champs
	 * de la page.
	 */
	@InitBinder
	public void initEntityBinding(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(java.util.Date.class, new DateEditor(DateFormatUtils.DATE_FORMAT));
	}

	/*
	 * Une méthode annotée @ModelAttribute défini une variable qui sera ajoutée
	 * automatiquement dans le modèle chaque fois que le controleur est appelé
	 * 
	 * @ModelAttribute peut aussi être utilisé sur un paramètre ou sur un une
	 * variable de retour d'une méthode @RequestMapping, auquel cas la variable
	 * sera respectivement recupérée depuis le modèle ou placée dans le modèle
	 * (voir MyEntityController.formPost(...))
	 */
	@ModelAttribute("model_attribute_name")
	public String getModelAttribute() {
		return "I'm the string returned by @ModelAttribute(\"model_attribute_name\") example";
	}

	/*
	 * Cette fois-ci le RequestMapping est fait de manière absolue par rapport
	 * au RequestMapping du controlleur. La méthode sera donc bindée
	 * /<context>/springmvc/mycontroller/getentity
	 */
	@RequestMapping(value = "getentity", method = RequestMethod.GET)
	public String formGet(Model model) {
		LOGGER.info("The controller call the service method getEntity");
		MyEntity entity = myService.getEntity();

		MyEntityForm myEntityForm = new MyEntityForm();
		myEntityForm.setId(entity.getId());
		myEntityForm.setDescr(entity.getDescr());
		myEntityForm.setDate(entity.getDate());

		model.addAttribute(ENTITY_ATTRIBUTE, entity);
		model.addAttribute(ENTITY_FORM_ATTRIBUTE, myEntityForm);

		return DEFAULT_VIEW;
	}

	/*
	 * Une méthode annotée @RequestMapping peut avoir un certain nombre de
	 * paramètres préremplis (Model, HttpSession, HttpRequest, ...)
	 * 
	 * Une méthode annotée @RequestMapping peut également avoir des types de
	 * retour différents (Model, ModelAndView, String, ...)
	 * 
	 * Le @ModelAttribute doit être suivi immédiatement de son BindingResult
	 * 
	 * Pour les listes exhaustive consulter la doc @RequestMapping
	 */
	@RequestMapping(value = "postentity", method = RequestMethod.POST)
	public String formPost(Model model,
			@ModelAttribute(ENTITY_FORM_ATTRIBUTE) MyEntityForm myEntityForm,
			BindingResult bindingResult, HttpSession session) {

		Errors errors = new BindException(bindingResult);
		if (!bindingResult.hasErrors()) {
			new MyEntityValidator().validate(myEntityForm, errors);
		}
		if (errors.hasErrors()) {
			LOGGER.warn("An errors has been found during dataBinding or validation");
			MyEntity entity = myService.getEntity();
			model.addAttribute("entity", entity);
			return DEFAULT_VIEW;
		} else {
			MyEntity entity = new MyEntity();
			entity.setId(myEntityForm.getId());
			entity.setDescr(myEntityForm.getDescr());
			entity.setDate(myEntityForm.getDate());

			LOGGER.info("The controller call the service method postEntity");
			myService.postEntity(entity);

			model.addAttribute("entity", entity);
			
			/*
			 * On peu passer dans la session un message signalant le succès de
			 * l'action pour afficher un message d'information dans une vue.
			 */
			SessionUtils.registerSuccess(session, "message.succes");
			return SUCCESS_VIEW;
		}
	}
}

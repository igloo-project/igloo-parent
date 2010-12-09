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
package fr.openwide.springmvc.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>Intercepteur simple permettant de logguer les requêtes URL</p>
 * 
 * @author Open Wide
 */
public class MyInterceptor extends HandlerInterceptorAdapter {

	private static final Log LOGGER = LogFactory.getLog(MyInterceptor.class);
	
	/*
	 * Pratique pour l'interception d'exceptions remontées par les vues car
	 * c'est l'endroit où on y a accès.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		LOGGER.info("afterCompletion URL : " + request.getRequestURL());
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		LOGGER.info("postHandle URL : " + request.getRequestURL());
	}

	/*
	 * Retourne true si la chaîne d'intercepteur doit se poursuivre après la
	 * méthode preHandle. Dans le cas contraire, le DispatcherServlet considère
	 * que l'intercepteur à traiter la réponse lui-même sans appel au
	 * controleur.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		LOGGER.info("preHandle URL : " + request.getRequestURL());
		return true;
	}
}

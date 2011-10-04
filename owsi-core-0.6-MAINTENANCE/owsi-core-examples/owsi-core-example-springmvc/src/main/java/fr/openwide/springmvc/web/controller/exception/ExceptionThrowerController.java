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
package fr.openwide.springmvc.web.controller.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * Contrôleur qui lève des exceptions pour tester le
 * SimpleMappingExceptionResolver.
 * </p>
 * 
 * @author Open Wide
 */
@Controller("exceptionController")
@RequestMapping("exceptioncontroller")
public class ExceptionThrowerController {
	
	@RequestMapping("general-error")
	public String throwException() {
		throw new RuntimeException();
	}
	
	@RequestMapping("state-error")
	public String throwStateException() {
		throw new IllegalStateException();
	}
}

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
package fr.openwide.springmvc.web.taglib;

import java.io.IOException;
import java.util.Date;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.springmvc.web.util.DateFormatUtils;

/**
 * <p>Affiche une date au format demandé.</p>
 * 
 * @author Open Wide
 */
public class DateFormatTag extends RequestContextAwareTag {
	
	private static final long serialVersionUID = -7874836634011902186L;
	
	private Date date;
	
	private String format;
	
	public void setDate(Date date) {
	    this.date = CloneUtils.clone(date);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	@Override
	protected int doStartTagInternal() throws IOException {
		String formatString = getRequestContext().getMessage(format);
		String text = DateFormatUtils.format(date, formatString, getRequestContext().getLocale());
		
		pageContext.getOut().write(text);
		
		return SKIP_BODY;
	}

}

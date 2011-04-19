/*
 * Copyright (C) 2009-2011 Open Wide
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

package fr.openwide.core.wicket.more.markup.html.basic;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.AbstractConverter;

public class BigDecimalToIntegerLabel extends Label {

	private static final long serialVersionUID = -6830982860837635819L;
	
	private static final IConverter CONVERTER = new BigDecimalConverter();

	public BigDecimalToIntegerLabel(String id, IModel<BigDecimal> model) {
		super(id, model);
	}
	
	@Override
	public IConverter getConverter(Class<?> type) {
		return CONVERTER;
	}
	
	private static class BigDecimalConverter extends AbstractConverter {
		private static final long serialVersionUID = 1L;

		@Override
		public Object convertToObject(String value, Locale locale) {
			throw new IllegalAccessError();
		}
		
		@Override
		public String convertToString(Object value, Locale locale) {
			if (value == null) {
				return "";
			} else {
				return String.format("%1$.0f", value);
			}
		}

		@Override
		protected Class<?> getTargetType() {
			return BigDecimal.class;
		}
	}

}

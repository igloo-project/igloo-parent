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

package fr.openwide.core.wicket.more.util.convert.converters;

import java.util.Locale;

import org.apache.wicket.Localizer;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;

public class HumanReadableEnumConverter implements IConverter<Enum<?>> {

	private static final long serialVersionUID = -6934415690685574154L;
	
	private static HumanReadableEnumConverter INSTANCE = new HumanReadableEnumConverter();
	public static HumanReadableEnumConverter get() {
		return INSTANCE;
	}
	
	private HumanReadableEnumConverter() { }

	@Override
	public Enum<?> convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("This converter cannot convert from string to Enum<?>");
	}

	@Override
	public String convertToString(Enum<?> value, Locale locale) {
		if (value == null) {
			return null;
		}
		return Localizer.get().getString(resourceKey(value), null);
	}

	protected String resourceKey(Enum<?> value) {
		return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
	}

}

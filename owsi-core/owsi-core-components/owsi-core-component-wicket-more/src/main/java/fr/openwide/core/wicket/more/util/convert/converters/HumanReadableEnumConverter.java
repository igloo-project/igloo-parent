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
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;

import fr.openwide.core.spring.util.StringUtils;

public class HumanReadableEnumConverter implements IConverter<Enum<?>> {

	private static final long serialVersionUID = -6934415690685574154L;
	
	private static HumanReadableEnumConverter INSTANCE = new HumanReadableEnumConverter();
	public static HumanReadableEnumConverter get() {
		return INSTANCE;
	}
	
	public static HumanReadableEnumConverter withPrefix(String prefix) {
		return with(prefix, null);
	};
	
	public static HumanReadableEnumConverter withSuffix(String suffix) {
		return with(null, suffix);
	};
	
	public static HumanReadableEnumConverter with(String prefix, String suffix) {
		return new HumanReadableEnumConverter(prefix, suffix);
	}
	
	private String prefix = null;
	
	private String suffix = null;
	
	private HumanReadableEnumConverter() { }

	public HumanReadableEnumConverter(String prefix, String suffix) {
		this();
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	@Override
	public Enum<?> convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("This converter cannot convert from string to Enum<?>");
	}

	@Override
	public String convertToString(Enum<?> value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		StringBuilder key = new StringBuilder();
		
		if (StringUtils.hasText(prefix)) {
			key.append(prefix).append(".");
		}
		
		key.append(resourceKey(value));
		
		if (StringUtils.hasText(suffix)) {
			key.append(".").append(suffix);
		}
		
		return Localizer.get().getString(key.toString(), null, Model.of(value));
	}

	protected String resourceKey(Enum<?> value) {
		return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
	}

}

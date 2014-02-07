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

import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.jpa.more.business.generic.model.GenericLocalizedGenericListItem;

public class HumanReadableLocalizedGenericListItemConverter implements IConverter<GenericLocalizedGenericListItem<?, ?>> {

	private static final long serialVersionUID = -6934415690685574154L;
	
	private static HumanReadableLocalizedGenericListItemConverter INSTANCE = new HumanReadableLocalizedGenericListItemConverter();
	public static HumanReadableLocalizedGenericListItemConverter get() {
		return INSTANCE;
	}
	
	private HumanReadableLocalizedGenericListItemConverter() { }

	@Override
	public GenericLocalizedGenericListItem<?, ?> convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException("This converter cannot convert from string to LocalizedGenericListItem");
	}

	@Override
	public String convertToString(GenericLocalizedGenericListItem<?, ?> value, Locale locale) {
		if (value == null) {
			return null;
		} else {
			return value.getLabel().getOrDefault(locale);
		}
	}

}

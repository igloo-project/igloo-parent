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

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.bindgen.Binding;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItemBinding;
import fr.openwide.core.spring.util.SpringBeanUtils;

public class GenericListItemListLabel extends Label {

	private static final long serialVersionUID = -6830982860837635819L;
	
	private static final String DEFAULT_SEPARATOR = ", ";
	
	@SuppressWarnings("rawtypes")
	private static final GenericListItemBinding<?> GENERIC_LIST_ITEM_BINDING = new GenericListItemBinding();
	
	private final String separator;
	
	private final Binding<String> binding;

	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model,
			Binding<String> binding, String separator) {
		super(id, model);
		this.separator = separator;
		this.binding = binding;
	}
	
	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model,
			Binding<String> binding) {
		this(id, model, binding, DEFAULT_SEPARATOR);
	}
	
	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model,
			String separator) {
		this(id, model, GENERIC_LIST_ITEM_BINDING.label(), separator);
	}
	
	public GenericListItemListLabel(String id, IModel<? extends List<? extends GenericListItem<?>>> model) {
		this(id, model, GENERIC_LIST_ITEM_BINDING.label(), DEFAULT_SEPARATOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (List.class.isAssignableFrom(type)) {
			return (IConverter<C>) new GenericListItemListConverter(separator);
		} else {
			return super.getConverter(type);
		}
	}
	
	private class GenericListItemListConverter extends AbstractConverter<List<GenericListItem<?>>> {
		private static final long serialVersionUID = 1L;
		
		private final String separator;
		
		private GenericListItemListConverter(String separator) {
			this.separator = separator;
		}

		@Override
		public List<GenericListItem<?>> convertToObject(String value, Locale locale) {
			throw new IllegalAccessError();
		}
		
		@Override
		public String convertToString(List<GenericListItem<?>> value, Locale locale) {
			StringBuilder sb = new StringBuilder();
			for (GenericListItem<?> item : value) {
				if (sb.length() > 0) {
					sb.append(this.separator);
				}
				sb.append(SpringBeanUtils.getBeanWrapper(item).getPropertyValue(binding.getPath()));
			}
			return sb.toString();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Class<List<GenericListItem<?>>> getTargetType() {
			return (Class<List<GenericListItem<?>>>) (Object) List.class;
		}
	}

}
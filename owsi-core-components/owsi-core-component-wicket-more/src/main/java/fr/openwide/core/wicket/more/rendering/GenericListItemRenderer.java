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

package fr.openwide.core.wicket.more.rendering;

import java.util.Locale;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

public class GenericListItemRenderer extends Renderer<GenericListItem<?>> {

	private static final long serialVersionUID = -4595938276377495743L;
	
	private static GenericListItemRenderer INSTANCE = new GenericListItemRenderer();
	public static GenericListItemRenderer get() {
		return INSTANCE;
	}
	
	/**
	 * @deprecated Use {@link #get()} instead.
	 */
	@Deprecated
	protected GenericListItemRenderer() { }

	@Override
	public String render(GenericListItem<?> value, Locale locale) {
		if (value == null) {
			return null;
		} else {
			return value.getLabel();
		}
	}

}

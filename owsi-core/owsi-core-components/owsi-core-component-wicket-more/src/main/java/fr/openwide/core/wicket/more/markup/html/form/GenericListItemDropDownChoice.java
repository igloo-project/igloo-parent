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

package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.wicket.more.markup.html.model.GenericListItemListModel;

public class GenericListItemDropDownChoice<T extends GenericListItem<? super T>> extends DropDownChoice<T> {
	private static final long serialVersionUID = 6111025269398387253L;
	
	public GenericListItemDropDownChoice(String id, IModel<T> model, Class<T> clazz) {
		super(id);
		setModel(model);
		setChoices(new GenericListItemListModel<T>(clazz, true));
		setChoiceRenderer(new GenericListItemChoiceRenderer());
		setNullValid(true);
	}
	
	private class GenericListItemChoiceRenderer implements IChoiceRenderer<T> {
		private static final long serialVersionUID = 69846864597356995L;

		@Override
		public Object getDisplayValue(T object) {
			return object.getLabel();
		}

		@Override
		public String getIdValue(T object, int index) {
			return object.getId().toString();
		}
		
	}

}

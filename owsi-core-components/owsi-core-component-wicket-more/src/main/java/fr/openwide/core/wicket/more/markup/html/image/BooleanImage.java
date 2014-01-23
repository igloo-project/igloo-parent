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

package fr.openwide.core.wicket.more.markup.html.image;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;

@Deprecated
public class BooleanImage extends Image {
	private static final long serialVersionUID = 174241024446763272L;
	
	private static final ResourceReference IMAGE_TRUE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/tick.png");
	
	private static final ResourceReference IMAGE_FALSE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/cross.png");
	
	public BooleanImage(String id, IModel<Boolean> model) {
		super(id, model);
	}
	
	@Override
	public void onComponentTag(final ComponentTag tag) {
		Boolean value = getValue();
		if (value != null && value) {
			setImageResourceReference(IMAGE_TRUE);
		} else {
			setImageResourceReference(IMAGE_FALSE);
		}
		
		super.onComponentTag(tag);
	}
	
	@Override
	public boolean isVisible() {
		return super.isVisible() && (getValue() != null);
	}
	
	private Boolean getValue() {
		return (Boolean) getDefaultModelObject();
	}

}

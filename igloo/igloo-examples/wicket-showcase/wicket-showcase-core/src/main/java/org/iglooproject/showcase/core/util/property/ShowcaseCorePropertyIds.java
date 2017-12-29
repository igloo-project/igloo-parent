package org.iglooproject.showcase.core.util.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public final class ShowcaseCorePropertyIds extends AbstractPropertyIds {
	
	private ShowcaseCorePropertyIds() {
	}

	public static final ImmutablePropertyId<String> SHOWCASE_FILE_ROOT_DIRECTORY = immutable("showcaseFile.rootDirectory");

}

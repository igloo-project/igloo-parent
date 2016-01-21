package fr.openwide.core.showcase.core.util.property;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class ShowcaseCorePropertyIds extends AbstractPropertyIds {
	
	private ShowcaseCorePropertyIds() {
	}

	public static final ImmutablePropertyId<String> SHOWCASE_FILE_ROOT_DIRECTORY = immutable("showcaseFile.rootDirectory");

}

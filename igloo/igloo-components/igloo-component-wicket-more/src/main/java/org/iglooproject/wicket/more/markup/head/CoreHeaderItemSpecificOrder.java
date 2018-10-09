package org.iglooproject.wicket.more.markup.head;

import java.util.stream.Stream;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.ResourceAggregator.RecordedHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

public class CoreHeaderItemSpecificOrder implements IHeaderItemSpecificOrder {

	private static final CoreHeaderItemSpecificOrder INSTANCE = new CoreHeaderItemSpecificOrder();

	protected static enum HeaderItemOrder {
		
		// JQuery and JQuery UI need to be added before Bootstrap JS resources.
		JQUERY(Application.get().getJavaScriptLibrarySettings().getJQueryReference()),
		JQUERY_UI(JQueryUIJavaScriptResourceReference.get());
		
		private final ResourceReference resourceReference;
		
		private HeaderItemOrder(ResourceReference resourceReference) {
			this.resourceReference = resourceReference;
		}
	}

	public static final CoreHeaderItemSpecificOrder get() {
		return INSTANCE;
	}

	@Override
	public Integer order(RecordedHeaderItem headerItem) {
		if (headerItem == null) {
			return null;
		}
		
		ResourceReference resourceReference = getResourceReference(headerItem);
		
		if (resourceReference == null) {
			return null;
		}
		
		return Stream.of(HeaderItemOrder.values())
			.filter(o -> resourceReference.equals(o.resourceReference))
			.findFirst()
			.map(Enum::ordinal)
			.orElse(null);
	}

}

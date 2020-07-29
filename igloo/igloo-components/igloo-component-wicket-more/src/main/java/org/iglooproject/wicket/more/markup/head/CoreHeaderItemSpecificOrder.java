package org.iglooproject.wicket.more.markup.head;

import java.util.stream.Stream;

import org.apache.wicket.markup.head.ResourceAggregator.RecordedHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;

public class CoreHeaderItemSpecificOrder implements IHeaderItemSpecificOrder {

	private static final CoreHeaderItemSpecificOrder INSTANCE = new CoreHeaderItemSpecificOrder();

	protected enum HeaderItemOrder {
		
		;
		// Add top priority resource references here.
		
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

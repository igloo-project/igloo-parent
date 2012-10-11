package fr.openwide.core.wicket.more.core;

import java.util.Comparator;

import org.apache.wicket.markup.head.IHeaderResponse;

public class CoreWiQueryDecoratingHeaderResponse extends WiQueryDecoratingHeaderResponse {

	private static final String EXTERNAL_RESOURCE_HIGH_PRIORITY = "000";
	private static final String INTERNAL_RESOURCE_NORMAL_PRIORITY = "100";
	private static final String PRIORITY_SEPARATOR = "-";

	public CoreWiQueryDecoratingHeaderResponse(IHeaderResponse real) {
		super(real);
	}

	@Override
	protected String newGroupingKey(ResourceReferenceAndStringData ref) {
		// on force le chargement des API externes (donc google) avant les autres
		if (ref.getReference() == null) {
			return EXTERNAL_RESOURCE_HIGH_PRIORITY + PRIORITY_SEPARATOR;
		}
		return INTERNAL_RESOURCE_NORMAL_PRIORITY + PRIORITY_SEPARATOR + super.newGroupingKey(ref);
	}
	
	@Override
	protected Comparator<String> getGroupingKeyComparator() {
		return new Comparator<String>() {
			private WiQuerySettings settings = WiQuerySettings.get();

			@Override
			public int compare(String o1, String o2) {
				String o1PriorityPrefix;
				String o1GroupingKey;
				
				String o2PriorityPrefix;
				String o2GroupingKey;
				
				if (o1 != null && o1.contains("-")) {
					o1PriorityPrefix = o1.substring(0, o1.indexOf('-'));
					o1GroupingKey = o1.substring(o1.indexOf('-') + 1);
				} else {
					o1PriorityPrefix = INTERNAL_RESOURCE_NORMAL_PRIORITY;
					o1GroupingKey = o1;
				}
				if (o2 != null && o2.contains("-")) {
					o2PriorityPrefix = o2.substring(0, o2.indexOf('-'));
					o2GroupingKey = o2.substring(o2.indexOf('-') + 1);
				} else {
					o2PriorityPrefix = INTERNAL_RESOURCE_NORMAL_PRIORITY;
					o2GroupingKey = o2;
				}
				
				int o1index = settings.getResourceGroupingKeys().indexOf(o1GroupingKey);
				int o2index = settings.getResourceGroupingKeys().indexOf(o2GroupingKey);
				
				if (o1PriorityPrefix.compareToIgnoreCase(o2PriorityPrefix) != 0) {
					return o1PriorityPrefix.compareToIgnoreCase(o2PriorityPrefix);
				}
				
				if (o1index < 0 && o2index < 0) {
					if (o1GroupingKey == null) {
						return o2GroupingKey == null ? 0 : 1;
					} else if (o2GroupingKey == null) {
						return -1;
					} else {
						return o1GroupingKey.compareToIgnoreCase(o2GroupingKey);
					}
				} else if (o1index < 0) {
					o1index = Integer.MAX_VALUE;
				} else if (o2index < 0) {
					o2index = Integer.MAX_VALUE;
				}

				return o1index - o2index;
			}
		};
	}

}
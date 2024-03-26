package org.iglooproject.jpa.more.business.difference.selector;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.danielbechler.diff.selector.ElementSelector;

public class CollectionItemByIndexSelector extends ElementSelector implements IKeyAwareSelector<Integer> {
	
	private final int index;

	public CollectionItemByIndexSelector(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof CollectionItemByIndexSelector) {
			CollectionItemByIndexSelector other = (CollectionItemByIndexSelector)o;
			return index == other.index;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(index)
				.build();
	}

	@Override
	public String toHumanReadableString() {
		return "[" + index + "]";
	}
	
	@Override
	public Integer getKey() {
		return index;
	}
}

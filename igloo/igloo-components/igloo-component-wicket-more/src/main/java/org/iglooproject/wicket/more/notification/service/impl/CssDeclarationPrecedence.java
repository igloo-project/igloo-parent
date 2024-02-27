package org.iglooproject.wicket.more.notification.service.impl;

import java.util.Collection;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

/**
 * @see {@link http://www.w3.org/TR/2009/CR-CSS2-20090908/cascade.html#cascading-order}
 * @see CssSelectorSpecificity
 */
@Deprecated
public class CssDeclarationPrecedence implements Comparable<CssDeclarationPrecedence> {
	
	private final boolean isImportant;
	private final CssSelectorSpecificity selectorSpecificity;
	
	public CssDeclarationPrecedence(boolean isImportant, Collection<CssSelectorSpecificity> selectorsSpecificity) {
		Assert.isTrue(!Iterables.isEmpty(selectorsSpecificity), "[Assertion failed] - this expression must be true");
		this.isImportant = isImportant;
		this.selectorSpecificity = Ordering.natural().max(selectorsSpecificity);
	}
	
	public CssDeclarationPrecedence(boolean isImportant, CssSelectorSpecificity selectorSpecificity) {
		this.isImportant = isImportant;
		this.selectorSpecificity = selectorSpecificity;
	}

	@Override
	public int compareTo(CssDeclarationPrecedence other) {
		return new CompareToBuilder()
				.append(isImportant, other.isImportant)
				.append(selectorSpecificity, other.selectorSpecificity)
				.build();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof CssDeclarationPrecedence)) {
			return false;
		}
		CssDeclarationPrecedence other = (CssDeclarationPrecedence) obj;
		return compareTo(other) == 0;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(isImportant)
				.append(selectorSpecificity)
				.build();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("isImportant", isImportant)
				.append("selectorSpecificity", selectorSpecificity)
				.build();
	}

}

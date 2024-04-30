package org.iglooproject.jpa.more.search.query;

import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.sort.ISort;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

public class HibernateSearchUtils {

	public static final String WILDCARD = "*";
	public static final String OR = " | ";
	public static final String AND = " + ";

	/**
	 * Split on whitespace.
	 */
	public static String wildcardTokensOr(String term) {
		return wildcardTokensOr(term, CharMatcher.whitespace());
	}

	public static String wildcardTokensOr(String term, CharMatcher charMatcher) {
		if (term == null || charMatcher == null) {
			return null;
		}
		return Splitter.on(charMatcher).trimResults().omitEmptyStrings().splitToStream(term)
			.map(t -> t + WILDCARD)
			.collect(Collectors.joining(OR));
	}

	/**
	 * Split on whitespace.
	 */
	public static String wildcardTokensAnd(String term) {
		return wildcardTokensAnd(term, CharMatcher.whitespace());
	}

	public static String wildcardTokensAnd(String term, CharMatcher charMatcher) {
		if (term == null || charMatcher == null) {
			return null;
		}
		return Splitter.on(charMatcher).trimResults().omitEmptyStrings().splitToStream(term)
			.map(t -> t + WILDCARD)
			.collect(Collectors.joining(AND));
	}

	public static SortOrder toSortOrder(ISort<?> sort, ISort.SortOrder sortOrder) {
		Objects.requireNonNull(sort);
		
		ISort.SortOrder effectiveSortOrder = sort.getDefaultOrder().asDefaultFor(sortOrder);
		
		return switch (effectiveSortOrder) {
		case ASC -> SortOrder.ASC;
		case DESC -> SortOrder.DESC;
		default -> throw new IllegalSwitchValueException(effectiveSortOrder);
		};
	}

}

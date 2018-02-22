package org.iglooproject.wicket.more.webjars;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public final class WebjarUtil {

	private WebjarUtil() {} //NOSONAR

	public static Supplier<List<HeaderItem>> memoizeHeaderItemsforReferences(ResourceReference... references) {
		return Suppliers.memoize(
			() -> Lists.newArrayList(references).stream()
					.map(JavaScriptHeaderItem::forReference)
					.collect(Collectors.toList())
		);
	}

}

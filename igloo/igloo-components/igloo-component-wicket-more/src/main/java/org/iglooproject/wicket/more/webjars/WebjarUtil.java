package org.iglooproject.wicket.more.webjars;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public final class WebjarUtil {

	private WebjarUtil() {} //NOSONAR

	public static Supplier<List<HeaderItem>> memoizeHeaderItemsforReferences(ResourceReference... references) {
		return Suppliers.memoize(
			() -> Lists.newArrayList(references).stream()
					.map(WebjarUtil::forReference)
					.collect(Collectors.toList())
		);
	}

	private static HeaderItem forReference(ResourceReference reference) {
		if (reference instanceof JavaScriptResourceReference) {
			return JavaScriptHeaderItem.forReference(reference);
		} else if (reference instanceof CssResourceReference) {
			return CssHeaderItem.forReference(reference);
		} else {
			throw new IllegalArgumentException(
					String.format("Type %s of [%s] cannot be linked to a javascript or css item",
							reference.getClass().getSimpleName(), reference));
		}
	}

}

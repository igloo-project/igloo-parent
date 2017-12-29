package org.iglooproject.wicket.more.console.template.style;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.retzlaff.select2.resource.Select2CssResourceReference;

import com.google.common.collect.Lists;

import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;
import org.iglooproject.wicket.more.markup.html.template.css.bootstrap2.jqueryui.JQueryUiCssResourceReference;

public final class ConsoleSignInLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = -2663350086883944309L;

	private static final ConsoleSignInLessCssResourceReference INSTANCE = new ConsoleSignInLessCssResourceReference();

	private ConsoleSignInLessCssResourceReference() {
		super(ConsoleSignInLessCssResourceReference.class, "signin.less");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(2);
		dependencies.add(CssHeaderItem.forReference(JQueryUiCssResourceReference.get()));
		dependencies.add(CssHeaderItem.forReference(Select2CssResourceReference.get()));
		return dependencies;
	}

	public static ConsoleSignInLessCssResourceReference get() {
		return INSTANCE;
	}

}

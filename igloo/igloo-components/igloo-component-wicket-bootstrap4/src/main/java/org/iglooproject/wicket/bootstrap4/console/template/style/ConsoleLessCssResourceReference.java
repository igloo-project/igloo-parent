package org.iglooproject.wicket.bootstrap4.console.template.style;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;
import org.retzlaff.select2.resource.Select2CssResourceReference;

import com.google.common.collect.Lists;

public final class ConsoleLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 7402497660522113371L;

	private static final ConsoleLessCssResourceReference INSTANCE = new ConsoleLessCssResourceReference();

	private ConsoleLessCssResourceReference() {
		super(ConsoleLessCssResourceReference.class, "console.less");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(2);
		dependencies.add(CssHeaderItem.forReference(JQueryUiCssResourceReference.get()));
		dependencies.add(CssHeaderItem.forReference(Select2CssResourceReference.get()));
		return dependencies;
	}

	public static ConsoleLessCssResourceReference get() {
		return INSTANCE;
	}

}

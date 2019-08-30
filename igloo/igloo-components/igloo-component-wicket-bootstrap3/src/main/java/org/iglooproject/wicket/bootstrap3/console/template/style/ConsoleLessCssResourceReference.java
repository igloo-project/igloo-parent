package org.iglooproject.wicket.bootstrap3.console.template.style;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;

import com.google.common.collect.ImmutableList;

public final class ConsoleLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 7402497660522113371L;

	private static final ConsoleLessCssResourceReference INSTANCE = new ConsoleLessCssResourceReference();

	private ConsoleLessCssResourceReference() {
		super(ConsoleLessCssResourceReference.class, "console.less");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return ImmutableList.of(
			CssHeaderItem.forReference(JQueryUiCssResourceReference.get())
		);
	}

	public static ConsoleLessCssResourceReference get() {
		return INSTANCE;
	}

}

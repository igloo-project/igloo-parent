package org.iglooproject.wicket.bootstrap3.console.template.style;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;

import com.google.common.collect.ImmutableList;

public final class ConsoleSignInLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = -2663350086883944309L;

	private static final ConsoleSignInLessCssResourceReference INSTANCE = new ConsoleSignInLessCssResourceReference();

	private ConsoleSignInLessCssResourceReference() {
		super(ConsoleSignInLessCssResourceReference.class, "signin.less");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return ImmutableList.of(
			CssHeaderItem.forReference(JQueryUiCssResourceReference.get())
		);
	}

	public static ConsoleSignInLessCssResourceReference get() {
		return INSTANCE;
	}

}

package org.iglooproject.basicapp.web.application.common.template.resources.styles.console.console;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;

import com.google.common.collect.ImmutableList;

public final class ConsoleScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 7402497660522113371L;

	private static final ConsoleScssResourceReference INSTANCE = new ConsoleScssResourceReference();

	private ConsoleScssResourceReference() {
		super(ConsoleScssResourceReference.class, "console.scss");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return ImmutableList.of(
			CssHeaderItem.forReference(JQueryUiCssResourceReference.get())
		);
	}

	public static ConsoleScssResourceReference get() {
		return INSTANCE;
	}

}

package org.iglooproject.basicapp.web.application.common.template.resources.styles.application.basic;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;

import com.google.common.collect.ImmutableList;

public final class StylesScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 1L;

	private static final StylesScssResourceReference INSTANCE = new StylesScssResourceReference();

	private StylesScssResourceReference() {
		super(StylesScssResourceReference.class, "styles.scss");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		return ImmutableList.of(
			CssHeaderItem.forReference(JQueryUiCssResourceReference.get())
		);
	}

	public static StylesScssResourceReference get() {
		return INSTANCE;
	}

}

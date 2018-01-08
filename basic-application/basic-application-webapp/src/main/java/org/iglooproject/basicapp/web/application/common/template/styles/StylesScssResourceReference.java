package org.iglooproject.basicapp.web.application.common.template.styles;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;
import org.iglooproject.wicket.more.markup.html.template.css.bootstrap3.jqueryui.JQueryUiCssResourceReference;
import org.retzlaff.select2.resource.Select2CssResourceReference;

import com.google.common.collect.Lists;

public final class StylesScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = -6024518060296236148L;

	private static final StylesScssResourceReference INSTANCE = new StylesScssResourceReference();

	private StylesScssResourceReference() {
		super(StylesScssResourceReference.class, "styles.scss");
	}
	
	@Override
	public List<HeaderItem> getDependencies() {
		// Make sure the overridden styles appear before their overrides
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(2);
		dependencies.add(CssHeaderItem.forReference(JQueryUiCssResourceReference.get()));
		dependencies.add(CssHeaderItem.forReference(Select2CssResourceReference.get()));
		return dependencies;
	}

	public static StylesScssResourceReference get() {
		return INSTANCE;
	}

}

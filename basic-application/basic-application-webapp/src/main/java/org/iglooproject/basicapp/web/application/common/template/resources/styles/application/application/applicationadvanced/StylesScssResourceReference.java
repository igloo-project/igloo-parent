package org.iglooproject.basicapp.web.application.common.template.resources.styles.application.application.applicationadvanced;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

public final class StylesScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 1L;

	private static final StylesScssResourceReference INSTANCE = new StylesScssResourceReference();

	private StylesScssResourceReference() {
		super(StylesScssResourceReference.class, "styles.scss");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
		return dependencies;
	}

	public static StylesScssResourceReference get() {
		return INSTANCE;
	}

}

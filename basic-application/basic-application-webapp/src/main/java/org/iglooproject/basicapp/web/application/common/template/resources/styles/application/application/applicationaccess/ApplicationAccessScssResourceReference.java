package org.iglooproject.basicapp.web.application.common.template.resources.styles.application.application.applicationaccess;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.css.scss.ScssResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

public final class ApplicationAccessScssResourceReference extends ScssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final ApplicationAccessScssResourceReference INSTANCE = new ApplicationAccessScssResourceReference();

	private ApplicationAccessScssResourceReference() {
		super(ApplicationAccessScssResourceReference.class, "application-access.scss");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
		return dependencies;
	}

	public static ApplicationAccessScssResourceReference get() {
		return INSTANCE;
	}

}

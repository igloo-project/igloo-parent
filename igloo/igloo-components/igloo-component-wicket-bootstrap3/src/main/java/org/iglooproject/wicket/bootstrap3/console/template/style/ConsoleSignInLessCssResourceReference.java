package org.iglooproject.wicket.bootstrap3.console.template.style;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

public final class ConsoleSignInLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = -2663350086883944309L;

	private static final ConsoleSignInLessCssResourceReference INSTANCE = new ConsoleSignInLessCssResourceReference();

	private ConsoleSignInLessCssResourceReference() {
		super(ConsoleSignInLessCssResourceReference.class, "signin.less");
	}

	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = super.getDependencies();
		dependencies.add(CssHeaderItem.forReference(WiQueryCoreThemeResourceReference.get()));
		return dependencies;
	}

	public static ConsoleSignInLessCssResourceReference get() {
		return INSTANCE;
	}

}

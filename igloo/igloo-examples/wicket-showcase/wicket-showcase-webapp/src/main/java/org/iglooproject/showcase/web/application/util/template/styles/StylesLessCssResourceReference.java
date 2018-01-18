package org.iglooproject.showcase.web.application.util.template.styles;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;

import com.google.common.collect.Lists;

public class StylesLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final StylesLessCssResourceReference INSTANCE = new StylesLessCssResourceReference();

	private StylesLessCssResourceReference() {
		super(StylesLessCssResourceReference.class, "styles.less");
	}
	
	@Override
	public List<HeaderItem> getDependencies() {
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(2);
		dependencies.add(CssHeaderItem.forReference(JQueryUiCssResourceReference.get()));
		return dependencies;
	}
	
	public static StylesLessCssResourceReference get() {
		return INSTANCE;
	}
}

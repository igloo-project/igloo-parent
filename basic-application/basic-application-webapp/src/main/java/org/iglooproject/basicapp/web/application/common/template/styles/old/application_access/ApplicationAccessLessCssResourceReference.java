package org.iglooproject.basicapp.web.application.common.template.styles.old.application_access;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;
import org.retzlaff.select2.resource.Select2CssResourceReference;

import com.google.common.collect.Lists;

public final class ApplicationAccessLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final ApplicationAccessLessCssResourceReference INSTANCE = new ApplicationAccessLessCssResourceReference();

	private ApplicationAccessLessCssResourceReference() {
		super(ApplicationAccessLessCssResourceReference.class, "application-access.less");
	}
	
	@Override
	public List<HeaderItem> getDependencies() {
		// Make sure the overridden styles appear before their overrides
		List<HeaderItem> dependencies = Lists.newArrayListWithExpectedSize(2);
		dependencies.add(CssHeaderItem.forReference(JQueryUiCssResourceReference.get()));
		dependencies.add(CssHeaderItem.forReference(Select2CssResourceReference.get()));
		return dependencies;
	}

	public static ApplicationAccessLessCssResourceReference get() {
		return INSTANCE;
	}

}

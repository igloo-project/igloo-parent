package fr.openwide.core.basicapp.web.application.common.template.styles;

import fr.openwide.core.wicket.more.css.lesscss.LessCssResourceReference;

public final class ApplicationAccessLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final ApplicationAccessLessCssResourceReference INSTANCE = new ApplicationAccessLessCssResourceReference();

	private ApplicationAccessLessCssResourceReference() {
		super(ApplicationAccessLessCssResourceReference.class, "application-access.less");
	}

	public static ApplicationAccessLessCssResourceReference get() {
		return INSTANCE;
	}

}

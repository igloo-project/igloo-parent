package fr.openwide.core.basicapp.web.application.common.template.styles;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class ServiceLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final ServiceLessCssResourceReference INSTANCE = new ServiceLessCssResourceReference();

	private ServiceLessCssResourceReference() {
		super(ServiceLessCssResourceReference.class, "service.less");
	}

	public static ServiceLessCssResourceReference get() {
		return INSTANCE;
	}

}

package fr.openwide.core.wicket.more.markup.html.template.css.bootstrap2.bootstrap;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class DefaultBootstrapLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 3351900003381313127L;

	private static final DefaultBootstrapLessCssResourceReference INSTANCE = new DefaultBootstrapLessCssResourceReference();

	private DefaultBootstrapLessCssResourceReference() {
		super(DefaultBootstrapLessCssResourceReference.class, "bootstrap.less");
	}

	public static DefaultBootstrapLessCssResourceReference get() {
		return INSTANCE;
	}

}

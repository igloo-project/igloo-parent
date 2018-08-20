package org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.bootstrap;

import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;

/**
 * @deprecated Use Bootstrap 4 instead.
 */
@Deprecated
public final class DefaultBootstrap3LessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 3351900003381313127L;

	private static final DefaultBootstrap3LessCssResourceReference INSTANCE = new DefaultBootstrap3LessCssResourceReference();

	private DefaultBootstrap3LessCssResourceReference() {
		super(DefaultBootstrap3LessCssResourceReference.class, "bootstrap.less");
	}

	public static DefaultBootstrap3LessCssResourceReference get() {
		return INSTANCE;
	}

}

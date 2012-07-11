package fr.openwide.core.basicapp.web.application.common.template.styles;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class StylesLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = -6024518060296236148L;

	private static final StylesLessCssResourceReference INSTANCE = new StylesLessCssResourceReference();

	private StylesLessCssResourceReference() {
		super(StylesLessCssResourceReference.class, "styles.less");
	}

	public static StylesLessCssResourceReference get() {
		return INSTANCE;
	}

}

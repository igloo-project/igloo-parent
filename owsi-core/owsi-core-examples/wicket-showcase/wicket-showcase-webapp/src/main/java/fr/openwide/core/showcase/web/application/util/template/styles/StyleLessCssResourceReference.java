package fr.openwide.core.showcase.web.application.util.template.styles;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public class StyleLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final StyleLessCssResourceReference INSTANCE = new StyleLessCssResourceReference();

	public StyleLessCssResourceReference() {
		super(StyleLessCssResourceReference.class, "style.less");
	}
	
	public static StyleLessCssResourceReference get() {
		return INSTANCE;
	}
}

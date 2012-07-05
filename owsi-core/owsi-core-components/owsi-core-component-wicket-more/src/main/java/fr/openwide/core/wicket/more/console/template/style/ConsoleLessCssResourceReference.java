package fr.openwide.core.wicket.more.console.template.style;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class ConsoleLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 7402497660522113371L;

	private static final ConsoleLessCssResourceReference INSTANCE = new ConsoleLessCssResourceReference();

	private ConsoleLessCssResourceReference() {
		super(ConsoleLessCssResourceReference.class, "console.less");
	}

	public static ConsoleLessCssResourceReference get() {
		return INSTANCE;
	}

}

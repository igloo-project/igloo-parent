package fr.openwide.core.wicket.more.console.template.style;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class ConsoleSignInLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = -2663350086883944309L;

	private static final ConsoleSignInLessCssResourceReference INSTANCE = new ConsoleSignInLessCssResourceReference();

	private ConsoleSignInLessCssResourceReference() {
		super(ConsoleSignInLessCssResourceReference.class, "signin.less");
	}

	public static ConsoleSignInLessCssResourceReference get() {
		return INSTANCE;
	}

}

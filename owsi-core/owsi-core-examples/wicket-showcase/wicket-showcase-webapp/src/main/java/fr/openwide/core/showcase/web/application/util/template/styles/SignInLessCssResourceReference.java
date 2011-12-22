package fr.openwide.core.showcase.web.application.util.template.styles;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public class SignInLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 4656765761895221782L;

	private static final SignInLessCssResourceReference INSTANCE = new SignInLessCssResourceReference();

	public SignInLessCssResourceReference() {
		super(SignInLessCssResourceReference.class, "signin.less");
	}

	public static SignInLessCssResourceReference get() {
		return INSTANCE;
	}

}

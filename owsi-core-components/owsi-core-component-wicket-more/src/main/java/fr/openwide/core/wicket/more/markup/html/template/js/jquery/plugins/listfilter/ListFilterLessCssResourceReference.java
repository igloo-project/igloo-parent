package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter;

import fr.openwide.core.wicket.more.lesscss.LessCssResourceReference;

public final class ListFilterLessCssResourceReference extends LessCssResourceReference {

	private static final long serialVersionUID = 5159425549094684032L;

	private static final ListFilterLessCssResourceReference INSTANCE = new ListFilterLessCssResourceReference();

	private ListFilterLessCssResourceReference() {
		super(ListFilterLessCssResourceReference.class, "listfilter.less");
	}

	public static ListFilterLessCssResourceReference get() {
		return INSTANCE;
	}

}

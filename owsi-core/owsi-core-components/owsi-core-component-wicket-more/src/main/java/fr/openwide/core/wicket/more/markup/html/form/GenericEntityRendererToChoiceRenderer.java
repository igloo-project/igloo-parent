package fr.openwide.core.wicket.more.markup.html.form;

import org.apache.wicket.Session;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.rendering.Renderer;

public final class GenericEntityRendererToChoiceRenderer {

	private GenericEntityRendererToChoiceRenderer() {
	}

	public static <E extends GenericEntity<?, ?>>
			AbstractGenericEntityChoiceRenderer<E> of(final Renderer<E> renderer) {
		return new AbstractGenericEntityChoiceRenderer<E>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Object getDisplayValue(E value) {
				return renderer.render(value, Session.get().getLocale());
			}
		};
	}

	public static <E extends GenericEntity<?, ?>>
			AbstractGenericEntityChoiceRenderer<E> ofWildcardRenderer(final Renderer<? super E> renderer) {
		return new AbstractGenericEntityChoiceRenderer<E>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Object getDisplayValue(E value) {
				return renderer.render(value, Session.get().getLocale());
			}
		};
	}

}

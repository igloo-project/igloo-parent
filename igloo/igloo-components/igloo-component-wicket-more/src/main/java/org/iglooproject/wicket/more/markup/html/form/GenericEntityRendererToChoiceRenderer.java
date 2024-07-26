package org.iglooproject.wicket.more.markup.html.form;

import igloo.wicket.renderer.Renderer;
import org.apache.wicket.Session;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public final class GenericEntityRendererToChoiceRenderer {

  private GenericEntityRendererToChoiceRenderer() {}

  public static <E extends GenericEntity<?, ?>> AbstractGenericEntityChoiceRenderer<E> of(
      final Renderer<E> renderer) {
    return new AbstractGenericEntityChoiceRenderer<E>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Object getDisplayValue(E value) {
        return renderer.render(value, Session.get().getLocale());
      }
    };
  }

  public static <E extends GenericEntity<?, ?>>
      AbstractGenericEntityChoiceRenderer<E> ofWildcardRenderer(
          final Renderer<? super E> renderer) {
    return new AbstractGenericEntityChoiceRenderer<E>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Object getDisplayValue(E value) {
        return renderer.render(value, Session.get().getLocale());
      }
    };
  }
}

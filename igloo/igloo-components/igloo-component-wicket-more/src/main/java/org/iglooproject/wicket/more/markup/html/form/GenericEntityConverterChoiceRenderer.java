package org.iglooproject.wicket.more.markup.html.form;

import java.util.Locale;
import org.apache.wicket.Session;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public class GenericEntityConverterChoiceRenderer<T extends GenericEntity<?, ?>>
    extends AbstractGenericEntityChoiceRenderer<T> {

  private static final long serialVersionUID = -8949718525256769535L;

  private final IConverter<? super T> converter;

  public GenericEntityConverterChoiceRenderer(IConverter<? super T> converter) {
    super();
    this.converter = converter;
  }

  @Override
  public Object getDisplayValue(T object) {
    return converter.convertToString(object, getLocale());
  }

  protected Locale getLocale() {
    return Session.get().getLocale();
  }
}

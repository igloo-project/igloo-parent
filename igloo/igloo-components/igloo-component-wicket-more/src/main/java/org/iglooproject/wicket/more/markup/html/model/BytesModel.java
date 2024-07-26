package org.iglooproject.wicket.more.markup.html.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Bytes;

public class BytesModel implements IModel<Bytes> {

  private static final long serialVersionUID = -4537289783663569276L;

  private final IModel<Long> wrappedModel;

  private final BytesUnit unit;

  public static BytesModel ofLong(IModel<Long> wrappedModel, BytesUnit unit) {
    return new BytesModel(wrappedModel, unit);
  }

  private BytesModel(IModel<Long> wrappedModel, BytesUnit unit) {
    if (wrappedModel == null || unit == null) {
      throw new IllegalArgumentException("model and unit cannot be null");
    }
    this.wrappedModel = wrappedModel;
    this.unit = unit;
  }

  @Override
  public Bytes getObject() {
    Long longValue = wrappedModel.getObject();
    return longValue == null ? null : unit.fromLong(longValue);
  }

  @Override
  public void detach() {
    IModel.super.detach();
    wrappedModel.detach();
  }
}

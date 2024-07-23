package org.iglooproject.wicket.more.markup.html.model;

import java.math.BigDecimal;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Utilitaire pour wrapper un IModel<Float> pour des raisons de compatibilité. Dans le cas général,
 * il est conseillé d'utiliser plutôt des BigDecimal avec les pourcentages.
 */
public class PercentageFloatToBigDecimalModel extends LoadableDetachableModel<BigDecimal> {

  private static final long serialVersionUID = 3511276823849427019L;

  private final IModel<Float> floatModel;

  protected PercentageFloatToBigDecimalModel(IModel<Float> floatModel) {
    super();
    this.floatModel = floatModel;
  }

  @Override
  protected BigDecimal load() {
    if (floatModel.getObject() == null) {
      return null;
    } else {
      return BigDecimal.valueOf(floatModel.getObject().doubleValue());
    }
  }

  @Override
  public void detach() {
    floatModel.detach();
    super.detach();
  }

  @Override
  public void setObject(BigDecimal object) {
    if (object == null) {
      floatModel.setObject(null);
    } else {
      floatModel.setObject(object.floatValue());
    }
    super.setObject(object);
  }

  public static final PercentageFloatToBigDecimalModel of(IModel<Float> floatModel) {
    return new PercentageFloatToBigDecimalModel(floatModel);
  }
}

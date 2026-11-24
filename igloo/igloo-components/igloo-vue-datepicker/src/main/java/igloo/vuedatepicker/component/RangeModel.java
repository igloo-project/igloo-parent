package igloo.vuedatepicker.component;

import igloo.wicket.model.Detachables;
import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

/**
 * Range edited by a range field, made of two models: start and end.
 *
 * <p>The range is {@code null} when both models are empty. A range with a single value is kept as
 * is, with a {@code null} start or end: the datepicker displays it empty, as it cannot display it,
 * and Wicket sees the difference with the empty input submitted, so it empties both models. What is
 * saved is what the user saw.
 */
final class RangeModel<T> implements IModel<Pair<T, T>> {

  private static final long serialVersionUID = 1L;

  private final IModel<T> startModel;

  private final IModel<T> endModel;

  RangeModel(IModel<T> startModel, IModel<T> endModel) {
    this.startModel = startModel;
    this.endModel = endModel;
  }

  @Override
  public Pair<T, T> getObject() {
    T start = startModel.getObject();
    T end = endModel.getObject();
    return start != null || end != null ? Pair.with(start, end) : null;
  }

  @Override
  public void setObject(Pair<T, T> range) {
    startModel.setObject(range == null ? null : range.getValue0());
    endModel.setObject(range == null ? null : range.getValue1());
  }

  @Override
  public void detach() {
    Detachables.detach(startModel, endModel);
  }
}

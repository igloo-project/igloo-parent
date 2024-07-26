package org.iglooproject.wicket.more.markup.html.model;

import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ListItemModel<T> implements IModel<T> {

  private static final long serialVersionUID = -7747826759295752059L;

  private final IModel<? extends List<T>> listModel;

  private final IModel<Integer> indexModel;

  public ListItemModel(IModel<? extends List<T>> listModel, int index) {
    this(listModel, Model.of(index));
  }

  public ListItemModel(IModel<? extends List<T>> listModel, IModel<Integer> indexModel) {
    super();
    this.listModel = listModel;
    this.indexModel = indexModel;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(listModel).append(indexModel).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ListItemModel)) {
      return false;
    }
    ListItemModel<?> other = (ListItemModel<?>) obj;
    return new EqualsBuilder()
        .append(listModel, other.listModel)
        .append(indexModel, other.indexModel)
        .build();
  }

  @Override
  public T getObject() {
    return listModel.getObject().get(indexModel.getObject());
  }

  @Override
  public void setObject(T object) {
    listModel.getObject().set(indexModel.getObject(), object);
  }

  @Override
  public void detach() {
    listModel.detach();
    indexModel.detach();
  }

  public IModel<? extends List<T>> getListModel() {
    return listModel;
  }

  public IModel<Integer> getIndexModel() {
    return indexModel;
  }
}

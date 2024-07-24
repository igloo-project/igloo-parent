package org.iglooproject.wicket.more.markup.html.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.lang.Bytes;

public class BytesToLongModel extends LoadableDetachableModel<Long> {

  private static final long serialVersionUID = 3511276823849427019L;

  private final IModel<Bytes> bytesModel;

  protected BytesToLongModel(IModel<Bytes> bytesModel) {
    super();
    this.bytesModel = bytesModel;
  }

  @Override
  protected Long load() {
    if (bytesModel.getObject() == null) {
      return null;
    } else {
      return Long.valueOf(bytesModel.getObject().bytes());
    }
  }

  @Override
  public void detach() {
    bytesModel.detach();
    super.detach();
  }

  @Override
  public void setObject(Long object) {
    if (object == null) {
      bytesModel.setObject(null);
    } else {
      bytesModel.setObject(Bytes.bytes(object));
    }
    super.setObject(object);
  }

  public static final BytesToLongModel of(IModel<Bytes> bytesModel) {
    return new BytesToLongModel(bytesModel);
  }
}

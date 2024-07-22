package igloo.wicket.markup.html.list;

import java.util.List;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

public abstract class OddEvenListView<T> extends ListView<T> {

  private static final long serialVersionUID = -7408299272975588957L;

  public OddEvenListView(final String id, final IModel<? extends List<T>> model) {
    super(id, model);
  }

  @Override
  protected ListItem<T> newItem(final int index, IModel<T> itemModel) {
    return new OddEvenListItem<>(index, itemModel);
  }
}

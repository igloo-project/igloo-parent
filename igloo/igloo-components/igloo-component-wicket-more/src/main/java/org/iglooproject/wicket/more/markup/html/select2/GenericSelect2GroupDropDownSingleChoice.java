package org.iglooproject.wicket.more.markup.html.select2;

import java.util.List;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

public abstract class GenericSelect2GroupDropDownSingleChoice<T>
    extends GenericSelect2DropDownSingleChoice<T> {

  private static final long serialVersionUID = 8581582499159251418L;

  private T last;

  private boolean grouped = true;

  protected GenericSelect2GroupDropDownSingleChoice(
      String id,
      IModel<T> model,
      IModel<? extends List<? extends T>> choicesModel,
      IChoiceRenderer<? super T> renderer) {
    super(id, model, choicesModel, renderer);
  }

  private boolean isLast(int index) {
    return index - 1 == getChoices().size();
  }

  private boolean isFirst(int index) {
    return index == 0;
  }

  protected abstract boolean isNewGroup(T current, T last);

  protected abstract String getGroupLabel(T current);

  @Override
  protected void appendOptionHtml(
      AppendingStringBuffer buffer, T choice, int index, String selected) {
    if (grouped) {
      if (last == null || isNewGroup(choice, last)) {
        if (!isFirst(index)) {
          buffer.append("</optgroup>");
        }
        buffer.append("<optgroup label='");
        buffer.append(Strings.escapeMarkup(getGroupLabel(choice)));
        buffer.append("'>");
      }
      super.appendOptionHtml(buffer, choice, index, selected);
      if (isLast(index)) {
        buffer.append("</optgroup>");
      }
      last = choice;
    } else {
      super.appendOptionHtml(buffer, choice, index, selected);
    }
  }

  @Override
  protected void onAfterRender() {
    super.onAfterRender();
    last = null;
  }

  public boolean isGrouped() {
    return grouped;
  }

  public void setGrouped(boolean grouped) {
    this.grouped = grouped;
  }
}

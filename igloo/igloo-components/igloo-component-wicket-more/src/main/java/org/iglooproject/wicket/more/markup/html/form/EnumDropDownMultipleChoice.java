package org.iglooproject.wicket.more.markup.html.form;

import java.util.Collection;
import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

public class EnumDropDownMultipleChoice<E extends Enum<E>>
    extends GenericSelect2DropDownMultipleChoice<E> {

  private static final long serialVersionUID = 6244269987751271782L;

  public <C extends Collection<E>> EnumDropDownMultipleChoice(
      String id,
      IModel<C> collectionModel,
      SerializableSupplier2<? extends C> collectionSupplier,
      Class<E> clazz) {
    this(id, collectionModel, collectionSupplier, Model.ofList(EnumUtils.getEnumList(clazz)));
  }

  public <C extends Collection<E>> EnumDropDownMultipleChoice(
      String id,
      IModel<C> collectionModel,
      SerializableSupplier2<? extends C> collectionSupplier,
      IModel<? extends Collection<? extends E>> choicesModel) {
    this(id, collectionModel, collectionSupplier, choicesModel, new EnumChoiceRenderer<E>());
  }

  public <C extends Collection<E>> EnumDropDownMultipleChoice(
      String id,
      IModel<C> collectionModel,
      SerializableSupplier2<? extends C> collectionSupplier,
      IModel<? extends Collection<? extends E>> choicesModel,
      IChoiceRenderer<? super E> renderer) {
    super(id, collectionModel, collectionSupplier, choicesModel, renderer);
  }
}

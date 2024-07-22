package org.iglooproject.wicket.more.markup.html.template.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.wicket.model.IModel;

public class BreadCrumbElementListConcatModel implements IModel<List<BreadCrumbElement>> {

  private static final long serialVersionUID = -4163053491976956557L;

  private final IModel<List<BreadCrumbElement>> prependedListModel;
  private final IModel<List<BreadCrumbElement>> appendedListModel;
  private final int numberOfElementsToSubstract;

  public BreadCrumbElementListConcatModel(
      IModel<List<BreadCrumbElement>> prependedListModel,
      IModel<List<BreadCrumbElement>> appendedListModel) {
    this(prependedListModel, appendedListModel, 0);
  }

  public BreadCrumbElementListConcatModel(
      IModel<List<BreadCrumbElement>> prependedListModel,
      IModel<List<BreadCrumbElement>> appendedListModel,
      int numberOfElementsToSubstract) {
    super();
    this.prependedListModel = prependedListModel;
    this.appendedListModel = appendedListModel;
    this.numberOfElementsToSubstract = numberOfElementsToSubstract;
  }

  @Override
  public List<BreadCrumbElement> getObject() {
    List<BreadCrumbElement> prependedList = prependedListModel.getObject();
    List<BreadCrumbElement> appendedList = appendedListModel.getObject();

    ImmutableList.Builder<BreadCrumbElement> builder = ImmutableList.builder();
    builder.addAll(prependedList);

    if (numberOfElementsToSubstract > 0) {
      builder.addAll(
          appendedList.subList(0, Math.max(0, appendedList.size() - numberOfElementsToSubstract)));
    } else {
      builder.addAll(appendedList);
    }

    return builder.build();
  }
}

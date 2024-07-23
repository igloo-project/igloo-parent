package org.iglooproject.wicket.more.link.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.iglooproject.spring.util.StringUtils;

public class AnchorNameModel extends LoadableDetachableModel<String> {

  private static final long serialVersionUID = 1L;

  public static IModel<String> id(String unencodedAnchorName) {
    return new AnchorNameModel(unencodedAnchorName, AnchorType.ID);
  }

  public static IModel<String> id(IModel<String> unencodedAnchorNameModel) {
    return new AnchorNameModel(unencodedAnchorNameModel, AnchorType.ID);
  }

  public static IModel<String> href(String unencodedAnchorName) {
    return new AnchorNameModel(unencodedAnchorName, AnchorType.HREF);
  }

  public static IModel<String> href(IModel<String> unencodedAnchorNameModel) {
    return new AnchorNameModel(unencodedAnchorNameModel, AnchorType.HREF);
  }

  public static enum AnchorType {
    HREF,
    ID;
  }

  private final IModel<String> unencodedAnchorNameModel;

  private final AnchorType anchorType;

  public AnchorNameModel(String unencodedAnchorName, AnchorType anchorType) {
    this(Model.of(unencodedAnchorName), anchorType);
  }

  public AnchorNameModel(IModel<String> unencodedAnchorNameModel, AnchorType anchorType) {
    super();
    this.unencodedAnchorNameModel = unencodedAnchorNameModel;
    this.anchorType = anchorType;
  }

  @Override
  protected String load() {
    StringBuilder builder = new StringBuilder();
    if (AnchorType.HREF.equals(anchorType)) {
      builder.append("#");
    }
    builder.append(StringUtils.urlize(unencodedAnchorNameModel.getObject()));
    return builder.toString();
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    if (unencodedAnchorNameModel != null) {
      unencodedAnchorNameModel.detach();
    }
  }
}

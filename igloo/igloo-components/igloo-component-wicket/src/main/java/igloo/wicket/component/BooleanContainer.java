package igloo.wicket.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BooleanContainer extends WebMarkupContainer {

  private static final long serialVersionUID = 7764638101080488816L;

  private boolean positive = false;

  public BooleanContainer(String id, IModel<Boolean> model) {
    this(id, model, true);
  }

  public BooleanContainer(String id, IModel<Boolean> model, boolean positive) {
    super(id, model);

    this.positive = positive;
  }

  @Override
  protected void onConfigure() {
    super.onConfigure();

    Boolean booleanValue = (Boolean) getDefaultModelObject();

    setVisible(Boolean.valueOf(positive).equals(booleanValue));
  }
}

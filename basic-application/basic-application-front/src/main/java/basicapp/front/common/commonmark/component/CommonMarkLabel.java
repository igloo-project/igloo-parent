package basicapp.front.common.commonmark.component;

import basicapp.front.common.commonmark.renderer.CommonMarkRenderer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class CommonMarkLabel extends Label {

  private static final long serialVersionUID = 1L;

  public CommonMarkLabel(String id, IModel<String> model) {
    super(id, CommonMarkRenderer.get().asModel(model));
    setEscapeModelStrings(false);
  }
}

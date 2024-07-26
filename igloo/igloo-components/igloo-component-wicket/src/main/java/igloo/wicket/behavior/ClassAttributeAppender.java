package igloo.wicket.behavior;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ClassAttributeAppender extends AttributeAppender {

  private static final long serialVersionUID = -1051427671590955890L;

  private static final String CLASS_ATTRIBUTE = "class";
  private static final String SEPARATOR = " ";

  public ClassAttributeAppender(IModel<?> appendModel) {
    super(CLASS_ATTRIBUTE, appendModel, SEPARATOR);
  }

  public ClassAttributeAppender(String appendString) {
    super(CLASS_ATTRIBUTE, Model.of(appendString), SEPARATOR);
  }
}

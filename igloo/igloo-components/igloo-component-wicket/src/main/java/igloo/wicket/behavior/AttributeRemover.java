package igloo.wicket.behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.value.IValueMap;

public class AttributeRemover extends Behavior {
  private static final long serialVersionUID = -5358213150482831485L;

  public static final String VALUELESS_ATTRIBUTE = "VA";

  private static final String DEFAULT_ATTRIBUTE_SEPARATOR = " ";

  private final String attribute;

  private final IModel<?> removeModel;

  private final String separator;

  public AttributeRemover(final String attribute, final IModel<?> removeModel) {
    this(attribute, removeModel, DEFAULT_ATTRIBUTE_SEPARATOR);
  }

  public AttributeRemover(final String attribute, final IModel<?> removeModel, String separator) {
    super();

    Args.notNull(attribute, "attribute");

    this.attribute = attribute;
    this.removeModel = removeModel;
    this.separator = separator;
  }

  @Override
  public final void onComponentTag(Component component, ComponentTag tag) {
    if (tag.getType() != TagType.CLOSE) {
      replaceAttributeValue(tag);
    }
  }

  private void replaceAttributeValue(ComponentTag tag) {
    if (removeModel != null && removeModel.getObject() != VALUELESS_ATTRIBUTE) {
      final IValueMap tagAttributes = tag.getAttributes();
      Object attributeValue = tagAttributes.get(attribute);

      List<String> valuesToRemove = getClassesToRemove();

      if (attributeValue != null && !valuesToRemove.isEmpty()) {
        List<String> values = Arrays.asList(attributeValue.toString().split(separator));

        StringBuilder newAttributeValue = new StringBuilder();
        for (String value : values) {
          if (!valuesToRemove.contains(value)) {
            newAttributeValue.append(value).append(separator);
          }
        }

        tagAttributes.put(attribute, newAttributeValue);
      }
    }
  }

  private List<String> getClassesToRemove() {
    if (removeModel != null && removeModel.getObject() != null) {
      return Arrays.asList(removeModel.getObject().toString().split(separator));
    }
    return new ArrayList<>();
  }

  @Override
  public String toString() {
    return "[AttributeRemover attribute=" + attribute + ", removeModel=" + removeModel + "]";
  }

  @Override
  public void detach(Component component) {
    super.detach(component);
    if (removeModel != null) {
      removeModel.detach();
    }
  }
}

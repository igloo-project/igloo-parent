package igloo.wicket.markup.html.link;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

public class EmailLink extends AbstractLink {

  private static final long serialVersionUID = 6275214385143438381L;

  private static final String MAILTO_PREFIX = "mailto:";

  public EmailLink(String id, IModel<String> emailAddressModel) {
    this(id, emailAddressModel, emailAddressModel);
  }

  public EmailLink(String id, IModel<String> emailAddressModel, IModel<String> bodyModel) {
    super(id, emailAddressModel);
    setBody(bodyModel);
  }

  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);

    if (isEnabledInHierarchy() == false) {
      disableLink(tag);
    } else if (getDefaultModel() != null) {
      Object hrefValue = getDefaultModelObject();
      if (hrefValue != null) {
        String url = MAILTO_PREFIX + hrefValue.toString();

        // if the tag is an anchor proper
        if (tag.getName().equalsIgnoreCase("a")
            || tag.getName().equalsIgnoreCase("link")
            || tag.getName().equalsIgnoreCase("area")) {
          // generate the href attribute
          tag.put("href", url);
        }
      }
    }
  }
}

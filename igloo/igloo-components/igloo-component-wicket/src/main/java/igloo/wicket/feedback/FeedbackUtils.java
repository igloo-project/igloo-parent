package igloo.wicket.feedback;

import com.google.common.collect.ImmutableMap;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public final class FeedbackUtils {

  public static void refreshFeedback(AjaxRequestTarget target, Page page) {
    target.addChildren(page, IFeedbackPanel.class);
  }

  public static String createFeedbackMessageWithLink(
      Component component, String resourceKey, String fullUrl, String linkBodyResourceKey) {
    String link =
        "<a href="
            + JsUtils.doubleQuotes(fullUrl, true)
            + " onclick=\"if (event.stopPropagation) event.stopPropagation() ; else event.cancelBubble = true;\">"
            + component.getString(linkBodyResourceKey)
            + "</a>";
    return component.getString(resourceKey, Model.ofMap(ImmutableMap.of("link", link)));
  }

  private FeedbackUtils() {}
}

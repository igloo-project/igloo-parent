package test.wicket.more.link.descriptor;

import java.util.Objects;
import org.apache.wicket.request.Url;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.test.context.ContextConfiguration;
import test.wicket.more.AbstractWicketMoreTestCase;
import test.wicket.more.link.descriptor.config.spring.WicketMoreTestLinkDescriptorWebappConfig;

@ContextConfiguration(
    inheritLocations = false,
    classes = WicketMoreTestLinkDescriptorWebappConfig.class)
public abstract class AbstractTestLinkDescriptor extends AbstractWicketMoreTestCase {

  protected static Matcher<String> hasPathAndQuery(String expectedPathRelativeToServlet) {
    final String cleanedExpectedPathRelativeToServlet =
        getPathAndQuery(expectedPathRelativeToServlet);
    return new TypeSafeMatcher<String>() {
      @Override
      public void describeTo(Description description) {
        description
            .appendText("a URL with a path matching ")
            .appendValue(cleanedExpectedPathRelativeToServlet);
      }

      @Override
      protected void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("got ");
        try {
          mismatchDescription.appendValue(getPathAndQuery(item));
        } catch (RuntimeException e) {
          mismatchDescription.appendValue(e);
        }
      }

      @Override
      protected boolean matchesSafely(String item) {
        try {
          return Objects.equals(cleanedExpectedPathRelativeToServlet, getPathAndQuery(item));
        } catch (RuntimeException e) {
          return false;
        }
      }
    };
  }

  private static String getPathAndQuery(String item) {
    Url url = Url.parse(item);
    String path = StringUtils.trimLeadingCharacter(url.getPath(), '.');
    String query = url.getQueryString();
    if (query == null) {
      query = "";
    } else {
      query = "?" + query;
    }
    return path + query;
  }
}

package basicapp.front.common.commonmark.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.iglooproject.spring.util.StringUtils;

public class CommonMarkRenderer extends Renderer<String> {

  private static final long serialVersionUID = 1L;

  private static final CommonMarkRenderer INSTANCE = new CommonMarkRenderer();

  private CommonMarkRenderer() {
    super();
  }

  @Override
  public String render(String value, Locale locale) {
    if (!StringUtils.hasText(value)) {
      return null;
    }

    return HtmlRenderer.builder()
        .escapeHtml(true)
        .sanitizeUrls(true)
        .softbreak("<br>")
        .attributeProviderFactory(context -> new ImageAttributeProvider())
        .build()
        .render(Parser.builder().build().parse(value));
  }

  public static final CommonMarkRenderer get() {
    return INSTANCE;
  }
}

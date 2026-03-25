package basicapp.front.common.commonmark.renderer;

import java.util.Map;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

public class ImageAttributeProvider implements AttributeProvider {

  @Override
  public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
    if (node instanceof Image) {
      attributes.put("class", "img-fluid");
    }
  }
}

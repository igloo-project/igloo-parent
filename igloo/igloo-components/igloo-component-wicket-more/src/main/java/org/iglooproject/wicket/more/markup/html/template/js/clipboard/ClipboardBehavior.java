package org.iglooproject.wicket.more.markup.html.template.js.clipboard;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.spring.util.StringUtils;

public class ClipboardBehavior extends Behavior {

  private static final long serialVersionUID = 3323980221810150352L;

  private static final String DATA_CLIPBOARD = "data-clipboard";
  private static final String DATA_CLIPBOARD_TARGET = "data-clipboard-target";
  private static final String DATA_CLIPBOARD_ACTION = "data-clipboard-action";
  private static final String DATA_CLIPBOARD_TEXT = "data-clipboard-text";

  private Map<String, IModel<String>> dataAttributesValues =
      Maps.newHashMap(
          ImmutableMap.<String, IModel<String>>builder()
              .put(DATA_CLIPBOARD_TARGET, Model.of())
              .put(DATA_CLIPBOARD_ACTION, Model.of())
              .put(DATA_CLIPBOARD_TEXT, Model.of())
              .build());

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    response.render(
        JavaScriptReferenceHeaderItem.forReference(ClipboardJavaScriptResourceReference.get()));
    response.render(OnDomReadyHeaderItem.forScript("new ClipboardJS('[" + DATA_CLIPBOARD + "]')"));
  }

  @Override
  public void onComponentTag(Component component, ComponentTag tag) {
    tag.append(DATA_CLIPBOARD, "", " ");

    dataAttributesValues.forEach(
        (k, v) -> {
          if (StringUtils.hasText(v.getObject())) {
            tag.append(k, v.getObject(), " ");
          }
        });

    super.onComponentTag(component, tag);
  }

  public ClipboardBehavior target(Component component) {
    return target(
        new IModel<String>() {
          private static final long serialVersionUID = 1L;

          @Override
          public String getObject() {
            return component != null ? "#" + component.getMarkupId() : null;
          }
        });
  }

  public ClipboardBehavior target(String target) {
    return target(Model.of(target));
  }

  public ClipboardBehavior target(IModel<String> targetModel) {
    dataAttributesValues.put(DATA_CLIPBOARD_TARGET, Objects.requireNonNull(targetModel));
    return this;
  }

  public ClipboardBehavior copy() {
    return action("copy");
  }

  public ClipboardBehavior cut() {
    return action("cut");
  }

  public ClipboardBehavior action(String action) {
    return action(Model.of(action));
  }

  public ClipboardBehavior action(IModel<String> actionModel) {
    dataAttributesValues.put(DATA_CLIPBOARD_ACTION, Objects.requireNonNull(actionModel));
    return this;
  }

  public ClipboardBehavior text(String text) {
    return text(Model.of(text));
  }

  public ClipboardBehavior text(IModel<String> textModel) {
    dataAttributesValues.put(DATA_CLIPBOARD_TEXT, Objects.requireNonNull(textModel));
    return this;
  }
}

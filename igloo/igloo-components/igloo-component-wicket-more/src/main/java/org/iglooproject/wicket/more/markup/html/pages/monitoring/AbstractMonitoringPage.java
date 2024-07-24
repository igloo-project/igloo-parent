package org.iglooproject.wicket.more.markup.html.pages.monitoring;

import igloo.wicket.component.CoreLabel;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.Strings;

public abstract class AbstractMonitoringPage extends Page {

  private static final long serialVersionUID = -2327468306332270500L;

  public static final String TEXT_MIME = "text/plain";

  public static final MarkupType TEXT_PLAIN_MARKUP_TYPE = new MarkupType("txt", TEXT_MIME);

  private boolean success;

  private List<String> details = new ArrayList<>();

  private String message;

  public AbstractMonitoringPage() {
    super();
  }

  @Override
  public void onInitialize() {
    super.onInitialize();
    add(new CoreLabel("status", isSuccess() ? "OK" : "KO").setEscapeModelStrings(false));

    add(
        new ListView<String>("details", getDetails()) {

          private static final long serialVersionUID = 1998240269123369862L;

          @Override
          protected void populateItem(ListItem<String> item) {
            item.add(
                new CoreLabel("detail", item.getModelObject().replaceAll("\\|", "<pipe>"))
                    .setEscapeModelStrings(false));

            Label separator = new CoreLabel("separator", " | ");
            separator.setVisible(item.getIndex() != getList().size() - 1);
            item.add(separator);
          }

          @Override
          protected void onConfigure() {
            super.onConfigure();
            setVisible(!getDetails().isEmpty());
          }
        });

    add(
        new CoreLabel("message", Model.of(getMessage()))
            .hideIfEmpty()
            .setEscapeModelStrings(false));
  }

  @Override
  public MarkupType getMarkupType() {
    return TEXT_PLAIN_MARKUP_TYPE;
  }

  @Override
  protected void onRender() {
    final String encoding = getApplication().getRequestCycleSettings().getResponseRequestEncoding();
    final boolean validEncoding = (Strings.isEmpty(encoding) == false);
    final String contentType;
    if (validEncoding) {
      contentType = getMarkupType().getMimeType() + "; charset=" + encoding;
    } else {
      contentType = getMarkupType().getMimeType();
    }

    ((WebResponse) RequestCycle.get().getResponse()).setContentType(contentType);

    super.onRender();
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  public List<String> getDetails() {
    return details;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

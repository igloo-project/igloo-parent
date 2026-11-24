package igloo.vuedatepicker.test;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.danekja.java.util.function.serializable.SerializableFunction;

/** Page with a form holding a single datepicker, with id {@link #FIELD_ID}, and its label. */
public class DatePickerTestPage extends WebPage implements IMarkupResourceStreamProvider {

  private static final long serialVersionUID = 1L;

  public static final String FORM_ID = "form";

  public static final String FIELD_ID = "field";

  public static final String FIELD_PATH = FORM_ID + ":" + FIELD_ID;

  private final boolean selfClosingTag;

  public DatePickerTestPage(SerializableFunction<String, FormComponent<?>> fieldFactory) {
    this(fieldFactory, false);
  }

  public DatePickerTestPage(
      SerializableFunction<String, FormComponent<?>> fieldFactory, boolean selfClosingTag) {
    this.selfClosingTag = selfClosingTag;
    add(new Form<Void>(FORM_ID).add(fieldFactory.apply(FIELD_ID)));
  }

  @Override
  public IResourceStream getMarkupResourceStream(
      MarkupContainer container, Class<?> containerClass) {
    String fieldTag =
        selfClosingTag
            ? "<vue-date-picker wicket:id=\"field\" />"
            : "<vue-date-picker wicket:id=\"field\"></vue-date-picker>";
    return new StringResourceStream(
        """
        <html>
          <head></head>
          <body>
            <form wicket:id="form">
              <label wicket:for="field">Date</label>
              %s
              <span id="after-field"></span>
            </form>
          </body>
        </html>
        """
            .formatted(fieldTag));
  }
}

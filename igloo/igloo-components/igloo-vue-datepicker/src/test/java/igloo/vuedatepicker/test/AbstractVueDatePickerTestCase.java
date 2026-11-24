package igloo.vuedatepicker.test;

import static org.mockito.Mockito.mock;

import de.agilecoders.wicket.webjars.WicketWebjars;
import java.util.Locale;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.iglooproject.spring.property.service.IPropertyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Wicket application able to render the datepickers: Spring injection of {@link IPropertyService},
 * webjars, and the resource keys an igloo application provides.
 */
public abstract class AbstractVueDatePickerTestCase {

  protected static final Map<String, String> RESOURCES =
      Map.ofEntries(
          Map.entry("datetime.pattern.date", "dd/MM/yyyy"),
          Map.entry("datetime.pattern.date.placeholder", "jj/mm/aaaa"),
          Map.entry("datetime.pattern.date.range.placeholder", "jj/mm/aaaa - jj/mm/aaaa"),
          Map.entry("datetime.pattern.dateTime", "dd/MM/yyyy HH:mm"),
          Map.entry("datetime.pattern.dateTime.placeholder", "jj/mm/aaaa, hh:mm"),
          Map.entry(
              "datetime.pattern.dateTime.range.placeholder",
              "jj/mm/aaaa, hh:mm - jj/mm/aaaa, hh:mm"),
          Map.entry("datetime.pattern.time", "HH:mm"),
          Map.entry("datetime.pattern.time.placeholder", "hh:mm"),
          Map.entry("datetime.pattern.yearMonth", "MM/yyyy"),
          Map.entry("datetime.pattern.yearMonth.placeholder", "mm/aaaa"),
          Map.entry("datetime.pattern.year", "yyyy"),
          Map.entry("datetime.pattern.year.placeholder", "aaaa"),
          Map.entry("datepicker.wording.select", "Select"),
          Map.entry("datepicker.wording.cancel", "Cancel"),
          Map.entry("datepicker.aria.label.incrementValue.hours", "Increment l'heure"),
          Map.entry("conversion.error.localDate", "invalid localDate"),
          Map.entry("conversion.error.localDate.range", "invalid localDate range"),
          Map.entry("conversion.error.localDateTime", "invalid localDateTime"),
          Map.entry("conversion.error.localDateTime.range", "invalid localDateTime range"),
          Map.entry("conversion.error.localTime", "invalid localTime"),
          Map.entry("conversion.error.yearMonth", "invalid yearMonth"),
          Map.entry("conversion.error.year", "invalid year"));

  protected WicketTester tester;

  protected IPropertyService propertyService;

  @BeforeEach
  void setUpTester() {
    propertyService = mock(IPropertyService.class);
    ApplicationContextMock applicationContext = new ApplicationContextMock();
    applicationContext.putBean(propertyService);

    tester =
        new WicketTester(
            new MockApplication() {
              @Override
              protected void init() {
                super.init();
                WicketWebjars.install(this);
                getComponentInstantiationListeners()
                    .add(new SpringComponentInjector(this, applicationContext));
                getResourceSettings().getStringResourceLoaders().addFirst(new MapResourceLoader());
                // other keys (aria labels...) only need a value
                getResourceSettings().setThrowExceptionOnMissingResource(false);
              }
            });
  }

  @AfterEach
  void tearDownTester() {
    tester.destroy();
  }

  /** Renders {@link DatePickerTestPage} with the given field. */
  protected FormComponent<?> render(SerializableFunction<String, FormComponent<?>> fieldFactory) {
    tester.startPage(new DatePickerTestPage(fieldFactory));
    return field();
  }

  /** Renders the field, then submits the form with {@code input} typed in the field. */
  protected FormComponent<?> submit(
      SerializableFunction<String, FormComponent<?>> fieldFactory, String input) {
    render(fieldFactory);
    FormTester form = tester.newFormTester(DatePickerTestPage.FORM_ID);
    form.setValue(DatePickerTestPage.FIELD_ID, input);
    form.submit();
    return field();
  }

  protected FormComponent<?> field() {
    return (FormComponent<?>)
        tester.getComponentFromLastRenderedPage(DatePickerTestPage.FIELD_PATH);
  }

  private static final class MapResourceLoader implements IStringResourceLoader {

    @Override
    public String loadStringResource(
        Class<?> clazz, String key, Locale locale, String style, String variation) {
      return RESOURCES.get(key);
    }

    @Override
    public String loadStringResource(
        Component component, String key, Locale locale, String style, String variation) {
      return RESOURCES.get(key);
    }
  }
}

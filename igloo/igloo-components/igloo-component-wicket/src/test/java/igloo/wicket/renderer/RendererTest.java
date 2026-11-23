package igloo.wicket.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.wicket.Component;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Renderer#getStringOptional(String, Locale, IModel)}. */
class RendererTest {

  private static final String SIMPLE_KEY = "renderer.test.simple";
  private static final String EMPTY_KEY = "renderer.test.empty";
  private static final String MODEL_KEY = "renderer.test.model";
  private static final String LOCALIZED_KEY = "renderer.test.localized";
  private static final String MISSING_KEY = "renderer.test.missing";

  /** Resources are keyed by resource key, optionally suffixed by "|language". */
  private static final Map<String, String> RESOURCES =
      Map.of(
          SIMPLE_KEY,
          "simple value",
          EMPTY_KEY,
          "",
          MODEL_KEY,
          "Hello ${name}",
          LOCALIZED_KEY + "|en",
          "english value",
          LOCALIZED_KEY + "|fr",
          "valeur française");

  private WicketTester tester;

  @BeforeEach
  void setUp() {
    tester =
        new WicketTester(
            new MockApplication() {
              @Override
              protected void init() {
                super.init();
                getResourceSettings().getStringResourceLoaders().add(new MapStringResourceLoader());
              }
            });
  }

  @AfterEach
  void tearDown() {
    tester.destroy();
  }

  @Test
  void getStringOptional_existingKey_returnsValue() {
    Optional<String> result = Renderer.getStringOptional(SIMPLE_KEY, Locale.ENGLISH);

    assertThat(result).contains("simple value");
  }

  @Test
  void getStringOptional_missingKey_returnsEmpty() {
    Optional<String> result = Renderer.getStringOptional(MISSING_KEY, Locale.ENGLISH);

    assertThat(result).isEmpty();
  }

  @Test
  void getStringOptional_missingKeyWithModel_returnsEmpty() {
    Optional<String> result =
        Renderer.getStringOptional(MISSING_KEY, Locale.ENGLISH, Model.of("ignored"));

    assertThat(result).isEmpty();
  }

  @Test
  void getStringOptional_emptyValue_returnsPresentEmptyString() {
    // an existing key with an empty value must not be mistaken for a missing key
    Optional<String> result = Renderer.getStringOptional(EMPTY_KEY, Locale.ENGLISH);

    assertThat(result).contains("");
  }

  @Test
  void getStringOptional_localizedKey_usesGivenLocale() {
    assertThat(Renderer.getStringOptional(LOCALIZED_KEY, Locale.ENGLISH)).contains("english value");
    assertThat(Renderer.getStringOptional(LOCALIZED_KEY, Locale.FRENCH))
        .contains("valeur française");
  }

  @Test
  void getStringOptional_withModel_substitutesPropertyExpressions() {
    IModel<Map<String, String>> model = Model.ofMap(Map.of("name", "World"));

    Optional<String> result = Renderer.getStringOptional(MODEL_KEY, Locale.ENGLISH, model);

    assertThat(result).contains("Hello World");
  }

  @Test
  void getStringOptional_withoutModel_keepsPropertyExpressions() {
    Optional<String> result = Renderer.getStringOptional(MODEL_KEY, Locale.ENGLISH);

    assertThat(result).contains("Hello ${name}");
  }

  private static class MapStringResourceLoader implements IStringResourceLoader {

    @Override
    public String loadStringResource(
        Class<?> clazz, String key, Locale locale, String style, String variation) {
      if (locale != null) {
        String localized = RESOURCES.get(key + "|" + locale.getLanguage());
        if (localized != null) {
          return localized;
        }
      }
      return RESOURCES.get(key);
    }

    @Override
    public String loadStringResource(
        Component component, String key, Locale locale, String style, String variation) {
      return loadStringResource((Class<?>) null, key, locale, style, variation);
    }
  }
}

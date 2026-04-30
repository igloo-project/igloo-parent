package test;

import static org.assertj.core.api.Assertions.assertThat;

import igloo.juice.JuiceInliner;
import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Vérifie le comportement par défaut de {@code IJuiceInliner.objectMapper()} : les valeurs nulles
 * doivent être exclues de la sérialisation. Compile à l'identique sur Jackson 2 et Jackson 3 grâce
 * à l'usage de {@code var} (l'import de {@code ObjectMapper} n'est pas nécessaire ici).
 */
class TestIJuiceInlinerObjectMapper {

  public record SamplePayload(String kept, String dropped) {}

  @Test
  void defaultObjectMapperExcludesNullPojoProperties() throws Exception {
    var inliner = JuiceInliner.builder().inlinerUrl(URI.create("http://localhost").toURL()).build();
    var mapper = inliner.objectMapper();

    SamplePayload payload = new SamplePayload("value", null);

    String json = mapper.writeValueAsString(payload);

    assertThat(json).contains("\"kept\":\"value\"").doesNotContain("dropped");
  }

  @Test
  void defaultObjectMapperEmitsNonNullObjects() throws Exception {
    var inliner = JuiceInliner.builder().inlinerUrl(URI.create("http://localhost").toURL()).build();
    var mapper = inliner.objectMapper();

    Map<String, Object> payload = Map.of("a", 1, "b", "two");

    String json = mapper.writeValueAsString(payload);

    assertThat(json).contains("\"a\":1").contains("\"b\":\"two\"");
  }
}

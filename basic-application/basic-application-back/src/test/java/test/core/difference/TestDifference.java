package test.core.difference;

import basicapp.back.config.BasicApplicationBackDifferenceConfiguration;
import com.google.common.io.Resources;
import igloo.difference.model.DifferenceFields;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDifference {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestDifference.class);

  @Test
  void testUser() {
    testDifference("User", BasicApplicationBackDifferenceConfiguration.userFields());
  }

  private void testDifference(String name, DifferenceFields fields) {
    String fieldsPlainString = fields.toPlainString();
    Assertions.assertThat(
            new ByteArrayInputStream(fieldsPlainString.getBytes(StandardCharsets.UTF_8)))
        .as(
            """
        Difference introspection model changed from reference;
        update introspection configuration or update %s.txt file with content:
        %s"""
                .formatted(name, fieldsPlainString))
        .hasSameContentAs(loadFields(name));
  }

  private ByteArrayInputStream loadFields(String name) {
    String resourceName = "/difference/%s.txt".formatted(name);
    try {
      URL resource = Resources.getResource(TestDifference.class, resourceName);
      return new ByteArrayInputStream(Resources.toByteArray(resource));
    } catch (RuntimeException | IOException e) {
      LOGGER.warn("File {} not found", name);
      return new ByteArrayInputStream(null);
    }
  }
}

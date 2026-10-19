package test.core.business.common;

import basicapp.back.business.common.model.atomic.BasicApplicationMediaType;
import basicapp.back.config.BasicApplicationBackMediaTypeConfiguration;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.iglooproject.commons.util.mime.IMediaTypeRegistry;
import org.iglooproject.commons.util.mime.MediaType;
import org.junit.jupiter.api.Test;

class TestBasicApplicationMediaType {

  @Test
  void mediaTypeRegistry() {
    IMediaTypeRegistry registry =
        new BasicApplicationBackMediaTypeConfiguration().mediaTypeRegistry();

    Assertions.assertThat(registry.values())
        .containsAll(List.of(MediaType.values()))
        .containsAll(List.of(BasicApplicationMediaType.values()));
    Assertions.assertThat(registry.fromExtension("pdf")).isNotNull();
  }
}

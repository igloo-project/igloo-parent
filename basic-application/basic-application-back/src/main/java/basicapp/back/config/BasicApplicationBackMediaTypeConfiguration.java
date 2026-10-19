package basicapp.back.config;

import basicapp.back.business.common.model.atomic.BasicApplicationMediaType;
import org.iglooproject.commons.util.mime.IMediaTypeRegistry;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationBackMediaTypeConfiguration {

  @Bean
  public IMediaTypeRegistry mediaTypeRegistry() {
    return MediaTypeRegistry.of(MediaType.class, BasicApplicationMediaType.class);
  }
}

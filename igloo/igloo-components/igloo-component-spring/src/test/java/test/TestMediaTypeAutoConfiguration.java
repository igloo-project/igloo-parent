package test;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.IMediaTypeRegistry;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeDefinition;
import org.iglooproject.commons.util.mime.MediaTypeRegistry;
import org.iglooproject.spring.autoconfigure.MediaTypeAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class TestMediaTypeAutoConfiguration {

  private enum ProjectMediaType implements IMediaType {
    TEXT_PLAIN(MediaTypeDefinition.from(MediaType.TEXT_PLAIN).withExtensions("conf"));

    private final MediaTypeDefinition definition;

    ProjectMediaType(MediaTypeDefinition definition) {
      this.definition = definition;
    }

    @Override
    public MediaTypeDefinition definition() {
      return definition;
    }
  }

  @Configuration
  static class ProjectMediaTypeConfiguration {

    @Bean
    public IMediaTypeRegistry mediaTypeRegistry() {
      return MediaTypeRegistry.of(MediaType.class, ProjectMediaType.class);
    }
  }

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(MediaTypeAutoConfiguration.class));

  @Test
  void defaultRegistry() {
    contextRunner.run(
        context ->
            Assertions.assertThat(context.getBean(IMediaTypeRegistry.class).values())
                .containsExactlyElementsOf(List.of(MediaType.values())));
  }

  @Test
  void projectRegistry() {
    contextRunner
        .withUserConfiguration(ProjectMediaTypeConfiguration.class)
        .run(
            context -> {
              Assertions.assertThat(context).hasSingleBean(IMediaTypeRegistry.class);
              IMediaTypeRegistry registry = context.getBean(IMediaTypeRegistry.class);
              Assertions.assertThat(registry.fromExtension("conf"))
                  .isSameAs(ProjectMediaType.TEXT_PLAIN);
              Assertions.assertThat(registry.canonical(MediaType.TEXT_PLAIN))
                  .isSameAs(ProjectMediaType.TEXT_PLAIN);
            });
  }
}

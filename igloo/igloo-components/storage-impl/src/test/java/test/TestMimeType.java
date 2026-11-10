package test;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.igloo.storage.impl.MimeTypeResolver;
import org.iglooproject.commons.util.mime.IMediaType;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.commons.util.mime.MediaTypeDefinition;
import org.iglooproject.commons.util.mime.MediaTypeRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

@Extensions(@ExtendWith(SoftAssertionsExtension.class))
class TestMimeType {

  private enum ProjectMediaType implements IMediaType {
    APPLICATION_PDF(MediaTypeDefinition.from(MediaType.APPLICATION_PDF).withExtensions("pdfa"));

    private final MediaTypeDefinition definition;

    ProjectMediaType(MediaTypeDefinition definition) {
      this.definition = definition;
    }

    @Override
    public MediaTypeDefinition definition() {
      return definition;
    }
  }

  private MimeTypeResolver mimeTypeResolver = new MimeTypeResolver();

  @Test
  void testMimetype(SoftAssertions softly) {
    softly.assertThat(mimeTypeResolver.resolve(null)).isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve(".gitinfo")).isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve("file.")).isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve("png.")).isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve("png.   ")).isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve(".pdf")).isEqualTo("application/pdf");
    softly.assertThat(mimeTypeResolver.resolve(".png.pdf")).isEqualTo("application/pdf");
    softly.assertThat(mimeTypeResolver.resolve("whatever.png")).isEqualTo("image/png");
    softly.assertThat(mimeTypeResolver.resolve("whatever.tiff")).isEqualTo("image/tiff");
    softly.assertThat(mimeTypeResolver.resolve("whatever.tif")).isEqualTo("image/tiff");
    softly.assertThat(mimeTypeResolver.resolve("whatever.PNG")).isEqualTo("image/png");
    softly.assertThat(mimeTypeResolver.resolve("whatever.pNG")).isEqualTo("image/png");
    softly.assertThat(mimeTypeResolver.resolve("whatever.jpeg")).isEqualTo("image/jpeg");
    softly.assertThat(mimeTypeResolver.resolve("whatever.jpg")).isEqualTo("image/jpeg");
    softly.assertThat(mimeTypeResolver.resolve("whatever.jpe")).isEqualTo("image/jpeg");
    softly
        .assertThat(mimeTypeResolver.resolve("whatever.jp2"))
        .isEqualTo("application/octet-stream");
    softly.assertThat(mimeTypeResolver.resolve("whatever.heic")).isEqualTo("image/heic");
    softly.assertThat(mimeTypeResolver.resolve("whatever.webp")).isEqualTo("image/webp");
    softly.assertThat(mimeTypeResolver.resolve("whatever.bmp")).isEqualTo("image/bmp");
    softly.assertThat(mimeTypeResolver.resolve("whatever.js")).isEqualTo("text/javascript");
    softly.assertThat(mimeTypeResolver.resolve("whatever.json")).isEqualTo("application/json");
    softly.assertThat(mimeTypeResolver.resolve("whatever.tar.gz")).isEqualTo("application/gzip");
  }

  @Test
  void testMimetypeProjectRegistry() {
    MimeTypeResolver projectMimeTypeResolver =
        new MimeTypeResolver(
            MediaTypeRegistry.of(
                List.of(MediaType.values()), List.of(ProjectMediaType.APPLICATION_PDF)));

    Assertions.assertThat(projectMimeTypeResolver.resolve("whatever.pdfa"))
        .isEqualTo("application/pdf");
    Assertions.assertThat(mimeTypeResolver.resolve("whatever.pdfa"))
        .isEqualTo("application/octet-stream");
  }
}

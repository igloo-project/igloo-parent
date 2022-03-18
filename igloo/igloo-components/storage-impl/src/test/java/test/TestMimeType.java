package test;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.igloo.storage.impl.MimeTypeResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;

@Extensions(@ExtendWith(SoftAssertionsExtension.class))
class TestMimeType {

	private MimeTypeResolver mimeTypeResolver = new MimeTypeResolver();

	@Test
	void testMimetype(SoftAssertions softly) {
		softly.assertThat(mimeTypeResolver.resolve(null)).isEqualTo("application/octet-stream");
		softly.assertThat(mimeTypeResolver.resolve(".gitinfo")).isEqualTo("application/octet-stream");
		softly.assertThat(mimeTypeResolver.resolve("file.")).isEqualTo("application/octet-stream");
		softly.assertThat(mimeTypeResolver.resolve("png.")).isEqualTo("application/octet-stream");
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
		softly.assertThat(mimeTypeResolver.resolve("whatever.jp2")).isEqualTo("image/jp2");
	}
}

package fr.openwide.core.test.commons.util.mime;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.commons.util.mime.MediaType;

public class TestMediaType {
	
	@Test
	public void testFromExtension() {
		Assert.assertSame(MediaType.IMAGE_GIF, MediaType.fromExtension("gif"));
		Assert.assertSame(MediaType.IMAGE_GIF, MediaType.fromExtension("GIF"));
		
		Assert.assertSame(MediaType.IMAGE_JPEG, MediaType.fromExtension("jpg"));
		Assert.assertSame(MediaType.IMAGE_JPEG, MediaType.fromExtension("jpeg"));
	}
	
	@Test
	public void testFromMimeType() {
		Assert.assertSame(MediaType.APPLICATION_JSON, MediaType.fromMimeType("application/json"));
		Assert.assertSame(MediaType.APPLICATION_JSON, MediaType.fromMimeType("application/json;charset=UTF-8"));
	}
	
	@Test
	public void testGetters() {
		Assert.assertEquals("jpg", MediaType.IMAGE_JPEG.extension());
		Assert.assertEquals("image/jpeg", MediaType.IMAGE_JPEG.mime());
		Assert.assertEquals("application/json;charset=UTF-8", MediaType.APPLICATION_JSON.mimeUtf8());
		
		List<String> supportedExtensions = MediaType.IMAGE_JPEG.supportedExtensions();
		Assert.assertTrue(supportedExtensions.contains("jpg"));
		Assert.assertTrue(supportedExtensions.contains("jpe"));
		Assert.assertTrue(supportedExtensions.contains("jpeg"));
	}
}

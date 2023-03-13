package org.iglooproject.sass.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.google.common.hash.Hashing;

public class StaticResourceHelper {

	public static final String DEFAULT_STATIC_SCSS_RESOURCE_PATH = "igloo-static-scss";

	private StaticResourceHelper() {}

	public static Path getStaticResourcePath(String baseResourcePath, Class<?> clazz, String path) {
		// build package.ClassName:filename string
		String unhashedValue = String.format("%s:%s", clazz.getName(), path);
		// build a hash to allow to store all resources in the same folder
		String hashedValue = Hashing.sha256().hashString(unhashedValue, StandardCharsets.UTF_8).toString();
		// concatenate to base resource path
		return Path.of(baseResourcePath).resolve(hashedValue + ".css");
	}

}

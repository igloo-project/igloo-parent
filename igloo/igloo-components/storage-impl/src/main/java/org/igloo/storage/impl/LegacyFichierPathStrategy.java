package org.igloo.storage.impl;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierPathStrategy;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import org.springframework.util.StringUtils;


public class LegacyFichierPathStrategy implements IFichierPathStrategy {

	private final int hashSizeInBytes;

	public LegacyFichierPathStrategy(int hashSizeInBytes) {
		this.hashSizeInBytes = hashSizeInBytes;
	}

	@Override
	public String getPath(Fichier fichier) {
		Long id = fichier.getId();
		String extension = FilenameUtils.getExtension(fichier.getFilename());
		StringBuilder filenameBuilder = new StringBuilder(id.toString());
		if (StringUtils.hasText(extension)) {
			filenameBuilder.append(FilenameUtils.EXTENSION_SEPARATOR).append(extension);
		}

		String digest = md5(id);
		Path path = Splitter.fixedLength(2)
				.splitToStream(digest)
				.limit(hashSizeInBytes)
				.map(Path::of)
				.reduce(Path.of(""), (a, b) -> a.resolve(b));

		return Path.of(fichier.getType().getPath(), path.toString(), filenameBuilder.toString()).toString();
	}

	@SuppressWarnings("deprecation")
	private String md5(Long id) {
		// md5 used as a legacy option and with no cryptographic role
		return Hashing.md5().hashBytes(id.toString().getBytes(StandardCharsets.UTF_8)).toString();
	}

}

package org.igloo.storage.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierPathStrategy;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class LegacyFichierPathStrategy implements IFichierPathStrategy {

	private final int hashSizeInBytes;

	public LegacyFichierPathStrategy(int hashSizeInBytes) {
		this.hashSizeInBytes = hashSizeInBytes;
	}

	@Override
	public String getPath(Fichier fichier) {
		Long id = fichier.getId();
		String digest = Hashing.md5().hashBytes(id.toString().getBytes(StandardCharsets.UTF_8)).toString();
		Path path = Splitter.fixedLength(2)
				.splitToStream(digest)
				.limit(hashSizeInBytes)
				.map(Path::of)
				.reduce(Path.of(""), (a, b) -> a.resolve(b));
		// TODO: gestion extension
		return Path.of(fichier.getFichierType().getPath(), path.toString(), id.toString()).toString();
	}

}

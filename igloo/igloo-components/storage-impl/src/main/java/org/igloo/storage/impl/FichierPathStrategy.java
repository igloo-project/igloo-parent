package org.igloo.storage.impl;

import java.nio.file.Path;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierPathStrategy;

import com.google.common.base.Splitter;

public class FichierPathStrategy implements IFichierPathStrategy {

	private final int hashSizeInBytes;

	public FichierPathStrategy(int hashSizeInBytes) {
		this.hashSizeInBytes = hashSizeInBytes;
	}

	@Override
	public String getPath(Fichier fichier) {
		String digest = fichier.getUuid().toString().replace("-", "");

		Path path = Splitter.fixedLength(2)
			.splitToStream(digest)
			.skip(1)
			.limit(hashSizeInBytes)
			.map(Path::of)
			.reduce(Path.of(""), (a, b) -> a.resolve(b));

		return Path.of(fichier.getType().getPath(), path.toString(), fichier.getUuid().toString()).toString();
	}

}

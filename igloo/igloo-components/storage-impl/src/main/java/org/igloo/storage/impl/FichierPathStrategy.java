package org.igloo.storage.impl;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierPathStrategy;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

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
		// TODO: gestion extension
		return Path.of(fichier.getFichierType().getPath(), path.toString(), fichier.getUuid().toString()).toString();
	}

}

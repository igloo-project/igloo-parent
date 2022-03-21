package org.igloo.storage.impl;

import java.nio.file.Path;

import javax.annotation.concurrent.Immutable;

@Immutable
public class StorageEvent {

	private final Long id;
	private final StorageEventType type;
	private final Path path;

	public StorageEvent(Long id, StorageEventType type, Path path) {
		this.id = id;
		this.type = type;
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public StorageEventType getType() {
		return type;
	}

	public Path getPath() {
		return path;
	}
}

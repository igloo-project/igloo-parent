package org.igloo.storage.impl;

import java.nio.file.Path;

import javax.annotation.concurrent.Immutable;

@Immutable
public class StorageTask {

	private final Long id;
	private final StorageTaskType type;
	private final Path path;

	public StorageTask(Long id, StorageTaskType type, Path path) {
		this.id = id;
		this.type = type;
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public StorageTaskType getType() {
		return type;
	}

	public Path getPath() {
		return path;
	}
}

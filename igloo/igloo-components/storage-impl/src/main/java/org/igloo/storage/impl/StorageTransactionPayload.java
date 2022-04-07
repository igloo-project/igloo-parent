package org.igloo.storage.impl;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StorageTransactionPayload {

	private final List<StorageEvent> events = new LinkedList<>();

	public void addEvent(Long id, StorageEventType type, Path path) {
		events.add(new StorageEvent(id, type, path));
	}

	public List<StorageEvent> getEvents() {
		return Collections.unmodifiableList(events);
	}

}

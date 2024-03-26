package org.iglooproject.jpa.more.business.difference.model;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;

public class Difference<T> {
	
	private final T before;

	private final T after;
	
	private final DiffNode diffNode;

	public Difference(T before, T after, DiffNode diffNode) {
		super();
		this.before = before;
		this.after = after;
		this.diffNode = diffNode;
	}

	public T getBefore() {
		return before;
	}

	public T getAfter() {
		return after;
	}

	public DiffNode getDiffNode() {
		return diffNode;
	}
	
	public boolean hasChange(NodePath path) {
		DiffNode nodeForPath = diffNode.getChild(path);
		return nodeForPath != null && nodeForPath.hasChanges();
	}
	
	public boolean hasChanges() {
		return diffNode.hasChanges();
	}
}

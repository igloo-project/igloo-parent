package org.iglooproject.jpa.more.business.difference.model;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;
import org.jspecify.annotations.Nullable;

public class Difference<T> {

  private final @Nullable T before;

  private final T after;

  private final DiffNode diffNode;

  public Difference(@Nullable T before, T after, DiffNode diffNode) {
    super();
    this.before = before;
    this.after = after;
    this.diffNode = diffNode;
  }

  public @Nullable T getBefore() {
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

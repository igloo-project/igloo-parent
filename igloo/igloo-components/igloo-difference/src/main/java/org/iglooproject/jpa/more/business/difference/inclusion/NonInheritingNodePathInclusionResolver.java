package org.iglooproject.jpa.more.business.difference.inclusion;

import static de.danielbechler.diff.inclusion.Inclusion.DEFAULT;
import static de.danielbechler.diff.inclusion.Inclusion.EXCLUDED;
import static de.danielbechler.diff.inclusion.Inclusion.INCLUDED;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.inclusion.InclusionResolver;
import de.danielbechler.diff.inclusion.ValueNode;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;

/**
 * A slightly modified version of {@link NodePathInclusionResolver} that does
 * not assume that every child node of an included node is also included.
 * <p>See the "MODIFIED HERE" comment for what was modified exactly.
 */
public class NonInheritingNodePathInclusionResolver implements InclusionResolver {
	private final ValueNode<Inclusion> inclusions = new ValueNode<>();
	private boolean containsIncluded;
	private boolean containsExcluded;

	@Override
	public Inclusion getInclusion(final DiffNode node) {
		if (isInactive()) {
			return DEFAULT;
		}
		return resolveInclusion(inclusions.getNodeForPath(node.getPath()));
	}

	@Override
	public boolean enablesStrictIncludeMode() {
		return containsIncluded;
	}

	private boolean isInactive() {
		return !containsIncluded && !containsExcluded;
	}

	public NonInheritingNodePathInclusionResolver setInclusion(final NodePath nodePath, final Inclusion inclusion) {
		inclusions.getNodeForPath(nodePath).setValue(inclusion);
		containsIncluded = inclusions.containsValue(INCLUDED);
		containsExcluded = inclusions.containsValue(EXCLUDED);
		return this;
	}

	private Inclusion resolveInclusion(final ValueNode<Inclusion> inclusionNode) {
		// When the node has been explicitly excluded it's clear what to do
		if (inclusionNode.getValue() == EXCLUDED) {
			return EXCLUDED;
		}

		// Since excluding a parent node wins over an explicit inclusion
		// of the current node we need to check that first (in fact it shouldn't
		// even be possible to get to this point, if the parent is excluded)
		final Inclusion parentInclusion = resolveParentInclusion(inclusionNode);
		if (parentInclusion == EXCLUDED) {
			// MODIFIED HERE
			return EXCLUDED;
		}

		// Only after the parent has been checked we can honor the explicit
		// inclusion of this node by checking whether itself or any of its
		// children is included
		if (inclusionNode.containsValue(INCLUDED)) {
			return INCLUDED;
		}

		return DEFAULT;
	}

	private Inclusion resolveParentInclusion(final ValueNode<Inclusion> inclusionNode) {
		final ValueNode<Inclusion> parentWithInclusion = inclusionNode.getClosestParentWithValue();
		if (parentWithInclusion != null) {
			return resolveInclusion(parentWithInclusion);
		}
		return DEFAULT;
	}
}
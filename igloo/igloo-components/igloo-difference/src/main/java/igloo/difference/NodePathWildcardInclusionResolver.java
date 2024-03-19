package igloo.difference;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.inclusion.InclusionResolver;
import de.danielbechler.diff.node.DiffNode;
import igloo.difference.model.DifferenceField;
import igloo.difference.model.DifferenceMode;

public class NodePathWildcardInclusionResolver implements InclusionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(NodePathWildcardInclusionResolver.class);

	private final Set<String> nodePaths = new HashSet<>();

	@Override
	public Inclusion getInclusion(DiffNode node) {
		LOGGER.debug("checking {}", node.getPath());
		String normalizedPath = PathHelper.normalizeNodePath(node.getPath());
		LOGGER.debug("normalized path {}", normalizedPath);
		if (nodePaths.contains(normalizedPath)) {
			return Inclusion.INCLUDED;
		}
		return Inclusion.DEFAULT;
	}

	@Override
	public boolean enablesStrictIncludeMode() {
		return true;
	}

	public void addInclusion(DifferenceField field) {
		nodePaths.add(field.getPath().toString());
		if (field.isContainer() && DifferenceMode.DEEP.equals(field.getMode())) {
			nodePaths.add(field.getPath().toString() + "[*]");
		}
	}

}

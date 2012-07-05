package fr.openwide.core.wicket.more.core;

import java.util.Set;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.aggregation.ResourceReferenceAndStringData;
import org.apache.wicket.resource.aggregation.ResourceReferenceCollection;
import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference.ResourceType;
import org.odlabs.wiquery.core.WiQueryDecoratingHeaderResponse;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

import fr.openwide.core.wicket.more.core.resources.Core172JavaScriptResourceReference;

public class Core172DecoratingHeaderResponse extends WiQueryDecoratingHeaderResponse {

	private static final String JAVASCRIPT_HIGH_PRIORITY = "000";
	private static final String JAVASCRIPT_NORMAL_PRIORITY = "100";

	public Core172DecoratingHeaderResponse(IHeaderResponse real) {
		super(real);
	}

	@Override
	protected String newGroupingKey(ResourceReferenceAndStringData ref) {
		// on force le chargement des API externes (donc google) avant les autres
		if (ref.getReference() != null) {
			return JAVASCRIPT_NORMAL_PRIORITY;
		} else {
			return JAVASCRIPT_HIGH_PRIORITY;
		}
	}

	@Override
	protected void renderCollection(Set<ResourceReferenceAndStringData> alreadyRendered, String key, ResourceReferenceCollection coll) {
		for (ResourceReferenceAndStringData data : coll) {
			ResourceReference ref = data.getReference();
			// permet de charger jquery 1.7.2 à chaque fois que jquery est demandé
			if (ref == CoreJavaScriptResourceReference.get()) {
				ref = Core172JavaScriptResourceReference.get();
			}
			if (ref instanceof AbstractResourceDependentResourceReference) {
				AbstractResourceDependentResourceReference parent = (AbstractResourceDependentResourceReference)ref;
				ResourceReferenceCollection childColl = newResourceReferenceCollection(key);
				for (AbstractResourceDependentResourceReference child : parent.getDependentResourceReferences())
				{
					childColl.add(toData(child));
				}
				// render the group of dependencies before the parent
				renderCollection(alreadyRendered, key, childColl);
				// now render the parent since the dependencies are rendered
				renderIfNotAlreadyRendered(alreadyRendered, toData((AbstractResourceDependentResourceReference) ref));
			} else {
				// render
				renderIfNotAlreadyRendered(alreadyRendered, data);
			}
		}
	}

	private static ResourceReferenceAndStringData toData(AbstractResourceDependentResourceReference reference) {
		boolean css = ResourceType.CSS.equals(reference.getResourceType());
		String string = css ? reference.getMedia() : reference.getUniqueId();
		return new ResourceReferenceAndStringData(reference, null, null, string, reference.getResourceType(), false, null, null);
	}

}

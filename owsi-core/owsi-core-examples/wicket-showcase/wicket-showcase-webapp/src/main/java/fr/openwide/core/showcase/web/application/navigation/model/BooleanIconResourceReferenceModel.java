package fr.openwide.core.showcase.web.application.navigation.model;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.showcase.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;

/**
 * Cf. {@link LinkFactory#testImage(IModel, IModel)}
 */
public class BooleanIconResourceReferenceModel extends AbstractReadOnlyModel<ResourceReference> {
	private static final long serialVersionUID = 1L;
	
	private static final ResourceReference IMAGE_TRUE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/tick.png");
	private static final ResourceReference IMAGE_FALSE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/cross.png");
	
	private final IModel<Boolean> booleanModel;
	
	public BooleanIconResourceReferenceModel(IModel<Boolean> booleanModel) {
		super();
		this.booleanModel = booleanModel;
	}
	
	@Override
	public ResourceReference getObject() {
		Boolean aBoolean = booleanModel.getObject();
		
		if (aBoolean == null) {
			return null;
		} else if (aBoolean) {
			return IMAGE_TRUE;
		} else {
			return IMAGE_FALSE;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		booleanModel.detach();
	}
}

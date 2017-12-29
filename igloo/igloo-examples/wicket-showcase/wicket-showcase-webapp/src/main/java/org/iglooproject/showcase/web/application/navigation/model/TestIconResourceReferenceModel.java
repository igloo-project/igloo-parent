package org.iglooproject.showcase.web.application.navigation.model;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import org.iglooproject.showcase.web.application.navigation.link.LinkFactory;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;

/**
 * Cf. {@link LinkFactory#testImage(IModel, IModel)}
 */
public class TestIconResourceReferenceModel extends AbstractReadOnlyModel<ResourceReference> {
	private static final long serialVersionUID = 1L;
	
	private static final ResourceReference IMAGE_TRUE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/logo_openwide.png");
	
	private final IModel<Boolean> booleanModel;
	
	public TestIconResourceReferenceModel(IModel<Boolean> booleanModel) {
		super();
		this.booleanModel = booleanModel;
	}
	
	@Override
	public ResourceReference getObject() {
		Boolean aBoolean = booleanModel.getObject();
		
		if (aBoolean != null && aBoolean) {
			return IMAGE_TRUE;
		} else {
			return null;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		booleanModel.detach();
	}
}

package fr.openwide.core.wicket.more.markup.html.template.component;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElementListConcatModel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class BodyBreadCrumbPanel extends GenericPanel<List<BreadCrumbElement>> {
	
	private static final long serialVersionUID = -3398120588294325073L;
	
	protected static final IModel<String> DEFAULT_DIVIDER_MODEL = ReadOnlyModel.of(Model.of("/"));

	public BodyBreadCrumbPanel(
			String id,
			IModel<List<BreadCrumbElement>> prependedBreadCrumbElementsModel,
			IModel<List<BreadCrumbElement>> breadCrumbElementsModel) {
		this(id, prependedBreadCrumbElementsModel, breadCrumbElementsModel, DEFAULT_DIVIDER_MODEL);
	}
	
	public BodyBreadCrumbPanel(
			String id,
			IModel<List<BreadCrumbElement>> prependedBreadCrumbElementsModel,
			IModel<List<BreadCrumbElement>> breadCrumbElementsModel,
			IModel<String> dividerModel) {
		super(
				id,
				new BreadCrumbElementListConcatModel(prependedBreadCrumbElementsModel, breadCrumbElementsModel)
		);
		
		add(new BreadCrumbListView("breadCrumbElementListView", getModel(), BreadCrumbMarkupTagRenderingBehavior.HTML_BODY, dividerModel));
		
		add(
				new EnclosureBehavior() {
					private static final long serialVersionUID = 1L;
					@Override
					protected void setVisibility(Component component, boolean visible) {
						component.setVisible(visible);
					}
				}
				.collectionModel(getModel())
		);
	}

}

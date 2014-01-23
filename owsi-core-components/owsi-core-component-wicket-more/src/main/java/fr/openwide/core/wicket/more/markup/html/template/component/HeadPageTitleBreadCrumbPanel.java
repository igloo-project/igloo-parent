package fr.openwide.core.wicket.more.markup.html.template.component;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

public class HeadPageTitleBreadCrumbPanel extends GenericPanel<List<BreadCrumbElement>> {

	private static final long serialVersionUID = 3662301038397441921L;
	
	public HeadPageTitleBreadCrumbPanel(
			String id,
			IModel<List<BreadCrumbElement>> prependedElementsModel,
			IModel<List<BreadCrumbElement>> headPageTitleElementsModel, IModel<List<BreadCrumbElement>> breadCrumbElementsModel,
			IModel<String> dividerModel, IModel<String> dividerReversedModel,
			IModel<Boolean> reverseModel
			) {
		super(
				id,
				new HtmlHeadPageTitleElementsModel(prependedElementsModel, headPageTitleElementsModel, breadCrumbElementsModel, reverseModel)
		);
		
		add(
				new BreadCrumbListView(
						"breadCrumbElementListView", getModel(),
						BreadCrumbMarkupTagRenderingBehavior.HTML_HEAD,
						new DividerReversedIfNecessaryModel(dividerModel, dividerReversedModel, reverseModel)
				)
		);
		
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

	private static class DividerReversedIfNecessaryModel extends AbstractReadOnlyModel<String> {
		
		private static final long serialVersionUID = 4497702163006031509L;
		
		private final IModel<String> dividerModel;
		private final IModel<String> dividerReversedModel;
		private final IModel<Boolean> reverseModel;
		
		public DividerReversedIfNecessaryModel(IModel<String> dividerModel, IModel<String> dividerReversedModel, IModel<Boolean> reverseModel) {
			this.dividerModel = dividerModel;
			this.dividerReversedModel = dividerReversedModel;
			this.reverseModel = reverseModel;
		}
		
		@Override
		public String getObject() {
			return reverseModel.getObject()
					? dividerReversedModel.getObject()
					: dividerModel.getObject();
		}
		
		@Override
		public void detach() {
			super.detach();
			reverseModel.detach();
			dividerModel.detach();
			dividerReversedModel.detach();
		}
	}
	
	private static class HtmlHeadPageTitleElementsModel extends AbstractReadOnlyModel<List<BreadCrumbElement>> {
		
		private static final long serialVersionUID = -1809848796763995233L;

		private final IModel<List<BreadCrumbElement>> prependedElementsModel;
		private final IModel<List<BreadCrumbElement>> headPageTitleElementsModel;
		private final IModel<List<BreadCrumbElement>> breadCrumbElementsModel;
		private final IModel<Boolean> reverseModel;
		
		public HtmlHeadPageTitleElementsModel(
				IModel<List<BreadCrumbElement>> prependedElementsModel,
				IModel<List<BreadCrumbElement>> headPageTitleElementsModel,
				IModel<List<BreadCrumbElement>> breadCrumbElementsModel,
				IModel<Boolean> reverseModel) {
			this.prependedElementsModel = prependedElementsModel;
			this.headPageTitleElementsModel = headPageTitleElementsModel;
			this.breadCrumbElementsModel = breadCrumbElementsModel;
			this.reverseModel = reverseModel;
		}

		@Override
		public List<BreadCrumbElement> getObject() {
			List<BreadCrumbElement> elements = headPageTitleElementsModel.getObject();
			if (elements == null || elements.isEmpty()) {
				elements = breadCrumbElementsModel.getObject();
			}
			
			ImmutableList<BreadCrumbElement> list = ImmutableList.<BreadCrumbElement>builder()
					.addAll(prependedElementsModel.getObject())
					.addAll(elements)
					.build();
			
			return reverseModel.getObject() ? list.reverse() : list;
		}
		
		@Override
		public void detach() {
			super.detach();
			prependedElementsModel.detach();
			headPageTitleElementsModel.detach();
			breadCrumbElementsModel.detach();
			reverseModel.detach();
		}
	}

}

package org.iglooproject.wicket.more.markup.html.template.component;

import java.util.List;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbMarkupTagRenderingBehavior;

import com.google.common.collect.ImmutableList;

public class HeadPageTitleBreadCrumbPanel extends GenericPanel<List<BreadCrumbElement>> {

	private static final long serialVersionUID = 3662301038397441921L;
	
	public HeadPageTitleBreadCrumbPanel(
		String id,
		IModel<List<BreadCrumbElement>> prependedElementsModel,
		IModel<List<BreadCrumbElement>> headPageTitleElementsModel, IModel<List<BreadCrumbElement>> breadCrumbElementsModel,
		IModel<String> dividerModel, IModel<String> dividerReversedModel,
		IModel<Boolean> reverseModel
	) {
		super(id, new HtmlHeadPageTitleElementsModel(prependedElementsModel, headPageTitleElementsModel, breadCrumbElementsModel, reverseModel));
		
		add(
			new BreadCrumbListView(
				"breadCrumbElementListView",
				getModel(),
				BreadCrumbMarkupTagRenderingBehavior.HTML_HEAD,
				new DividerReversedIfNecessaryModel(dividerModel, dividerReversedModel, reverseModel)
			)
		);
		
		add(
			Condition.collectionModelNotEmpty(getModel()).thenShowInternal()
		);
	}

	private static class DividerReversedIfNecessaryModel implements IModel<String> {
		
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
			IModel.super.detach();
			reverseModel.detach();
			dividerModel.detach();
			dividerReversedModel.detach();
		}
	}
	
	private static class HtmlHeadPageTitleElementsModel implements IModel<List<BreadCrumbElement>> {
		
		private static final long serialVersionUID = -1809848796763995233L;
		
		private final IModel<List<BreadCrumbElement>> prependedElementsModel;
		private final IModel<List<BreadCrumbElement>> headPageTitleElementsModel;
		private final IModel<List<BreadCrumbElement>> breadCrumbElementsModel;
		private final IModel<Boolean> reverseModel;
		
		public HtmlHeadPageTitleElementsModel(
			IModel<List<BreadCrumbElement>> prependedElementsModel,
			IModel<List<BreadCrumbElement>> headPageTitleElementsModel,
			IModel<List<BreadCrumbElement>> breadCrumbElementsModel,
			IModel<Boolean> reverseModel
		) {
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
			IModel.super.detach();
			prependedElementsModel.detach();
			headPageTitleElementsModel.detach();
			breadCrumbElementsModel.detach();
			reverseModel.detach();
		}
	}

}

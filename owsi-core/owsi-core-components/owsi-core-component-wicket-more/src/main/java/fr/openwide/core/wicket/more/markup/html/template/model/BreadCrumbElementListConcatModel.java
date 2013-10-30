package fr.openwide.core.wicket.more.markup.html.template.model;

import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.common.collect.ImmutableList;

public class BreadCrumbElementListConcatModel extends AbstractReadOnlyModel<List<BreadCrumbElement>> {
	
	private static final long serialVersionUID = -4163053491976956557L;
	
	private final IModel<List<BreadCrumbElement>> prependedListModel;
	private final IModel<List<BreadCrumbElement>> appendedListModel;
	
	public BreadCrumbElementListConcatModel(IModel<List<BreadCrumbElement>> prependedListModel,
			IModel<List<BreadCrumbElement>> appendedListModel) {
		super();
		this.prependedListModel = prependedListModel;
		this.appendedListModel = appendedListModel;
	}

	@Override
	public List<BreadCrumbElement> getObject() {
		List<BreadCrumbElement> prependedList = prependedListModel.getObject();
		List<BreadCrumbElement> appendedList = appendedListModel.getObject();
		
		return ImmutableList.<BreadCrumbElement>builder()
				.addAll(prependedList)
				.addAll(appendedList)
				.build();
	}

}

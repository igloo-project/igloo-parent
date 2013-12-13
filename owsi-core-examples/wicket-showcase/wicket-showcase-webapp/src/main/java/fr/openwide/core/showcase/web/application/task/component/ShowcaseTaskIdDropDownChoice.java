package fr.openwide.core.showcase.web.application.task.component;

import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import fr.openwide.core.showcase.core.business.task.model.ShowcaseTaskQueueId;
import fr.openwide.core.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class ShowcaseTaskIdDropDownChoice extends GenericSelect2DropDownSingleChoice<ShowcaseTaskQueueId> {

	private static final long serialVersionUID = 1L;
	
	public ShowcaseTaskIdDropDownChoice(String id, IModel<ShowcaseTaskQueueId> model) {
		super(id, model, new ListModel<ShowcaseTaskQueueId>(EnumUtils.getEnumList(ShowcaseTaskQueueId.class)), new ChoiceRenderer<ShowcaseTaskQueueId>());
		setNullValid(true);
	}
	
	@Override
	protected String getNullKey() {
		return "tasks.queue.default";
	}

}

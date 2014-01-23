package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.retzlaff.select2.Select2MultipleChoice;

import fr.openwide.core.showcase.core.business.user.model.User;

public class UserSelect2AjaxMultipleChoice extends Select2MultipleChoice<User> {
	
	private static final long serialVersionUID = -794964174746182964L;
	
	public UserSelect2AjaxMultipleChoice(String id, IModel<Collection<User>> model) {
		super(id, model, new UserSelect2AjaxAdapter());
		
		// TODO : vérifier Session.get().getLocale();
//		getSettings().setPlaceholderKey("widgets.selectbox.empty");
//		getSettings().setNoMatchesKey("widgets.selectbox.noMatches");
//		getSettings().setInputTooShortKey("widgets.selectbox.inputTooShort");
//		getSettings().setSelectionTooBigKey("widgets.selectbox.selectionTooBig");
		getSettings().setMaximumSelectionSize(3);
	}

}

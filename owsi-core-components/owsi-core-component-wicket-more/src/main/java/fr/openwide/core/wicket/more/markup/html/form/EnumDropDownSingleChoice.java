package fr.openwide.core.wicket.more.markup.html.form;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class EnumDropDownSingleChoice<E extends Enum<E>> extends GenericSelect2DropDownSingleChoice<E> {

	private static final long serialVersionUID = 6244269987751271782L;
	
	public EnumDropDownSingleChoice(String id, IModel<E> model, Class<E> clazz) {
		this(id, model, Model.ofList(EnumUtils.getEnumList(clazz)));
	}
	
	public EnumDropDownSingleChoice(String id, IModel<E> model, IModel<? extends List<? extends E>> choicesModel) {
		this(id, model, choicesModel, new EnumChoiceRenderer<E>());
	}

	protected EnumDropDownSingleChoice(String id, IModel<E> model, IModel<? extends List<? extends E>> choicesModel,
			IChoiceRenderer<? super E> renderer) {
		super(id, model, choicesModel, renderer);
	}

}

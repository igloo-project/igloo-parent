package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Supplier;

public class EnumDropDownMultipleChoice<E extends Enum<E>, C extends Collection<E>> extends GenericSelect2DropDownMultipleChoice<E, C> {

	private static final long serialVersionUID = 6244269987751271782L;
	
	public EnumDropDownMultipleChoice(String id, IModel<C> collectionModel, Supplier<? extends C> collectionSupplier, Class<E> clazz) {
		this(id, collectionModel, collectionSupplier, Model.ofList(EnumUtils.getEnumList(clazz)));
	}
	
	public EnumDropDownMultipleChoice(String id, IModel<C> collectionModel, Supplier<? extends C> collectionSupplier, IModel<? extends List<? extends E>> choicesModel) {
		this(id, collectionModel, collectionSupplier, choicesModel, new EnumChoiceRenderer<E>());
	}

	protected EnumDropDownMultipleChoice(String id, IModel<C> collectionModel, Supplier<? extends C> collectionSupplier,
			IModel<? extends List<? extends E>> choicesModel, IChoiceRenderer<? super E> renderer) {
		super(id, collectionModel, collectionSupplier, choicesModel, renderer);
	}

}

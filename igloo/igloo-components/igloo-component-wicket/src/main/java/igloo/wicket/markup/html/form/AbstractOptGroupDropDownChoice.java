package igloo.wicket.markup.html.form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

public abstract class AbstractOptGroupDropDownChoice<E> extends DropDownChoice<E> {

	private static final long serialVersionUID = -9151561337916965210L;

	private E last;

	private boolean grouped = true;

	protected AbstractOptGroupDropDownChoice(String id, IModel<E> model) {
		super(id);
		setModel(model);
	}
	
	protected AbstractOptGroupDropDownChoice(String id, IModel<E> model, IModel<? extends List<? extends E>> choicesModel) {
		super(id, model, choicesModel);
	}
	
	protected AbstractOptGroupDropDownChoice(String id, IModel<E> model, IModel<? extends List<? extends E>> choicesModel,
				IChoiceRenderer<? super E> renderer) {
		super(id, model, choicesModel, renderer);
	}
	
	protected AbstractOptGroupDropDownChoice(String id, IModel<E> model, final IModel<? extends List<? extends E>> choicesModel,
				IChoiceRenderer<? super E> renderer, final Comparator<E> sorter) {
		super(id, model, choicesModel, renderer);
		setChoices(new LoadableDetachableModel<List<? extends E>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<? extends E> load() {
				List<? extends E> result = choicesModel.getObject();
				Collections.sort(result, sorter);
				return result;
			};
			@Override
			public void detach() {
				super.detach();
				choicesModel.detach();
			}
		});
	}

	private boolean isLast(int index) {
		return index - 1 == getChoices().size();
	}

	private boolean isFirst(int index) {
		return index == 0;
	}

	protected abstract boolean isNewGroup(E current, E last);

	protected abstract String getGroupLabel(E current);

	@Override
	protected void appendOptionHtml(AppendingStringBuffer buffer, E choice, int index, String selected) {
		if (grouped) {
			if (last == null || isNewGroup(choice, last)) {
				if (!isFirst(index)) {
					buffer.append("</optgroup>");
				}
				buffer.append("<optgroup label='");
				buffer.append(Strings.escapeMarkup(getGroupLabel(choice)));
				buffer.append("'>");
			}
			super.appendOptionHtml(buffer, choice, index, selected);
			if (isLast(index)) {
				buffer.append("</optgroup>");
			}
			last = choice;
		} else {
			super.appendOptionHtml(buffer, choice, index, selected);
		}
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		last = null;
	}
	
	public boolean isGrouped() {
		return grouped;
	}
	
	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}
}

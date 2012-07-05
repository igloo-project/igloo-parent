package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

public abstract class HideableContainer extends WebMarkupContainer {
	
	private static final long serialVersionUID = -4570949966472824133L;
	
	private boolean visibleIfEmpty;
	
	private List<IModel<?>> models = Lists.newArrayList();

	private List<IModel<? extends List<?>>> listModels = Lists.newArrayList();
	
	private List<Component> components = Lists.newArrayList();
	
	protected HideableContainer(String id, boolean visibleIfEmpty) {
		super(id);
		this.visibleIfEmpty = visibleIfEmpty;
	}
	
	public HideableContainer listModel(IModel<? extends List<?>> model) {
		listModels.add(model);
		return this;
	}
	
	public HideableContainer model(IModel<?> model) {
		models.add(model);
		return this;
	}
	
	public HideableContainer component(Component component) {
		components.add(component);
		return this;
	}
	
	public HideableContainer components(Component... component) {
		components.addAll(Arrays.asList(component));
		return this;
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		for (Component component : components) {
			component.configure();
			if (component.determineVisibility()) {
				setVisible(!visibleIfEmpty);
				return;
			}
		}
		
		for (IModel<? extends List<?>> listModel : listModels) {
			if (listModel != null && listModel.getObject() != null && listModel.getObject().size() > 0) {
				setVisible(!visibleIfEmpty);
				return;
			}
		}
		
		for (IModel<?> model : models) {
			if (model != null && model.getObject() != null) {
				setVisible(!visibleIfEmpty);
				return;
			}
		}
		
		setVisible(visibleIfEmpty);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		
		for (IModel<?> listModel : listModels) {
			listModel.detach();
		}
		
		for (IModel<?> model : models) {
			model.detach();
		}
	}

}

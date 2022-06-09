package igloo.wicket.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

public class EnabledContainer extends WebMarkupContainer {
	
	private static final long serialVersionUID = 3955461375453681131L;

	private boolean enabledIfEmpty;

	private boolean toggleEnabled;

	private String enabledClassName;

	private String disabledClassName;
	
	private List<IModel<?>> models = Lists.newArrayList();

	private List<IModel<? extends List<?>>> listModels = Lists.newArrayList();
	
	private List<Component> components = Lists.newArrayList();

	public EnabledContainer(String id, boolean enabledIfEmpty, boolean toggleEnabled,
			String enabledClassName, String disabledClassName) {
		super(id);
		this.enabledIfEmpty = enabledIfEmpty;
		this.toggleEnabled = toggleEnabled;
		this.enabledClassName = enabledClassName;
		this.disabledClassName = disabledClassName;
	}
	
	public EnabledContainer listModel(IModel<? extends List<?>> model) {
		listModels.add(model);
		return this;
	}
	
	public EnabledContainer model(IModel<?> model) {
		models.add(model);
		return this;
	}
	
	public EnabledContainer component(Component component) {
		components.add(component);
		return this;
	}
	
	public EnabledContainer components(Component... component) {
		components.addAll(Arrays.asList(component));
		return this;
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		for (Component component : components) {
			component.configure();
			if (component.isVisible()) {
				conditionnalySetEnabled(!enabledIfEmpty);
				return;
			}
		}
		
		for (IModel<? extends List<?>> listModel : listModels) {
			if (listModel != null && listModel.getObject() != null && listModel.getObject().size() > 0) {
				conditionnalySetEnabled(!enabledIfEmpty);
				return;
			}
		}
		
		for (IModel<?> model : models) {
			if (model != null && model.getObject() != null) {
				conditionnalySetEnabled(!enabledIfEmpty);
				return;
			}
		}
		
		conditionnalySetEnabled(enabledIfEmpty);
	}
	
	@Override
	public void onComponentTag(ComponentTag openTag) {
		super.onComponentTag(openTag);
		if (isEnabled()) {
			openTag.append("class", enabledClassName, " ");
		} else {
			openTag.append("class", disabledClassName, " ");
		}
	}

	protected void conditionnalySetEnabled(boolean enabled) {
		if (toggleEnabled) {
			setEnabled(enabled);
		}
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

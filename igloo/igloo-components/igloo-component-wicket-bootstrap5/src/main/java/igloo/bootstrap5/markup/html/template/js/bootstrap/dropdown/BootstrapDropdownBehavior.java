package igloo.bootstrap5.markup.html.template.js.bootstrap.dropdown;

import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import igloo.wicket.model.Detachables;

public class BootstrapDropdownBehavior extends Behavior {

	private static final long serialVersionUID = -264765317701324800L;

	private final Component dropdownMenu;

	private final IModel<? extends IBootstrapDropdownOptions> optionsModel;

	public BootstrapDropdownBehavior(Component dropdownMenu) {
		this(dropdownMenu, LoadableDetachableModel.of(BootstrapDropdownOptions::get));
	}

	public BootstrapDropdownBehavior(Component dropdownMenu, IModel<? extends IBootstrapDropdownOptions> optionsModel) {
		super();
		this.dropdownMenu = Objects.requireNonNull(dropdownMenu);
		this.optionsModel = Objects.requireNonNull(optionsModel);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.put("data-bs-toggle", "dropdown");
		
		if (tag.getName().equals("a")) {
			tag.put("href", "#");
			tag.put("role", "button");
		}
		
		tag.put("aria-expanded", false);
	}

	@Override
	public void bind(Component component) {
		super.bind(component);
		dropdownMenu.add(new Behavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onComponentTag(Component dropdownMenu, ComponentTag tag) {
				tag.append("class", "dropdown-menu", " ");
				tag.put("aria-labelledby", component.getMarkupId());
			}
			
			@Override
			public boolean isEnabled(Component dropdownMenu) {
				return BootstrapDropdownBehavior.this.isEnabled(component);
			}
		});
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(OnDomReadyHeaderItem.forScript("new bootstrap.Dropdown(document.getElementById('" + component.getMarkupId() + "'), " + optionsModel.getObject().getJavaScriptOptions() + ");"));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(optionsModel);
	}

}

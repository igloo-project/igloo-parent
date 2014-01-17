package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.sortable.SortableBehavior;
import org.odlabs.wiquery.ui.sortable.SortableBehavior.ToleranceEnum;
import org.odlabs.wiquery.ui.sortable.SortableRevert;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.core.util.binding.Bindings;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.sortable.SortableListUpdateJavaScriptResourceReference;
import fr.openwide.core.wicket.more.model.BindingModel;

public class SortableUserListPanel extends GenericPanel<List<User>> {

	private static final long serialVersionUID = 2647182262770724957L;

	@SpringBean
	private IUserService userService;
	
	private Form<List<User>> userListForm;
	
	private ListView<User> sortableUserListView;
	
	private SortableBehavior listeUserSortableBehavior;
	
	private boolean sortMode;
	
	public SortableUserListPanel(String id, IModel<List<User>> userListModel) {
		super(id, userListModel);
		setOutputMarkupId(true);
		
		userListForm = new Form<List<User>>("userListForm", userListModel) {
			private static final long serialVersionUID = -4854860245258596038L;
			
			@Override
			public void onSubmit() {
				try {
					userService.updateUserPosition(SortableUserListPanel.this.getModelObject());
					sortableUserListView.getModel().detach();
					sortableUserListView.removeAll();
					
					SortableUserListPanel.this.setSortMode(false);
					
					getSession().success(getString("widgets.sortable.message.success"));
				} catch (Exception e) {
					getSession().error(getString("widgets.sortable.message.error"));
				}
				
				throw new RestartResponseException(getPage());
			}
		};
		add(userListForm);
		
		// List view
		sortableUserListView = new ListView<User>("userListView", userListModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<User> item) {
				IModel<User> userModel = item.getModel();
				item.setOutputMarkupId(true);
				
				item.add(new RequiredTextField<Integer>("positionField", BindingModel.of(userModel, Bindings.user().position())));
				
				item.add(new Label("positionLabel", BindingModel.of(userModel, Bindings.user().position())));
				item.add(new Label("username", BindingModel.of(userModel, Bindings.user().userName())));
				item.add(new Label("fullname", BindingModel.of(userModel, Bindings.user().fullName())));
			}
		};
		
		WebMarkupContainer listeUserContainer = new WebMarkupContainer("listeUserContainer");
		listeUserSortableBehavior = new SortableBehavior();
		listeUserSortableBehavior
			.setUpdateEvent(new JsScopeUiEvent() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void execute(JsScopeContext scopeContext) {
					scopeContext.append("onSortableUpdate($(this));");
				}
			})
			.setPlaceholder("element-place-holder")
			.setRevert(new SortableRevert(true))
			.setForcePlaceholderSize(true)
			.setTolerance(ToleranceEnum.POINTER)
			.setDisabled(!isSortMode());
		listeUserContainer.add(sortableUserListView);
		listeUserContainer.add(listeUserSortableBehavior);
		listeUserContainer.add(new ClassAttributeAppender(new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				if (!SortableUserListPanel.this.isSortMode()) {
					return "ui-sortable-disabled";
				}
				return null;
			}
		}));
		userListForm.add(listeUserContainer);
		
		// Actions sur la liste des users
		WebMarkupContainer actions = new WebMarkupContainer("actions");
		userListForm.add(actions);
		
		AjaxLink<?> modifierOrdre = new AjaxLink<Void>("modifierOrdre") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				SortableUserListPanel.this.listeUserSortableBehavior.enable(target);
				SortableUserListPanel.this.setSortMode(true);
				target.add(SortableUserListPanel.this);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!SortableUserListPanel.this.isSortMode());
			}
		};
		actions.add(modifierOrdre);
		
		Button enregistrerOrdre = new Button("enregistrerOrdre") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(SortableUserListPanel.this.isSortMode());
			}
		};
		actions.add(enregistrerOrdre);
		
		AjaxLink<?> annulerOrdre = new AjaxLink<Void>("annulerOrdre") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				SortableUserListPanel.this.setSortMode(false);
				SortableUserListPanel.this.listeUserSortableBehavior.disable(target);
				target.add(SortableUserListPanel.this);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(SortableUserListPanel.this.isSortMode());
			}
		};
		actions.add(annulerOrdre);
	}

	
	public boolean isSortMode() {
		return sortMode;
	}
	
	public void setSortMode(boolean sortMode) {
		this.sortMode = sortMode;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(SortableListUpdateJavaScriptResourceReference.get()));
	}
}

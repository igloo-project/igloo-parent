package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.form.UserGroupPopup;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.repeater.collection.CollectionView;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserGroupDescriptionPanel extends GenericPanel<UserGroup> {

	private static final long serialVersionUID = 4372823586880908316L;
	
	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;

	public UserGroupDescriptionPanel(String id, final IModel<UserGroup> userGroupModel) {
		super(id, userGroupModel);
		
		UserGroupPopup updatePopup = new UserGroupPopup("updatePopup", getModel());
		
		add(
				new WebMarkupContainer("lockedWarningContainer") {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(userGroupModel.getObject().isLocked());
					}
				},
				new MultiLineLabel("description", BindingModel.of(userGroupModel, Bindings.userGroup().description())),
				
				new CollectionView<Authority>(
						"authorities",
						Model.ofList(authorityUtils.getPublicAuthorities()),
						GenericEntityModel.<Authority>factory()
				) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void populateItem(Item<Authority> item) {
						final Authority authority = item.getModelObject();
						item.add(
								new Label("authorityName", new ResourceModel("administration.usergroup.authority." + authority.getName())),
								new BooleanIcon("authorityCheck", new LoadableDetachableModel<Boolean>() {
									private static final long serialVersionUID = 1L;
									@Override
									protected Boolean load() {
										return userGroupModel.getObject().getAuthorities().contains(authority);
									}
								})
						);
					}
				},
				
				updatePopup,
				new BlankLink("updateButton")
						.add(new AjaxModalOpenBehavior(updatePopup, MouseEvent.CLICK))
		);
	}
}

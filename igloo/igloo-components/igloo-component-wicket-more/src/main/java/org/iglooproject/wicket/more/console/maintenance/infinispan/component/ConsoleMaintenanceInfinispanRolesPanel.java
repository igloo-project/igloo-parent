package org.iglooproject.wicket.more.console.maintenance.infinispan.component;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iglooproject.commons.util.functional.SerializablePredicate;
import org.iglooproject.infinispan.model.IRole;
import org.iglooproject.infinispan.model.IRoleAttribution;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.maintenance.infinispan.form.ConsoleMaintenanceInfinispanRoleAssignPopup;
import org.iglooproject.wicket.more.markup.html.action.AbstractOneParameterAjaxAction;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;

public class ConsoleMaintenanceInfinispanRolesPanel extends Panel {

	private static final long serialVersionUID = 2410247581542059920L;

	public static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceInfinispanRolesPanel.class);

	@SpringBean
	private IInfinispanClusterService infinispanClusterService;
	
	private final IModel<Set<IRole>> rolesModel;

	public ConsoleMaintenanceInfinispanRolesPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		rolesModel = new LoadableDetachableModel<Set<IRole>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Set<IRole> load() {
				return infinispanClusterService.getAllRolesForAssignation();
			}
			
		};
		
		ConsoleMaintenanceInfinispanRoleAssignPopup assignPopup = new ConsoleMaintenanceInfinispanRoleAssignPopup("assignPopup");
		add(assignPopup);
		
		add(
				DataTableBuilder.start(
						ReadOnlyCollectionModel.of(rolesModel, Models.serializableModelFactory())
				)
						.addColumn(
								new AbstractCoreColumn<IRole, ISort<?>>(new ResourceModel("business.infinispan.role.key")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<IRole>> cellItem, String componentId, IModel<IRole> rowModel) {
										cellItem.add(
												new RoleKeyFragment(componentId, rowModel)
										);
									}
								}
						)
						.addColumn(
								new AbstractCoreColumn<IRole, ISort<?>>(new ResourceModel("business.infinispan.role.address")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<IRole>> cellItem, String componentId, IModel<IRole> rowModel) {
										cellItem.add(
												new RoleAddressFragment(componentId, rowModel)
										);
									}
								}
						)
						.addColumn(
								new AbstractCoreColumn<IRole, ISort<?>>(new ResourceModel("business.infinispan.role.attributionDate")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<IRole>> cellItem, String componentId, IModel<IRole> rowModel) {
										cellItem.add(
												new RoleAttributionDateFragment(componentId, rowModel)
										);
									}
								}
						)
						.addActionColumn()
								.addAction(
										BootstrapRenderer.constant("console.maintenance.infinispan.roles.actions.assign", "fa fa-exchange fa-fw", BootstrapColor.PRIMARY),
										new OneParameterModalOpenAjaxAction<IModel<IRole>>(assignPopup) {
											private static final long serialVersionUID = 1L;
											@Override
											protected void onShow(AjaxRequestTarget target, IModel<IRole> parameter) {
												assignPopup.init(parameter);
											}
										}
								)
								.addConfirmAction(
										BootstrapRenderer.constant("console.maintenance.infinispan.roles.actions.delete", "fa fa-times fa-fw", BootstrapColor.DANGER)
								)
										.title(new ResourceModel("console.maintenance.infinispan.roles.actions.delete.confirm.title"))
										.content(new ResourceModel("console.maintenance.infinispan.roles.actions.delete.confirm.content"))
										.yesNo()
										.onClick(
												new AbstractOneParameterAjaxAction<IModel<IRole>>() {
													private static final long serialVersionUID = 1L;
													@Override
													public void execute(AjaxRequestTarget target, IModel<IRole> model) {
														try {
															infinispanClusterService.unassignRole(model.getObject());
															Session.get().success(getString("console.maintenance.infinispan.roles.actions.delete.success"));
															target.add(ConsoleMaintenanceInfinispanRolesPanel.this);
														} catch (Exception e) {
															LOGGER.error("Erreur lors de la suppression d'un rôle.");
															Session.get().error(getString("common.error.unexpected"));
														}
														FeedbackUtils.refreshFeedback(target, getPage());
													}
												}
										)
										.when(
												new SerializablePredicate<IRole>() {
													private static final long serialVersionUID = 1L;
													@Override
													public boolean apply(IRole input) {
														IRoleAttribution roleAttribution = infinispanClusterService.getRoleAttribution(input);
														return roleAttribution != null && roleAttribution.getAttributionDate() != null && roleAttribution.getOwner() != null;
													}
												}
										)
										.withClassOnElements("btn-xs")
								.end()
						.bootstrapPanel()
								.title("console.maintenance.infinispan.roles")
								.responsive(Condition.alwaysTrue())
								.build("roles")
		);
	}
	
	private class RoleKeyFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public RoleKeyFragment(String id, IModel<IRole> roleModel) {
			super(id, "roleKey", ConsoleMaintenanceInfinispanRolesPanel.this, roleModel);
			
			add(
					new CoreLabel("key", BindingModel.of(roleModel, CoreWicketMoreBindings.iRole().key()))
							.add(
									new AttributeModifier("title", BindingModel.of(roleModel, CoreWicketMoreBindings.iRole().key()))
							)
			);
		}
		
	}
	
	private class RoleAddressFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IInfinispanClusterService infinispanClusterService;
		
		private final IModel<IRoleAttribution> roleAttributionModel;
		
		public RoleAddressFragment(String id, IModel<IRole> roleModel) {
			super(id, "roleAddress", ConsoleMaintenanceInfinispanRolesPanel.this, roleModel);
			
			roleAttributionModel = new LoadableDetachableModel<IRoleAttribution>() {
				private static final long serialVersionUID = 1L;
				@Override
				protected IRoleAttribution load() {
					return infinispanClusterService.getRoleAttribution(roleModel.getObject());
				}
				
			};
			
			add(
					new CoreLabel("address", BindingModel.of(roleAttributionModel, CoreWicketMoreBindings.iRoleAttribution().owner()))
			);
		}
		
		@Override
		protected void onDetach() {
			super.onDetach();
			Detachables.detach(roleAttributionModel);
		}
	}

	private class RoleAttributionDateFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IInfinispanClusterService infinispanClusterService;
		
		private final IModel<IRoleAttribution> roleAttributionModel;
		
		public RoleAttributionDateFragment(String id, IModel<IRole> roleModel) {
			super(id, "roleAttributionDate", ConsoleMaintenanceInfinispanRolesPanel.this, roleModel);
			
			roleAttributionModel = new LoadableDetachableModel<IRoleAttribution>() {
				private static final long serialVersionUID = 1L;
				@Override
				protected IRoleAttribution load() {
					return infinispanClusterService.getRoleAttribution(roleModel.getObject());
				}
				
			};
			
			add(
					new DateLabel("attributionDate", BindingModel.of(roleAttributionModel, CoreWicketMoreBindings.iRoleAttribution().attributionDate()), DatePattern.REALLY_SHORT_DATETIME)
			);
		}
		
		@Override
		protected void onDetach() {
			super.onDetach();
			Detachables.detach(roleAttributionModel);
		}
	}
	
	@Override
	protected void onDetach(){
		super.onDetach();
		Detachables.detach(rolesModel);
	}

}

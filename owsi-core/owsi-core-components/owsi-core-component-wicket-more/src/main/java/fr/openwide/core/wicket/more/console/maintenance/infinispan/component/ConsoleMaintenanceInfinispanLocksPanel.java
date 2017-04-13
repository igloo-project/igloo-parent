package fr.openwide.core.wicket.more.console.maintenance.infinispan.component;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.infinispan.model.ILock;
import fr.openwide.core.infinispan.model.ILockAttribution;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyCollectionModel;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.binding.CoreWicketMoreBindings;
import fr.openwide.core.wicket.more.util.model.Detachables;
import fr.openwide.core.wicket.more.util.model.Models;

public class ConsoleMaintenanceInfinispanLocksPanel extends Panel {

	private static final long serialVersionUID = -6235371376342468131L;
	
	@SpringBean
	private IInfinispanClusterService infinispanClusterService;

	private final IModel<Set<ILock>> locksModel;
	
	public ConsoleMaintenanceInfinispanLocksPanel(String id) {
		super(id);
		
		locksModel = new LoadableDetachableModel<Set<ILock>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Set<ILock> load() {
				return infinispanClusterService.getLocks();
			}
			
		};
		
		add(
				DataTableBuilder.start(
						ReadOnlyCollectionModel.of(locksModel, Models.serializableModelFactory())
				)
						.addColumn(
								new AbstractCoreColumn<ILock, ISort<?>>(new ResourceModel("business.infinispan.lock.key")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<ILock>> cellItem, String componentId, IModel<ILock> rowModel) {
										cellItem.add(
												new LockKeyFragment(componentId, rowModel)
										);
									}
								}
						)
						.addLabelColumn(
								new ResourceModel("business.infinispan.lock.type"),
								CoreWicketMoreBindings.iLock().type()
						)
						.addColumn(
								new AbstractCoreColumn<ILock, ISort<?>>(new ResourceModel("business.infinispan.lock.attribution")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<ILock>> cellItem, String componentId, IModel<ILock> rowModel) {
										cellItem.add(
												new LockAttributionFragment(componentId, rowModel)
										);
									}
								}
						)
								.bootstrapPanel()
										.title("console.maintenance.infinispan.locks")
										.responsive(Condition.alwaysTrue())
										.build("locks")
		);
	}
	
	private class LockAttributionFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		@SpringBean
		private IInfinispanClusterService infinispanClusterService;
		
		private final IModel<ILockAttribution> lockAttributionModel;
		
		public LockAttributionFragment(String id, IModel<ILock> lockModel) {
			super(id, "lockAttribution", ConsoleMaintenanceInfinispanLocksPanel.this, lockModel);
			
			lockAttributionModel = new LoadableDetachableModel<ILockAttribution>() {
				private static final long serialVersionUID = 1L;
				@Override
				protected ILockAttribution load() {
					return infinispanClusterService.getLockAttribution(lockModel.getObject());
				}
			};
			
			add(
					new CoreLabel("address", BindingModel.of(lockAttributionModel, CoreWicketMoreBindings.iLockAttribution().owner())),
					new DateLabel("attributionDate", BindingModel.of(lockAttributionModel, CoreWicketMoreBindings.iLockAttribution().attributionDate()), DatePattern.REALLY_SHORT_DATETIME)
			);
		}
		
		@Override
		protected void onDetach() {
			super.onDetach();
			Detachables.detach(lockAttributionModel);
		}
	}
	
	private class LockKeyFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public LockKeyFragment(String id, IModel<ILock> lockModel) {
			super(id, "lockKey", ConsoleMaintenanceInfinispanLocksPanel.this, lockModel);
			
			add(
					new CoreLabel("key",
									new StringResourceModel("business.infinispan.lock.key.${}", BindingModel.of(lockModel, CoreWicketMoreBindings.iLock().key()))
											.setDefaultValue(BindingModel.of(lockModel, CoreWicketMoreBindings.iLock().key()))
					)
							.add(
									new AttributeModifier("title", BindingModel.of(lockModel, CoreWicketMoreBindings.iLock().key()))
							)
			);
		}
		
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(locksModel);
	}
	

}

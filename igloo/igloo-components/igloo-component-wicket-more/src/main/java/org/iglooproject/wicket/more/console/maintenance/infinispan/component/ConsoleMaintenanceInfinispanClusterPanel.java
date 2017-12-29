package org.iglooproject.wicket.more.console.maintenance.infinispan.component;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Functions;

import org.iglooproject.infinispan.model.INode;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.maintenance.infinispan.renderer.INodeRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.component.BootstrapBadge;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;

public class ConsoleMaintenanceInfinispanClusterPanel extends Panel {

	private static final long serialVersionUID = -3170379589959735719L;

	@SpringBean
	private IInfinispanClusterService infinispanClusterService;

	private final IModel<List<INode>> nodesModel;

	public ConsoleMaintenanceInfinispanClusterPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		nodesModel = new LoadableDetachableModel<List<INode>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<INode> load() {
				return infinispanClusterService.getNodes();
			}
		};
		
		add(
				DataTableBuilder.start(
						ReadOnlyCollectionModel.of(nodesModel, Models.serializableModelFactory())
				)
						.addColumn(
								new AbstractCoreColumn<INode, ISort<?>>(new ResourceModel("business.infinispan.node.address")) {
									private static final long serialVersionUID = 1L;
									@Override
									public void populateItem(Item<ICellPopulator<INode>> cellItem, String componentId, IModel<INode> rowModel) {
										cellItem.add(
												new NodeAddressFragment(componentId, rowModel)
										);
									}
								}
						)
						.addLabelColumn(
								new ResourceModel("business.infinispan.node.name"),
								CoreWicketMoreBindings.iNode().name()
						)
						.addLabelColumn(
								new ResourceModel("business.infinispan.node.creationDate"),
								CoreWicketMoreBindings.iNode().creationDate(),
								DatePattern.REALLY_SHORT_DATETIME
						)
						.addBootstrapBadgeColumn(
								new ResourceModel("business.infinispan.node.anonymous"),
								Functions.identity(),
								INodeRenderer.anonymous()
						)
						.bootstrapPanel()
								.title("console.maintenance.infinispan.cluster")
								.responsive(Condition.alwaysTrue())
								.build("cluster")
		);
	}

	private class NodeAddressFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public NodeAddressFragment(String id, IModel<INode> nodeModel) {
			super(id, "nodeAddress", ConsoleMaintenanceInfinispanClusterPanel.this, nodeModel);
			
			add(
					new CoreLabel("address", BindingModel.of(nodeModel, CoreWicketMoreBindings.iNode().address())),
					new BootstrapBadge<>("local", BindingModel.of(nodeModel, CoreWicketMoreBindings.iNode()), INodeRenderer.local())
			);
		}
		
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(nodesModel);
	}

}

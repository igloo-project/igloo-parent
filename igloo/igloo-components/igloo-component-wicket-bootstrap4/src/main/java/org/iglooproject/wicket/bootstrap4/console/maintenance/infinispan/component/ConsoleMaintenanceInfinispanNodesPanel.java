package org.iglooproject.wicket.bootstrap4.console.maintenance.infinispan.component;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.infinispan.model.INode;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.wicket.bootstrap4.console.maintenance.infinispan.renderer.INodeRenderer;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.iglooproject.wicket.more.util.binding.CoreWicketMoreBindings;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.iglooproject.wicket.more.util.model.Models;

import com.google.common.base.Functions;

public class ConsoleMaintenanceInfinispanNodesPanel extends Panel {

	private static final long serialVersionUID = 5155655164189659661L;
	
	@SpringBean
	private IInfinispanClusterService infinispanClusterService;
	
	private final IModel<List<INode>> nodesModel;

	public ConsoleMaintenanceInfinispanNodesPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		nodesModel = new LoadableDetachableModel<List<INode>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<INode> load() {
				return infinispanClusterService.getAllNodes();
			}
		};
		
		add(
				DataTableBuilder.start(
						ReadOnlyCollectionModel.of(nodesModel, Models.serializableModelFactory())
				)
						.addLabelColumn(
								new ResourceModel("business.infinispan.node.address"),
								CoreWicketMoreBindings.iNode().address()
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
						.addLabelColumn(
								new ResourceModel("business.infinispan.node.leaveDate"),
								CoreWicketMoreBindings.iNode().leaveDate(),
								DatePattern.REALLY_SHORT_DATETIME
						)
						.addBootstrapBadgeColumn(
								new ResourceModel("business.infinispan.node.status"),
								Functions.identity(),
								INodeRenderer.status()
						)
								.bootstrapPanel()
										.title("console.maintenance.infinispan.nodes")
										.responsive(Condition.alwaysTrue())
										.build("nodes")
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(nodesModel);
	}

}

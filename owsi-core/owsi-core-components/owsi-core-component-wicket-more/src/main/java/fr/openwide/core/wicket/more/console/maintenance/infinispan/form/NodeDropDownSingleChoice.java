package fr.openwide.core.wicket.more.console.maintenance.infinispan.form;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.wicket.more.markup.html.select2.GenericSelect2DropDownSingleChoice;

public class NodeDropDownSingleChoice extends GenericSelect2DropDownSingleChoice<INode> {

	private static final long serialVersionUID = -6340793244408907094L;

	@SpringBean
	private IInfinispanClusterService infinispanClusterService;

	private static final NodeChoiceRenderer CHOICE_RENDERER = new NodeChoiceRenderer();

	protected NodeDropDownSingleChoice(String id, IModel<INode> model) {
		super(
				id,
				model,
				new ListModel<>(),
				CHOICE_RENDERER
		);
		setChoices(
				new LoadableDetachableModel<List<INode>>() {
					private static final long serialVersionUID = 1L;
					@Override
					protected List<INode> load() {
						return infinispanClusterService.getNodes();
					}
				}
		);
	}

	private static class NodeChoiceRenderer extends ChoiceRenderer<INode> {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getDisplayValue(INode object) {
			return object.getAddress();
		}
		@Override
		public String getIdValue(INode object, int index) {
			return object.getAddress().toString();
		}
	}

}

package basicapp.front.common.template.theme.common;

import org.apache.wicket.model.Model;

import basicapp.back.config.util.Environment;
import basicapp.front.BasicApplicationSession;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;

public class BootstrapBreakpointPanel extends EnclosureContainer {

	private static final long serialVersionUID = 5271828582493462504L;

	public BootstrapBreakpointPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		condition(Condition.isEqual(BasicApplicationSession.get().getEnvironmentModel(), Model.of(Environment.development)));
	}

}

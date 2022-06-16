package igloo.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

import igloo.bootstrap.popover.Popover;
import igloo.bootstrap.popover.PopoverBehavior;
import igloo.bootstrap.popover.PopoverPanel;

public class Bootstrap {

	private Bootstrap() {}

	public static Behavior behavior(Popover popover) {
		return new PopoverBehavior(popover);
	}

	public static Component panel(String wicketId, Popover popover) {
		return new PopoverPanel(wicketId, popover);
	}
}

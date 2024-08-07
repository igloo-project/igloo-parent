package igloo.bootstrap.badge;

import igloo.wicket.condition.Condition;
import org.apache.wicket.Component;

public interface IBootstrapBadge<T, C extends Component & IBootstrapBadge<T, C>> {

  C badgePill();

  C badgePill(Condition badgePill);

  C hideIcon();

  C showIcon();

  C showIcon(Condition showIcon);

  C hideLabel();

  C showLabel();

  C showLabel(Condition showLabel);

  C hideTooltip();

  C showTooltip();

  C showTooltip(Condition showTooltip);

  C asComponent();
}

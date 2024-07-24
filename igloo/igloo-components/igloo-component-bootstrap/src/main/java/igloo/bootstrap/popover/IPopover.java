package igloo.bootstrap.popover;

import igloo.bootstrap.woption.IWOption;
import igloo.bootstrap.woption.IWVisitable;
import igloo.bootstrap.woption.IWVisitor;
import igloo.bootstrap.woption.WVisitables;
import java.io.Serializable;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

@Value.Immutable
@Value.Style(typeImmutable = "*", typeAbstract = "I*")
public interface IPopover extends IWVisitable, Serializable {

  IJsPopover js();

  @Nullable
  IWOption<String> label();

  @Nullable
  IWOption<Boolean> showLabel();

  @Nullable
  IWOption<String> iconCssClass();

  @Nullable
  IWOption<String> linkCssClass();

  @Override
  default void accept(IWVisitor visitor) {
    WVisitables.visit(visitor, js(), label(), showLabel(), iconCssClass(), linkCssClass());
  }
}

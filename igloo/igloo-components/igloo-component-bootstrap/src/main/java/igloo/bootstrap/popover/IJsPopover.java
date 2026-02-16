package igloo.bootstrap.popover;

import igloo.bootstrap.js.statement.IJsObject;
import igloo.bootstrap.js.statement.IJsStatement;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeImmutable = "*", typeAbstract = "I*", jakarta = true)
public interface IJsPopover extends IJsObject, Serializable {

  @Nullable
  IJsStatement animation();

  @Nullable
  IJsStatement container();

  IJsStatement content();

  @Nullable
  IJsStatement delay();

  @Nullable
  IJsStatement html();

  @Nullable
  IJsStatement placement();

  @Nullable
  IJsStatement selector();

  @Nullable
  IJsStatement template();

  @Nullable
  IJsStatement title();

  @Nullable
  IJsStatement customClass();

  @Nullable
  IJsStatement trigger();

  @Nullable
  IJsStatement offset();

  @Nullable
  IJsStatement fallbackPlacements();

  @Nullable
  IJsStatement boundary();

  @Nullable
  IJsStatement sanitize();

  @Nullable
  IJsStatement allowList();

  @Nullable
  IJsStatement sanitizeFn();

  @Nullable
  IJsStatement popperConfig();

  public enum Placement {
    AUTO("auto"),
    TOP("top"),
    BOTTOM("bottom"),
    LEFT("left"),
    RIGHT("right");

    private final String value;

    private Placement(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public enum Trigger {
    CLICK("click"),
    HOVER("hover"),
    FOCUS("focus"),
    MANUAL("manual");

    private final String value;

    private Trigger(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public enum Boundary {
    CLIPPING_PARENTS("clippingParents");

    private final String value;

    private Boundary(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Override
  @Value.Derived
  default Map<String, IJsStatement> values() {
    Map<String, IJsStatement> result = new LinkedHashMap<>();
    List.<Map.Entry<String, Supplier<IJsStatement>>>of(
            Map.entry("allowList", this::allowList),
            Map.entry("animation", this::animation),
            Map.entry("boundary", this::boundary),
            Map.entry("container", this::container),
            Map.entry("content", this::content),
            Map.entry("customClass", this::customClass),
            Map.entry("delay", this::delay),
            Map.entry("fallbackPlacements", this::fallbackPlacements),
            Map.entry("html", this::html),
            Map.entry("offset", this::offset),
            Map.entry("placement", this::placement),
            Map.entry("popperConfig", this::popperConfig),
            Map.entry("sanitize", this::sanitize),
            Map.entry("sanitizeFn", this::sanitizeFn),
            Map.entry("selector", this::selector),
            Map.entry("template", this::template),
            Map.entry("title", this::title),
            Map.entry("trigger", this::trigger))
        .stream()
        .<Pair<String, IJsStatement>>map(e -> Pair.of(e.getKey(), e.getValue().get()))
        .filter(e -> e.getRight() != null)
        .forEachOrdered(e -> result.put(e.getLeft(), e.getRight()));
    return result;
  }
}

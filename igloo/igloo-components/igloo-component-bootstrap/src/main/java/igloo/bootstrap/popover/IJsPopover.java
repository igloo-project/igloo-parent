package igloo.bootstrap.popover;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

import igloo.bootstrap.js.statement.IJsObject;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsBooleanType;
import igloo.bootstrap.js.type.JsFunctionType;
import igloo.bootstrap.js.type.JsMappingType;
import igloo.bootstrap.js.type.JsSequenceType;
import igloo.bootstrap.js.type.JsStringType;

@Value.Immutable
@Value.Style(typeImmutable="*", typeAbstract="I*")
public interface IJsPopover extends IJsObject, Serializable {

	@Nullable
	IJsStatement<JsBooleanType> animation();

	@Nullable
	IJsStatement<JsAnyType> container();

	IJsStatement<JsAnyType> content();

	@Nullable
	IJsStatement<JsAnyType> delay();

	@Nullable
	IJsStatement<JsBooleanType> html();

	@Nullable
	IJsStatement<JsAnyType> placement();

	@Nullable
	IJsStatement<JsStringType> selector();

	@Nullable
	IJsStatement<JsStringType> template();

	@Nullable
	IJsStatement<JsAnyType> title();

	@Nullable
	IJsStatement<JsAnyType> customClass();

	@Nullable
	IJsStatement<JsStringType> trigger();

	@Nullable
	IJsStatement<JsAnyType> offset();

	@Nullable
	IJsStatement<JsSequenceType<JsStringType>> fallbackPlacements();

	@Nullable
	IJsStatement<JsAnyType> boundary();

	@Nullable
	IJsStatement<JsBooleanType> sanitize();

	@Nullable
	IJsStatement<JsMappingType<JsSequenceType<JsStringType>>> allowList();

	@Nullable
	IJsStatement<JsFunctionType> sanitizeFn();

	@Nullable
	IJsStatement<JsAnyType> popperConfig();

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
	default Map<String, IJsStatement<?>> values() {
		Map<String, IJsStatement<?>> result = new LinkedHashMap<>();
		List.<Map.Entry<String, Supplier<IJsStatement<?>>>>of(
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
				Map.entry("trigger", this::trigger)
			)
			.stream()
			.<Pair<String, IJsStatement<?>>>map(e -> Pair.of(e.getKey(), e.getValue().get()))
			.filter(e -> e.getRight() != null)
			.forEachOrdered(e -> result.put(e.getLeft(), rightCast(e)));
		return result;
	}

	@SuppressWarnings("unchecked")
	default IJsStatement<JsAnyType> rightCast(Pair<String, IJsStatement<?>> entry) {
		return (IJsStatement<JsAnyType>) entry.getRight();
	}

}

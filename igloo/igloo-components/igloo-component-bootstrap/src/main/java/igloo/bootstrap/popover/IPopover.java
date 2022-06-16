package igloo.bootstrap.popover;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

import igloo.bootstrap.js.statement.IJsMapping;
import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.js.type.JsAnyType;
import igloo.bootstrap.js.type.JsBooleanType;
import igloo.bootstrap.js.type.JsFunctionType;
import igloo.bootstrap.js.type.JsMappingType;
import igloo.bootstrap.js.type.JsSequenceType;
import igloo.bootstrap.js.type.JsStringType;
import igloo.bootstrap.woption.IWOption;
import igloo.bootstrap.woption.IWOptionVisitor;

@Value.Immutable
@Value.Style(typeImmutable="*", typeAbstract="I*")
public interface IPopover extends IJsMapping<JsAnyType>, Serializable {

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

	@Nullable
	IWOption<Boolean> showLabel();

	@Nullable
	IWOption<String> iconCssClass();

	@Nullable
	IWOption<String> linkCssClass();

	@Override
	@Value.Derived
	default Map<String, IJsStatement<JsAnyType>> values() {
		Map<String, IJsStatement<JsAnyType>> result = new LinkedHashMap<>();
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

	@Override
	default void accept(IWOptionVisitor visitor) {
		IJsMapping.super.accept(visitor);
		
	}

}

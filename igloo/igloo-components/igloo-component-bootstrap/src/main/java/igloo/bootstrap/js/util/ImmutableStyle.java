package igloo.bootstrap.js.util;

import org.immutables.value.Value;

@Value.Style(
    typeAbstract = "I*",
    typeImmutable = "*",
    defaults = @Value.Immutable(builder = false),
    jakarta = true)
public @interface ImmutableStyle {}

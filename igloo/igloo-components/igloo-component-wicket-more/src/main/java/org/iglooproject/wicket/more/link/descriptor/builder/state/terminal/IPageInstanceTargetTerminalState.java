package org.iglooproject.wicket.more.link.descriptor.builder.state.terminal;

/** A state where the build can be terminated, returning the result of the build. */
public interface IPageInstanceTargetTerminalState<Result> {

  Result build();
}

package igloo.bootstrap.jsmodel;

import javax.annotation.Nonnull;

import org.apache.wicket.Component;

public interface IJsComponentFactory {

	@Nonnull
	public Component createComponent(@Nonnull String wicketId);

}

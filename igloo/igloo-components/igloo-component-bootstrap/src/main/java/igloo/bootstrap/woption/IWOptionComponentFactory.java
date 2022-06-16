package igloo.bootstrap.woption;

import javax.annotation.Nonnull;

import org.apache.wicket.Component;

public interface IWOptionComponentFactory {

	@Nonnull
	public Component createComponent(@Nonnull String wicketId);

}

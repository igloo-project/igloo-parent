package org.iglooproject.test.sass.config;

import org.iglooproject.sass.config.ISassConfigurationProvider;

public final class TestSassConfigurationProvider implements ISassConfigurationProvider {

	public static final TestSassConfigurationProvider of() {
		return of(true);
	}

	public static final TestSassConfigurationProvider of(final boolean autoprefixerEnabled) {
		return new TestSassConfigurationProvider(autoprefixerEnabled);
	}

	private final boolean autoprefixerEnabled;

	public TestSassConfigurationProvider(final boolean autoprefixerEnabled) {
		this.autoprefixerEnabled = autoprefixerEnabled;
	}

	@Override
	public boolean isAutoprefixerEnabled() {
		return autoprefixerEnabled;
	}

}

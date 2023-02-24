package org.iglooproject.test.sass.config;

import org.iglooproject.sass.config.ISassConfigurationProvider;

public final class TestSassConfigurationProvider implements ISassConfigurationProvider {

	public static final TestSassConfigurationProvider of(final boolean autoprefixerEnabled, final boolean staticEnabled) {
		return new TestSassConfigurationProvider(autoprefixerEnabled, staticEnabled);
	}

	private final boolean autoprefixerEnabled;
	private final boolean staticEnabled;

	public TestSassConfigurationProvider(final boolean autoprefixerEnabled, final boolean staticEnabled) {
		this.autoprefixerEnabled = autoprefixerEnabled;
		this.staticEnabled = staticEnabled;
	}

	@Override
	public boolean isAutoprefixerEnabled() {
		return autoprefixerEnabled;
	}

	@Override
	public boolean useStatic() {
		return staticEnabled;
	}

}

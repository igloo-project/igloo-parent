package fr.openwide.core.jpa.config.spring.provider;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class JpaPackageScanProvider {

	private final List<Package> packages;

	public JpaPackageScanProvider(Package... packages) {
		super();
		
		this.packages = ImmutableList.copyOf(packages);
	}

	public List<Package> getPackages() {
		return packages;
	}

}

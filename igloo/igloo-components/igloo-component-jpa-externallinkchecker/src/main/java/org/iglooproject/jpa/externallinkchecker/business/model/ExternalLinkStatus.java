package org.iglooproject.jpa.externallinkchecker.business.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum ExternalLinkStatus {
	ONLINE,
	OFFLINE,
	DEAD_LINK,
	DELETED, // Hibernate has a lot of problem to cascade the removal so let's introduce a specific status for that
	IGNORED;
	
	public static final List<ExternalLinkStatus> INACTIVES = ImmutableList.of(DEAD_LINK, DELETED, IGNORED);
}

package fr.openwide.core.jpa.more.business.link.model;

public enum ExternalLinkStatus {
	ONLINE,
	OFFLINE,
	DEAD_LINK,
	DELETED // Hibernate has a lot of problem to cascade the removal so let's introduce a specific status for that
}

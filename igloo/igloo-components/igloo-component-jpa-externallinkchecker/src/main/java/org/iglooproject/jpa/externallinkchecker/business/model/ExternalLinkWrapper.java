package org.iglooproject.jpa.externallinkchecker.business.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.externallinkchecker.util.binding.CoreJpaExternalLinkCheckerBindings;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Maps;

@Bindable
@Cacheable
@Entity
public class ExternalLinkWrapper extends GenericEntity<Long, ExternalLinkWrapper> {

	private static final long serialVersionUID = -4558332826839419557L;
	
	@Id
	@GeneratedValue
	@DocumentId
	private Long id;
	
	@Column(nullable = false)
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType") // SQL type "text" (unknown size)
	private String url;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ExternalLinkStatus status = ExternalLinkStatus.ONLINE;
	
	@Column(nullable = false)
	private int consecutiveFailures = 0;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ExternalLinkErrorType lastErrorType;
	
	@Column
	private Integer lastStatusCode;
	
	@Column
	private Date lastCheckDate;
	
	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType") // SQL type "text" (unknown size)
	private String failureAudit;
	
	protected ExternalLinkWrapper() {
	}
	
	public ExternalLinkWrapper(String url) {
		this.url = url;
	}
	
	public ExternalLinkWrapper(ExternalLinkWrapper link) {
		setUrl(link.getUrl());
		setStatus(link.getStatus());
		setConsecutiveFailures(link.getConsecutiveFailures());
		setLastErrorType(link.getLastErrorType());
		setLastStatusCode(link.getLastStatusCode());
		setLastCheckDate(link.getLastCheckDate());
		setFailureAudit(link.getFailureAudit());
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public ExternalLinkStatus getStatus() {
		return status;
	}
	
	public void setStatus(ExternalLinkStatus status) {
		this.status = status;
	}
	
	public int getConsecutiveFailures() {
		return consecutiveFailures;
	}
	
	public void setConsecutiveFailures(int consecutiveFailures) {
		this.consecutiveFailures = consecutiveFailures;
	}
	
	public ExternalLinkErrorType getLastErrorType() {
		return lastErrorType;
	}

	public void setLastErrorType(ExternalLinkErrorType lastErrorType) {
		this.lastErrorType = lastErrorType;
	}

	public Integer getLastStatusCode() {
		return lastStatusCode;
	}
	
	public void setLastStatusCode(Integer lastStatusCode) {
		this.lastStatusCode = lastStatusCode;
	}
	
	public Date getLastCheckDate() {
		return CloneUtils.clone(lastCheckDate);
	}
	
	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = CloneUtils.clone(lastCheckDate);
	}

	public String getFailureAudit() {
		return failureAudit;
	}

	public void setFailureAudit(String failureAudit) {
		this.failureAudit = failureAudit;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper()
			.add("url", getUrl());
	}

	@Override
	public int compareTo(ExternalLinkWrapper other) {
		if (this.equals(other)) {
			return 0;
		}
		return this.url.compareToIgnoreCase(other.getUrl());
	}
	
	@Transient
	public void resetStatus() {
		status = ExternalLinkStatus.ONLINE;
		consecutiveFailures = 0;
		lastStatusCode = null;
		lastCheckDate = null;
		lastErrorType = null;
		failureAudit = null;
	}
	
	@Transient
	public Map<String, Object> getResetStatusPropertyValues() {
		Map<String, Object> changes = Maps.newHashMap();
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().status().getPath(), ExternalLinkStatus.ONLINE);
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().consecutiveFailures().getPath(), 0);
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().lastStatusCode().getPath(), null);
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().lastCheckDate().getPath(), null);
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().lastErrorType().getPath(), null);
		changes.put(CoreJpaExternalLinkCheckerBindings.externalLinkWrapper().failureAudit().getPath(), null);
		
		return changes;
	}
}

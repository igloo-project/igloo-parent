package fr.openwide.core.wicket.more.console.maintenance.ehcache.model;

import java.io.Serializable;
import java.util.Date;

import org.bindgen.Bindable;

import fr.openwide.core.commons.util.CloneUtils;

@Bindable
public class JVMStatus implements Serializable {
	
	private static final long serialVersionUID = -6789572113712721557L;

	private Date startDate;
	
	private long uptime;
	
	private long freeMemory;
	
	private long totalMemory;
	
	private long maxMemory;

	public Date getStartDate() {
		return CloneUtils.clone(startDate);
	}

	public void setStartDate(Date startDate) {
		this.startDate = CloneUtils.clone(startDate);
	}
	
	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}
	

}

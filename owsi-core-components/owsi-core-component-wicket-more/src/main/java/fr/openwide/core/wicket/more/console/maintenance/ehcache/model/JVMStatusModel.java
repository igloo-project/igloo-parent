package fr.openwide.core.wicket.more.console.maintenance.ehcache.model;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import org.apache.wicket.model.LoadableDetachableModel;

public class JVMStatusModel extends LoadableDetachableModel<JVMStatus> {

	private static final long serialVersionUID = -4491792567763015444L;

	@Override
	protected JVMStatus load() {
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		Runtime runtime = Runtime.getRuntime();
		
		JVMStatus status = new JVMStatus();
		status.setStartDate(new Date(mx.getStartTime()));
		status.setUptime(mx.getUptime());
		status.setFreeMemory(runtime.freeMemory());
		status.setMaxMemory(runtime.maxMemory());
		status.setTotalMemory(runtime.totalMemory());
		
		return status;
	}

}

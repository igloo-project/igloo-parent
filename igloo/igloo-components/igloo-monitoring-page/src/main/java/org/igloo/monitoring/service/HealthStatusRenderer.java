package org.igloo.monitoring.service;

import java.util.Optional;

import com.google.common.base.Strings;

public class HealthStatusRenderer {

	private HealthLookup lookup;

	public HealthStatusRenderer(HealthLookup lookup) {
		this.lookup = lookup;
	}

	public String getLookupString() {
		StringBuilder sb = new StringBuilder();
		sb.append(lookup.getStatus().name());
		sb.append(" ");
		sb.append(lookup.getName());
		sb.append(" - ");
		if (!Strings.isNullOrEmpty(lookup.getMessage())) {
			sb.append(lookup.getMessage());
		} else {
			sb.append("NO MESSAGE");
		}
		if (lookup.getPerfDatas() != null && !lookup.getPerfDatas().isEmpty()) {
			sb.append("| ");
			lookup.getPerfDatas().stream().forEach(pd -> sb.append(getPerfDataString(pd)));
		}
		return sb.toString();
	}

	private Object getPerfDataString(PerfData pd) {
		StringBuilder sb = new StringBuilder();
		sb.append(pd.getName());
		sb.append("=");
		sb.append(pd.getValue());
		sb.append(" ");
		sb.append(pd.getUnit());
		sb.append(";");
		sb.append(Optional.ofNullable(pd.getWarning()).orElse(0));
		sb.append(";");
		sb.append(Optional.ofNullable(pd.getCritical()).orElse(0));
		sb.append(";");
		sb.append(Optional.ofNullable(pd.getMin()).orElse(0));
		sb.append(";");
		sb.append(Optional.ofNullable(pd.getMax()).orElse(0));
		sb.append("\n");
		sb.append("| ");
		return sb.toString();
	}

}

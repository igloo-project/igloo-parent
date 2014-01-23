package fr.openwide.core.commons.util.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BatchReport implements Serializable {

	private static final long serialVersionUID = 2058290436066602760L;

	public static final String GLOBAL_CONTEXT = "GLOBAL";

	private String context;

	private Map<String, List<BatchReportItem>> items = new LinkedHashMap<String, List<BatchReportItem>>();

	private boolean isOnError = false;

	public BatchReport() {
		this(GLOBAL_CONTEXT);
	}

	public BatchReport(String context) {
		setContext(context);
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void info(String message) {
		addItem(BatchReportItemSeverity.INFO, message);
	}

	public void debug(String message) {
		addItem(BatchReportItemSeverity.DEBUG, message);
	}
	
	public void trace(String message) {
		addItem(BatchReportItemSeverity.TRACE, message);
	}

	public void warn(String message) {
		addItem(BatchReportItemSeverity.WARN, message);
	}

	public void error(String message) {
		error(message, null);
	}

	public void error(String message, Exception e) {
		setOnError(true);
		addItem(BatchReportItemSeverity.ERROR, message, e);
	}

	private void addItem(BatchReportItemSeverity severity, String message) {
		addItem(severity, message, null);
	}

	private void addItem(BatchReportItemSeverity severity, String message,
			Exception e) {
		if (!items.containsKey(context)) {
			items.put(context, new ArrayList<BatchReportItem>());
		}
		items.get(context).add(new BatchReportItem(severity, message, e));
	}

	public Map<String, List<BatchReportItem>> getAllItems() {
		return items;
	}

	public List<BatchReportItem> getItemsForContext(String context) {
		if (items.containsKey(context)) {
			return items.get(context);
		} else {
			return new ArrayList<BatchReportItem>(0);
		}
	}

	public List<BatchReportItem> getGlobalItems() {
		return getItemsForContext(GLOBAL_CONTEXT);
	}

	public boolean isOnError() {
		return isOnError;
	}

	public void setOnError(boolean isOnError) {
		this.isOnError = isOnError;
	}

}

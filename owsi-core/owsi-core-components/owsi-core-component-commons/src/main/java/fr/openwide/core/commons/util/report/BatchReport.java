package fr.openwide.core.commons.util.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BatchReport {

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

	public void addSuccess(String message) {
		addItem(BatchReportItemSeverity.SUCCESS, message);
	}

	public void addNotice(String message) {
		addItem(BatchReportItemSeverity.NOTICE, message);
	}

	public void addWarning(String message) {
		addItem(BatchReportItemSeverity.WARNING, message);
	}

	public void addError(String message) {
		addError(message, null);
	}

	public void addError(String message, Exception e) {
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

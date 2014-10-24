package fr.openwide.core.jpa.more.business.task.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bindgen.Bindable;

import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.commons.util.report.BatchReportItem;

/**
 * Version épurée du {@link BatchReport}, utilisé pour la sérialisation du rapport d'exécution en fin de tâche.
 * 
 * Nota : on ne sérialise pas directement {@link BatchReport} car ses méthodes et accesseurs n'ont pas
 * du tout été pensés pour cela.
 */
@Bindable
public class BatchReportBean implements Serializable {

	private static final long serialVersionUID = -9115453894264070783L;

	private Map<String, List<BatchReportItem>> items = Maps.newLinkedHashMap();

	private boolean onError = false;

	protected BatchReportBean() {
	}

	public BatchReportBean(BatchReport batchReport) {
		if (batchReport != null) {
			this.items.putAll(batchReport.getItems());
			this.onError = batchReport.isOnError();
		}
	}

	public Map<String, List<BatchReportItem>> getItems() {
		return items;
	}

	public void setItems(Map<String, List<BatchReportItem>> items) {
		this.items = items;
	}

	public boolean isOnError() {
		return onError;
	}

	public void setOnError(boolean onError) {
		this.onError = onError;
	}
}

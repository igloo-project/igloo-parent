package fr.openwide.core.wicket.more.export.excel.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalBackdrop;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalManagerStatement;

public class ExcelExportWorkInProgressModalPopupPanel extends AbstractModalPopupPanel<String> {
	
	private static final long serialVersionUID = -2634100306392990085L;
	
	public ExcelExportWorkInProgressModalPopupPanel(String id) {
		super(id, new ResourceModel("common.action.export.excel.loading"));
		BootstrapModal options = BootstrapModal.modal();
		options.setKeyboard(false);
		options.setBackdrop(BootstrapModalBackdrop.STATIC);
		setBootstrapModal(options);
	}
	
	public void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		attributes.getAjaxCallListeners().add(new AjaxCallListener()
				.onBeforeSend(BootstrapModalManagerStatement.show(getContainer(), getBootstrapModal()).render())
				// On ne fait qu'afficher la modal, la fermeture est faite par ailleurs, une fois que le fichier est généré
//				.onFailure(BootstrapModalManagerStatement.hide(getContainer()).render())
//				.onComplete(BootstrapModalManagerStatement.hide(getContainer()).render())
		);
	}
	
	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, getModel());
	}
	
	@Override
	protected Component createBody(String wicketId) {
		return new DelegatedMarkupPanel(wicketId, ExcelExportWorkInProgressModalPopupPanel.class);
	}
	
	@Override
	protected final Component createFooter(String wicketId) {
		return new InvisiblePanel(wicketId);
	}
	
	@Override
	protected IModel<String> getCssClassNamesModel() {
		return Model.of("modal-work-in-progress");
	}
}

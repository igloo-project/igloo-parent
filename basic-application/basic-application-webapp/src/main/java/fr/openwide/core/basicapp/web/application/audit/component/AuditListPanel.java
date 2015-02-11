package fr.openwide.core.basicapp.web.application.audit.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.atomic.AuditAction;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.list.GenericPortfolioPanel;
import fr.openwide.core.wicket.more.markup.html.navigation.paging.HideableAjaxPagingNavigator;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class AuditListPanel extends GenericPortfolioPanel<Audit> {

	private static final long serialVersionUID = 1188689543635870482L;

	public AuditListPanel(String id, IDataProvider<Audit> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// Ce panel est destiné à être utilisé sur la fiche d'un utilisateur.
		// C'est pourquoi on remplace le pager existant par une version Ajax.
		Component originalPager = get("pager");
		if (originalPager != null) {
			originalPager.replaceWith(new HideableAjaxPagingNavigator("pager", getDataView()));
		}
	}
	
	@Override
	protected void addItemColumns(Item<Audit> item, IModel<? extends Audit> auditModel) {
		item.add(
				new DateLabel("date", BindingModel.of(auditModel, Bindings.audit().date()), DatePattern.SHORT_DATETIME),
				new EnumLabel<AuditAction>("action", BindingModel.of(auditModel, Bindings.audit().action())),
				new CoreLabel("subject", BindingModel.of(auditModel, Bindings.audit().subjectDisplayName()))
		);
	}

	@Override
	protected boolean hasWritePermissionOn(IModel<? extends Audit> itemModel) {
		return false;
	}

	@Override
	protected void doDeleteItem(IModel<? extends Audit> itemModel) throws ServiceException,
			SecurityServiceException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean isActionAvailable() {
		return false;
	}

	@Override
	protected boolean isDeleteAvailable() {
		return false;
	}

	@Override
	protected boolean isEditAvailable() {
		return false;
	}

}

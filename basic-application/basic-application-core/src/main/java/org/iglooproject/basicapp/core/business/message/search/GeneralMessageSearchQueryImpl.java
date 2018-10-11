package org.iglooproject.basicapp.core.business.message.search;

import org.iglooproject.basicapp.core.business.message.model.GeneralMessage;
import org.iglooproject.basicapp.core.business.message.model.QGeneralMessage;
import org.iglooproject.jpa.more.business.search.query.AbstractJpaSearchQuery;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GeneralMessageSearchQueryImpl
		extends AbstractJpaSearchQuery<GeneralMessage, GeneralMessageSort>
		implements IGeneralMessageSearchQuery {

	protected GeneralMessageSearchQueryImpl() {
		super(QGeneralMessage.generalMessage);
	}

}

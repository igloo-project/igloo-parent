package fr.openwide.core.test.jpa.more.business;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAudit;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditAction;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditActionEnum;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeature;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeatureEnum;
import fr.openwide.core.test.jpa.more.business.audit.service.IMockAuditService;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;
import fr.openwide.core.test.jpa.more.business.entity.service.ITestEntityService;
import fr.openwide.core.test.jpa.more.config.spring.JpaMoreTestConfig;

@ContextConfiguration(classes = JpaMoreTestConfig.class)
public abstract class AbstractJpaMoreTestCase extends AbstractTestCase {

	@Autowired
	protected CoreConfigurer configurer;

	@Autowired
	protected IGenericListItemService genericListItemService;

	@Autowired
	protected IMockAuditService auditService;

	@Autowired
	protected ITestEntityService testEntityService;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanAudits();
		cleanTestEntities();
		cleanFeaturesAndActions();
	}

	private void cleanAudits() throws ServiceException, SecurityServiceException {
		for (MockAudit audit : auditService.list()) {
			auditService.delete(audit);
		}
	}

	private void cleanTestEntities() throws ServiceException, SecurityServiceException {
		for (TestEntity testEntity : testEntityService.list()) {
			testEntityService.delete(testEntity);
		}
	}

	private void cleanFeaturesAndActions() throws ServiceException, SecurityServiceException {
		for (GenericListItem<?> genericListItem : genericListItemService.list(MockAuditFeature.class)) {
			genericListItemService.delete(genericListItem);
		}
		
		for (GenericListItem<?> genericListItem : genericListItemService.list(MockAuditAction.class)) {
			genericListItemService.delete(genericListItem);
		}
	}

	@Before
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
		initFeaturesAndActions();
	}

	private void initFeaturesAndActions() {
		for (MockAuditFeatureEnum auditFeatureEnum : MockAuditFeatureEnum.values()) {
			MockAuditFeature auditFeature = new MockAuditFeature(auditFeatureEnum.name(), auditFeatureEnum, 1);
			genericListItemService.create(auditFeature);
		}
		
		for (MockAuditActionEnum auditActionEnum : MockAuditActionEnum.values()) {
			MockAuditAction auditAction = new MockAuditAction(auditActionEnum.name(), auditActionEnum, 1);
			genericListItemService.create(auditAction);
		}
	}
}

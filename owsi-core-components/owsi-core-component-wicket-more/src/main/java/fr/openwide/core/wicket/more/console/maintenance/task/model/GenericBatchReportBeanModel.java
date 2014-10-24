package fr.openwide.core.wicket.more.console.maintenance.task.model;

import java.io.IOException;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.config.spring.AbstractTaskManagementConfig;

public class GenericBatchReportBeanModel<B extends BatchReportBean> extends LoadableDetachableModel<B> {
	
	private static final long serialVersionUID = 421890607339627456L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericBatchReportBeanModel.class);
	
	@SpringBean(name = AbstractTaskManagementConfig.OBJECT_MAPPER_BEAN_NAME)
	private ObjectMapper queuedTaskHolderObjectMapper;
	
	private final Class<B> clazz;
	
	private final IModel<QueuedTaskHolder> queuedTaskHolderModel;
	
	public GenericBatchReportBeanModel(Class<B> clazz, IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super();
		this.clazz = clazz;
		this.queuedTaskHolderModel = queuedTaskHolderModel;
		
		Injector.get().inject(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected B load() {
		QueuedTaskHolder queuedTaskHolder = queuedTaskHolderModel.getObject();
		String report = queuedTaskHolder.getReport();
		if (StringUtils.hasText(report)) {
			try {
				BatchReportBean reportBean = queuedTaskHolderObjectMapper.readValue(queuedTaskHolder.getReport(), BatchReportBean.class);
				
				if (reportBean != null && clazz.isAssignableFrom(reportBean.getClass())) {
					return (B) reportBean;
				}
			} catch (IOException e) {
				LOGGER.error("Error while reading serialized BatchReportBean.", e);
			}
		}
		return null;
	}

}

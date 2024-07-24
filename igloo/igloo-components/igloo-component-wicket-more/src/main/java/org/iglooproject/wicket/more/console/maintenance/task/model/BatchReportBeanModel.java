package org.iglooproject.wicket.more.console.maintenance.task.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.autoconfigure.TaskAutoConfiguration;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class BatchReportBeanModel<B extends BatchReportBean> extends LoadableDetachableModel<B> {

  private static final long serialVersionUID = 1226667856014694255L;

  private static final Logger LOGGER = LoggerFactory.getLogger(BatchReportBeanModel.class);

  public static BatchReportBeanModel<BatchReportBean> fromString(IModel<String> reportModel) {
    return new BatchReportBeanModel<>(BatchReportBean.class, reportModel);
  }

  public static BatchReportBeanModel<BatchReportBean> fromTask(
      IModel<QueuedTaskHolder> queuedTaskHolderModel) {
    return new TaskBatchReportBeanModel<>(BatchReportBean.class, queuedTaskHolderModel);
  }

  @SpringBean(name = TaskAutoConfiguration.OBJECT_MAPPER_BEAN_NAME)
  private ObjectMapper queuedTaskHolderObjectMapper;

  private final Class<B> clazz;

  private final IModel<String> reportModel;

  public BatchReportBeanModel(Class<B> clazz, IModel<String> reportModel) {
    super();
    this.clazz = clazz;
    this.reportModel = reportModel;

    Injector.get().inject(this);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    reportModel.detach();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected B load() {
    String report = reportModel.getObject();
    if (StringUtils.hasText(report)) {
      try {
        BatchReportBean reportBean =
            queuedTaskHolderObjectMapper.readValue(report, BatchReportBean.class);

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

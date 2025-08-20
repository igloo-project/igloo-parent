package basicapp.back.util.init.service;

import basicapp.back.business.BasicApplicationBackCommonBusinessPackage;
import basicapp.back.business.role.model.Role;
import basicapp.back.business.user.model.User;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.util.init.service.AbstractImportDataServiceImpl;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;

@Service
public class ImportDataServiceImpl extends AbstractImportDataServiceImpl {

  @Override
  protected List<String> getReferenceDataPackagesToScan() {
    return Lists.newArrayList(
        BasicApplicationBackCommonBusinessPackage.class.getPackage().getName());
  }

  @Override
  protected void importMainBusinessItems(
      Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping, Workbook workbook) {
    doImportItem(idsMapping, workbook, Role.class);
    doImportItem(idsMapping, workbook, User.class);
  }

  @Override
  protected void customizeConversionService(GenericConversionService conversionService) {
    // no customization
  }
}

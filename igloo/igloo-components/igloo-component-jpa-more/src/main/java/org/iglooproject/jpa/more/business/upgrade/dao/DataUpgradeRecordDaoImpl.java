package org.iglooproject.jpa.more.business.upgrade.dao;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.jpa.more.business.upgrade.model.QDataUpgradeRecord;
import org.springframework.stereotype.Repository;

@Repository
public class DataUpgradeRecordDaoImpl extends GenericEntityDaoImpl<Long, DataUpgradeRecord>
    implements IDataUpgradeRecordDao {

  @Override
  public boolean isDone(IDataUpgrade dataUpgrade) {
    return new JPAQuery<>(getEntityManager())
            .select(QDataUpgradeRecord.dataUpgradeRecord.id)
            .from(QDataUpgradeRecord.dataUpgradeRecord)
            .where(
                QDataUpgradeRecord.dataUpgradeRecord.name.eq(dataUpgrade.getName()),
                QDataUpgradeRecord.dataUpgradeRecord.done.isTrue())
            .fetch()
            .size()
        > 0;
  }

  @Override
  public List<DataUpgradeRecord> listAutoPerform() {
    return new JPAQuery<>(getEntityManager())
        .select(QDataUpgradeRecord.dataUpgradeRecord)
        .from(QDataUpgradeRecord.dataUpgradeRecord)
        .where(
            QDataUpgradeRecord.dataUpgradeRecord.autoPerform.isTrue(),
            QDataUpgradeRecord.dataUpgradeRecord.done.isFalse())
        .orderBy(QDataUpgradeRecord.dataUpgradeRecord.id.asc())
        .fetch();
  }
}

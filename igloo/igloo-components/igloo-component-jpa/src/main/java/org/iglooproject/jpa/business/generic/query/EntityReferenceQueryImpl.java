package org.iglooproject.jpa.business.generic.query;

import com.google.common.primitives.Ints;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityReferenceQueryImpl<T extends GenericEntity<?, ?>>
    implements IEntityReferenceQuery<T> {

  private GenericEntityCollectionReference<?, T> reference;

  @Autowired private IEntityService entityService;

  @Override
  public EntityReferenceQueryImpl<T> setReference(
      GenericEntityCollectionReference<?, T> reference) {
    this.reference = reference;
    return this;
  }

  @Override
  @Transactional(readOnly = true)
  public List<T> fullList() {
    return entityService.listEntity(reference);
  }

  @Override
  @Transactional(readOnly = true)
  public List<T> list(long offset, long limit) {
    GenericEntityCollectionReference<?, T> subset =
        reference.subset(Ints.checkedCast(offset), Ints.checkedCast(limit));
    return entityService.listEntity(subset);
  }

  @Override
  public long count() {
    return reference.getEntityIdList().size();
  }
}

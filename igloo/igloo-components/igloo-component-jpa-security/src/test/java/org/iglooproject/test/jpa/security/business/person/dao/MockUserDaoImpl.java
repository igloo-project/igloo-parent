package org.iglooproject.test.jpa.security.business.person.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.QMockUser;
import org.springframework.stereotype.Repository;

@Repository
public class MockUserDaoImpl extends GenericEntityDaoImpl<Long, MockUser> implements IMockUserDao {

  private static final QMockUser qMockUser = QMockUser.mockUser;

  @Override
  public MockUser getByUsernameCaseInsensitive(String username) {
    return new JPAQuery<MockUser>(getEntityManager())
        .from(qMockUser)
        .where(qMockUser.username.lower().eq(username.toLowerCase()))
        .fetchOne();
  }

  @Override
  public Long countEnabled() {
    return countByField(qMockUser, qMockUser.enabled, true);
  }
}

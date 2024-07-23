package org.iglooproject.test.jpa.security.business.person.dao;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.dao.GenericUserGroupDaoImpl;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.business.person.model.QMockUser;
import org.iglooproject.test.jpa.security.business.person.model.QMockUserGroup;
import org.springframework.stereotype.Repository;

@Repository
public class MockUserGroupDaoImpl extends GenericUserGroupDaoImpl<MockUserGroup, MockUser>
    implements IMockUserGroupDao {

  @Override
  public List<MockUser> listUsersByUserGroup(MockUserGroup group)
      throws ServiceException, SecurityServiceException {
    JPQLQuery<MockUser> query = new JPAQuery<>(getEntityManager());

    query
        .from(QMockUser.mockUser)
        .join(QMockUser.mockUser.groups, QMockUserGroup.mockUserGroup)
        .where(QMockUserGroup.mockUserGroup.eq(group));
    List<MockUser> result = query.distinct().fetch();
    sort(result);
    return result;
  }
}

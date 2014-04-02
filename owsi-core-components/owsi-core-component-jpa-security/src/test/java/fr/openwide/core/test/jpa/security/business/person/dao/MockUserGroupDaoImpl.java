package fr.openwide.core.test.jpa.security.business.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.dao.GenericUserGroupDaoImpl;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;
import fr.openwide.core.test.jpa.security.business.person.model.QMockUser;
import fr.openwide.core.test.jpa.security.business.person.model.QMockUserGroup;

@Repository("mockPersonGroupDao")
public class MockUserGroupDaoImpl extends GenericUserGroupDaoImpl<MockUserGroup, MockUser> implements IMockUserGroupDao {
	
	@Override
	public List<MockUser> listUsersByUserGroup(MockUserGroup group) throws ServiceException, SecurityServiceException {
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(QMockUser.mockUser).join(QMockUser.mockUser.groups, QMockUserGroup.mockUserGroup)
				.where(QMockUserGroup.mockUserGroup.eq(group));
		List<MockUser> result = query.distinct().list(QMockUser.mockUser);
		sort(result);
		return result;
	}

}

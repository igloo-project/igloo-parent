package org.iglooproject.basicapp.web.application.common.typedescriptor.user;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationBasicUserListPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationTechnicalUserDetailPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationTechnicalUserListPage;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserListTemplate;
import org.iglooproject.basicapp.web.application.common.typedescriptor.AbstractGenericEntityChildTypeDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public abstract class AdministrationUserTypeDescriptor<U extends User> extends
		AbstractGenericEntityChildTypeDescriptor<UserTypeDescriptor<U>, U> {

	private static final long serialVersionUID = -1128901861897146296L;

	public static final AdministrationUserTypeDescriptor<TechnicalUser> TECHNICAL_USER = new AdministrationUserTypeDescriptor<TechnicalUser>(
			UserTypeDescriptor.TECHNICAL_USER, AdministrationTechnicalUserDetailPage.class,
			AdministrationTechnicalUserListPage.class) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return TECHNICAL_USER;
		}
		
		@Override
		public TechnicalUser newInstance() {
			return new TechnicalUser();
		}
	};

	public static final AdministrationUserTypeDescriptor<BasicUser> BASIC_USER = new AdministrationUserTypeDescriptor<BasicUser>(
			UserTypeDescriptor.BASIC_USER, AdministrationBasicUserDetailPage.class,
			AdministrationBasicUserListPage.class) {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected Object readResolve() {
			return BASIC_USER;
		}
		
		@Override
		public BasicUser newInstance() {
			return new BasicUser();
		}
	};

	private final Class<? extends AdministrationUserDetailTemplate<U>> detailPageClazz;

	private final Class<? extends AdministrationUserListTemplate<U>> listPageClazz;

	private AdministrationUserTypeDescriptor(UserTypeDescriptor<U> typeDescriptor,
			Class<? extends AdministrationUserDetailTemplate<U>> detailPageClazz,
			Class<? extends AdministrationUserListTemplate<U>> listPageClazz) {
		super(typeDescriptor);
		this.detailPageClazz = detailPageClazz;
		this.listPageClazz = listPageClazz;
	}

	public Class<? extends AdministrationUserDetailTemplate<U>> getDetailPageClass() {
		return detailPageClazz;
	}

	public Class<? extends AdministrationUserListTemplate<U>> getListPageClass() {
		return listPageClazz;
	}

	public IPageLinkDescriptor list() {
		return LinkDescriptorBuilder.start().page(listPageClazz);
	}

	public abstract U newInstance();

}

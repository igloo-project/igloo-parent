package fr.openwide.core.basicapp.core.business.user.difference.service;

import org.bindgen.BindingRoot;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.difference.service.AbstractGenericEntityDifferenceServiceImpl;

@Service
public class UserDifferenceServiceImpl extends AbstractGenericEntityDifferenceServiceImpl<User>
		implements IUserDifferenceService {

	@Override
	protected Iterable<? extends AbstractCoreBinding<? extends User, ?>> getSimpleInitializationFieldsBindings() {
		return ImmutableList.of(
				Bindings.user().firstName(),
				Bindings.user().lastName(),
				Bindings.user().email(),
				Bindings.user().locale(),
				Bindings.user().groups()
		);
	}
	
	@Override
	protected Iterable<? extends BindingRoot<? super User, ?>> getMinimalDifferenceFieldsBindings() {
		return ImmutableList.of(
				Bindings.user().firstName(),
				Bindings.user().lastName(),
				Bindings.user().email(),
				Bindings.user().locale(),
				Bindings.user().groups()
		);
	}

}

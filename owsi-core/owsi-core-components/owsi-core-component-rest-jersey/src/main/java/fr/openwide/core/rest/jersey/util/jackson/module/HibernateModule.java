package fr.openwide.core.rest.jersey.util.jackson.module;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import fr.openwide.core.rest.jersey.util.jackson.serializer.HibernateBeanSerializerModifier;

public class HibernateModule extends Module {

	@Override
	public String getModuleName() {
		return "hibernate";
	}

	@Override
	public Version version() {
		return new Version(0, 0, 1, "", "", "");
	}

	@Override
	public void setupModule(SetupContext context) {
		context.addBeanSerializerModifier(new HibernateBeanSerializerModifier());
	}

}

package fr.openwide.core.test.person;

import java.util.List;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.model.PersonSubTypeA;
import fr.openwide.core.test.jpa.example.business.person.model.QPerson;

public class TestMetaModel extends AbstractJpaCoreTestCase {

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		try {
			super.testMetaModel();
			Assert.fail("");
		} catch (IllegalStateException e) {
			
		}
		
		// lien vers un objet non primitif
		EntityType<?> personEntityType = getEntityManager().getMetamodel().entity(Person.class);
		Attribute<?, ?> otherPersonAttribute = personEntityType.getAttribute(QPerson.person.otherPerson.getMetadata().getName());
		try {
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(otherPersonAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers un objet de type inconnu (car va être sérialisé en base)
		}
		
		{
			List<Class<?>> classes = Lists.newArrayList();
			classes.add(PersonSubTypeA.class);
			super.testMetaModel(otherPersonAttribute, classes);
		}
		
		try {
			// lien vers une énumération ordinale
			Attribute<?, ?> enumerationAttribute = personEntityType.getAttribute(QPerson.person.enumeration.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une énumération textuelle
			Attribute<?, ?> enumerationStringAttribute = personEntityType.getAttribute(QPerson.person.enumerationString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationStringAttribute, classes);
		}
		
		try {
			// lien vers une liste énumération ordinale
			Attribute<?, ?> enumerationListAttribute = personEntityType.getAttribute(QPerson.person.enumList.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationListAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une liste d'énumération ordinale
		}
		
		{
			// lien vers une liste énumération textuelle
			Attribute<?, ?> enumerationListStringAttribute = personEntityType.getAttribute(QPerson.person.enumListString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationListStringAttribute, classes);
		}
		
		try {
			// lien vers une map clé d'énumération ordinale
			Attribute<?, ?> enumerationMapAttribute = personEntityType.getAttribute(QPerson.person.enumMap.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une map clé d'énumération textuelle
			Attribute<?, ?> enumerationMapStringAttribute = personEntityType.getAttribute(QPerson.person.enumMapString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapStringAttribute, classes);
		}
		
		try {
			// lien vers une map valeur énumération ordinale
			Attribute<?, ?> enumerationMapValueAttribute = personEntityType.getAttribute(QPerson.person.enumMapValue.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapValueAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une map clé d'énumération textuelle
			Attribute<?, ?> enumerationMapValueStringAttribute = personEntityType.getAttribute(QPerson.person.enumMapValueString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapValueStringAttribute, classes);
		}
	}

}

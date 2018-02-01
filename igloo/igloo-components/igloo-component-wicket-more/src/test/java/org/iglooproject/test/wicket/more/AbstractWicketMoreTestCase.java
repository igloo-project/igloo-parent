package org.iglooproject.test.wicket.more;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.util.tester.WicketTester;
import org.javatuples.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.wicket.more.business.person.service.IPersonService;
import org.iglooproject.test.wicket.more.config.spring.SimpleWicketMoreTestWebappConfig;
import org.iglooproject.test.wicket.more.junit.IWicketTestCase;
import org.iglooproject.test.wicket.more.junit.WicketTesterTestExecutionListener;

@ContextConfiguration(classes = SimpleWicketMoreTestWebappConfig.class)
@TestExecutionListeners({ WicketTesterTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext
public abstract class AbstractWicketMoreTestCase extends AbstractTestCase implements IWicketTestCase {
	
	private WicketTester wicketTester;
	
	@Autowired
	private IPersonService personService;

	@Override
	public void setWicketTester(WicketTester wicketTester) {
		this.wicketTester = wicketTester;
	}

	public WicketTester getWicketTester() {
		return wicketTester;
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(personService);
	}

	protected static <D extends IDetachable> D serializeAndDeserialize(D object) {
		object.detach();
		
		return doSerializeAndDeserialize(object);
	}

	protected static <T extends Tuple> T serializeAndDeserialize(T tuple) {
		for (Object value : tuple) {
			if (value instanceof IDetachable) {
				((IDetachable)value).detach();
			}
		}
		return doSerializeAndDeserialize(tuple);
	}

	@SuppressWarnings("unchecked")
	private static <T> T doSerializeAndDeserialize(T object) {
		byte[] array;
		try {
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
			objectOut.writeObject(object);
			array = arrayOut.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Error while serializing " + object, e);
		}
	
		try {
			ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
			ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
			return (T) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}

}
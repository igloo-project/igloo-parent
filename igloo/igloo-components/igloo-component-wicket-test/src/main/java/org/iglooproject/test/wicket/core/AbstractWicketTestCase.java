package org.iglooproject.test.wicket.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.wicket.core.junit.IWicketTestCase;
import org.javatuples.Tuple;

public abstract class AbstractWicketTestCase<T extends WicketTester> extends AbstractTestCase
    implements IWicketTestCase<T> {

  protected T tester;

  @Override
  public void setWicketTester(T tester) {
    this.tester = tester;
  }

  public T getWicketTester() {
    return tester;
  }

  protected static <D extends IDetachable> D serializeAndDeserialize(D object) {
    object.detach();

    return doSerializeAndDeserialize(object);
  }

  protected static <T extends Tuple> T serializeAndDeserialize(T tuple) {
    for (Object value : tuple) {
      if (value instanceof IDetachable) {
        ((IDetachable) value).detach();
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

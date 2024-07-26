package test.wicket.more.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.base.Equivalence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.wicket.model.IModel;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.service.IPersonService;

public abstract class AbstractTestGenericEntityModel extends AbstractTestModel<Person> {

  private Equivalence<? super Person> equivalence = Equivalence.equals();

  @Autowired private IPersonService personService;

  @PersistenceContext private EntityManager entityManager;

  protected abstract IModel<Person> createModel();

  protected abstract IModel<Person> createModel(Person person);

  @Test
  public void testNotAttached() {
    IModel<Person> model = createModel();
    model = serializeAndDeserialize(model);
    assertNull(model.getObject());
  }

  @Test
  public void testAttachedNull() {
    IModel<Person> model = createModel(null);
    model = serializeAndDeserialize(model);
    assertNull(model.getObject());
  }

  @Test
  public void testAttachedWhenTransient() {
    Person person = new Person("John", "Doe");
    IModel<Person> model = createModel(person);
    model = serializeAndDeserialize(model);
    assertThat(model.getObject(), CoreMatchers.not(isEquivalent(equivalence, person)));
  }

  @Test
  public void testAttachedWhenPersisted() throws Exception {
    Person person = new Person("John", "Doe");
    personService.create(person);
    assertThat(person, isAttachedToSession());

    IModel<Person> model = createModel(person);
    assertThat(model.getObject(), isAttachedToSession());

    model = serializeAndDeserialize(model);
    assertThat(model.getObject(), isEquivalent(equivalence, person));
    assertThat(person, isAttachedToSession());
    assertThat(model.getObject(), isAttachedToSession());
  }

  @Test
  public void testAttachedWhenTransientAndDetachedWhenPersisted() throws Exception {
    Person person = new Person("John", "Doe");
    IModel<Person> model = createModel(person);

    personService.create(person);
    assertThat(person, isAttachedToSession());
    assertThat(model.getObject(), isAttachedToSession());

    model = serializeAndDeserialize(model);
    assertThat(model.getObject(), isEquivalent(equivalence, person));
    assertThat(person, isAttachedToSession());
    assertThat(model.getObject(), isAttachedToSession());
  }

  @Test
  public void testAttachedWhenPersistedAndDetachedWhenTransient() throws Exception {
    Person person = new Person("John", "Doe");
    personService.create(person);
    assertThat(person, isAttachedToSession());

    IModel<Person> model = createModel(person);
    assertThat(model.getObject(), isAttachedToSession());

    personService.delete(person);

    model = serializeAndDeserialize(model);
    assertNull(model.getObject()); // Tries to load an entity whose id no longer exists => null
  }

  @Test
  public void testDetachedWhenTransientThenDetachedWhenPersisted() throws Exception {
    Person person = new Person("John", "Doe");
    IModel<Person> model = createModel(person);

    model.detach(); // First detach()

    // Simulate work on the same object obtained from another model
    personService.create(person);
    assertThat(person, isAttachedToSession());

    model = serializeAndDeserialize(model); // Includes a second detach()
    assertThat(model.getObject(), isEquivalent(equivalence, person));
    assertThat(person, isAttachedToSession());
    assertThat(model.getObject(), isAttachedToSession());
  }
}

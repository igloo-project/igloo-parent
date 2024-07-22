package test.search;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Indexed
public class EnumCollectionEntity {

  @Id @GeneratedValue private Long id;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @GenericField(projectable = Projectable.YES)
  public List<MyEnum> enums = new ArrayList<>();

  @ElementCollection
  @Enumerated(EnumType.ORDINAL)
  @GenericField(projectable = Projectable.YES)
  public List<MyEnum> enumsInt = new ArrayList<>();

  public static enum MyEnum {
    VALUE1,
    VALUE2,
    VALUE3;
  }
}

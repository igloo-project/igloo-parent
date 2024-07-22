package test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.igloo.storage.impl.FichierPathStrategy;
import org.igloo.storage.impl.LegacyFichierPathStrategy;
import org.igloo.storage.model.Fichier;
import org.junit.jupiter.api.Test;
import test.model.FichierType1;

class TestPathStrategy extends AbstractTest {

  // Generated from 'UUID.randomUUID()'
  private static final UUID uuid = UUID.fromString("6559c149-32b5-474d-9cf5-980e71bd4701");

  @Test
  void testFichierPathStrategy() {
    Fichier fichier = new Fichier();
    fichier.setUuid(uuid);
    fichier.setType(FichierType1.CONTENT1);

    FichierPathStrategy pathStrategy = new FichierPathStrategy(1);
    String path = pathStrategy.getPath(fichier);
    assertThat(path).isEqualTo("content1/59/6559c149-32b5-474d-9cf5-980e71bd4701");
  }

  @Test
  void testLegacyFichierPathStrategy() {
    {
      Fichier fichier = new Fichier();
      fichier.setId(29706L);
      fichier.setFilename("29706.png");
      fichier.setType(FichierType1.CONTENT1);

      LegacyFichierPathStrategy legacyPathStrategy = new LegacyFichierPathStrategy(1);
      String path = legacyPathStrategy.getPath(fichier);
      assertThat(path).isEqualTo("content1/bb/29706.png");
    }
    {
      Fichier fichier = new Fichier();
      fichier.setId(3675L);
      fichier.setFilename("3675.jpg");
      fichier.setType(FichierType1.CONTENT1);

      LegacyFichierPathStrategy legacyPathStrategy = new LegacyFichierPathStrategy(1);
      String path = legacyPathStrategy.getPath(fichier);
      assertThat(path).isEqualTo("content1/84/3675.jpg");
    }
  }
}

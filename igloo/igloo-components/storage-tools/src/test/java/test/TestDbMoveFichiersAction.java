package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.igloo.storage.tools.util.FichierUtil.SwitchToUnavailable;
import org.igloo.storage.tools.util.action.DbMoveFichiersAction;
import org.junit.jupiter.api.Test;
import test.model.FichierType;
import test.model.StorageUnitType;

class TestDbMoveFichiersAction {
  /** Check correct database update for {@link MoveResult}. */
  @Test
  void testDbMoveFichiersAction_switchToUnavailable() {
    testDbMoveFichiersAction(SwitchToUnavailable.YES);
  }

  @Test
  void testDbMoveFichiersAction_noSwitchToUnavailable() {
    testDbMoveFichiersAction(SwitchToUnavailable.NO);
  }

  /** Check correct database update for {@link MoveResult}. */
  @SuppressWarnings("unchecked")
  void testDbMoveFichiersAction(SwitchToUnavailable switchToUnavailable) {
    StorageUnit detachedTarget = new StorageUnit();
    detachedTarget.setType(StorageUnitType.TARGET);
    StorageUnit attachedTarget = new StorageUnit();
    attachedTarget.setType(StorageUnitType.TARGET);
    EntityManager entityManager = mock(EntityManager.class);
    DatabaseOperations databaseOperations = mock(DatabaseOperations.class);
    Fichier fichier1 = new Fichier();
    fichier1.setId(1l);
    fichier1.setUuid(UUID.randomUUID());
    fichier1.setType(FichierType.TYPE1);
    Fichier fichier2 = new Fichier();
    fichier2.setId(2l);
    fichier2.setUuid(UUID.randomUUID());
    fichier2.setType(FichierType.TYPE1);
    Fichier fichier3 = new Fichier();
    fichier3.setId(3l);
    fichier3.setUuid(UUID.randomUUID());
    fichier3.setType(FichierType.TYPE1);
    Fichier fichier4 = new Fichier();
    fichier4.setId(4l);
    fichier4.setUuid(UUID.randomUUID());
    fichier4.setType(FichierType.TYPE1);
    Fichier fichier5 = new Fichier();
    fichier5.setId(5l);
    fichier5.setUuid(UUID.randomUUID());
    fichier5.setType(FichierType.TYPE2);
    DbMoveFichiersAction action =
        new DbMoveFichiersAction(
            switchToUnavailable,
            detachedTarget,
            Map.of(
                1l,
                MoveResult.ALREADY_MOVED,
                2l,
                MoveResult.MISSING,
                3l,
                MoveResult.MOVE_FAILED,
                4l,
                MoveResult.MOVED,
                5l,
                MoveResult.UNTOUCHED));

    TypedQuery<Fichier> typedQuery = mock(TypedQuery.class);
    when(entityManager.createQuery(any(), (Class<Fichier>) any())).thenReturn(typedQuery);
    when(typedQuery.getResultList())
        .thenReturn(List.of(fichier1, fichier2, fichier3, fichier4, fichier5));
    // check that attached target is used and not detached target
    when(databaseOperations.getStorageUnit(any())).thenReturn(attachedTarget);

    action.perform(entityManager, databaseOperations);
    // untouched, move_failed -> no update
    assertThat(fichier3.getStorageUnit()).isNull();
    assertThat(fichier3.getStatus()).isNull();
    assertThat(fichier5.getStorageUnit()).isNull();
    assertThat(fichier5.getStatus()).isNull();
    // already-moved, missing, moved -> updated
    assertThat(fichier1.getStorageUnit()).isSameAs(attachedTarget);
    assertThat(fichier2.getStorageUnit()).isSameAs(attachedTarget);
    assertThat(fichier4.getStorageUnit()).isSameAs(attachedTarget);
    if (SwitchToUnavailable.YES.equals(switchToUnavailable)) {
      assertThat(fichier1.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
      assertThat(fichier2.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
      assertThat(fichier4.getStatus()).isEqualTo(FichierStatus.UNAVAILABLE);
    } else {
      assertThat(fichier1.getStatus()).isNull();
      assertThat(fichier2.getStatus()).isNull();
      assertThat(fichier4.getStatus()).isNull();
    }
  }
}

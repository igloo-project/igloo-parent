package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.igloo.storage.tools.util.action.DbMoveFichiersAction;
import org.junit.jupiter.api.Test;

class TestDbMoveFichiersAction {
  /** Check correct database update for {@link MoveResult}. */
  @SuppressWarnings("unchecked")
  @Test
  void testDbMoveFichiersAction() {
    StorageUnit detachedTarget = new StorageUnit();
    StorageUnit attachedTarget = new StorageUnit();
    EntityManager entityManager = mock(EntityManager.class);
    DatabaseOperations databaseOperations = mock(DatabaseOperations.class);
    Fichier fichier1 = new Fichier();
    fichier1.setId(1l);
    Fichier fichier2 = new Fichier();
    fichier2.setId(2l);
    Fichier fichier3 = new Fichier();
    fichier3.setId(3l);
    Fichier fichier4 = new Fichier();
    fichier4.setId(4l);
    Fichier fichier5 = new Fichier();
    fichier5.setId(5l);
    DbMoveFichiersAction action =
        new DbMoveFichiersAction(
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
    assertThat(fichier5.getStorageUnit()).isNull();
    // already-moved, missing, moved -> updated
    assertThat(fichier1.getStorageUnit()).isSameAs(attachedTarget);
    assertThat(fichier2.getStorageUnit()).isSameAs(attachedTarget);
    assertThat(fichier4.getStorageUnit()).isSameAs(attachedTarget);
  }
}

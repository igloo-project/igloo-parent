package org.igloo.storage.tools;

import jakarta.persistence.EntityManager;
import java.util.function.Function;

public interface EntityManagerHelper {

  <T> T doWithTransaction(Function<EntityManager, T> consumer);
}
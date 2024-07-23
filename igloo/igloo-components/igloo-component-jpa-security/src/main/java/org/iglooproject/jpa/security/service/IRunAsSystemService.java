package org.iglooproject.jpa.security.service;

import java.util.concurrent.Callable;

public interface IRunAsSystemService {

  <T> T runAsSystem(Callable<T> task);
}

package org.iglooproject.basicapp.core.business.upgrade.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataUpgradeApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DataUpgradeApplicationListener.class);

  @Autowired private IDataUpgradeManager dataUpgradeManager;

  /** Automatically launches data upgrades at startup */
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (event != null
        && event.getSource() != null
        && AbstractApplicationContext.class.isAssignableFrom(event.getSource().getClass())
        && ((AbstractApplicationContext) event.getSource()).getParent() == null) {
      init();
    }
  }

  private void init() {
    try {
      dataUpgradeManager.autoPerformDataUpgrades();
    } catch (Exception e) {
      LOGGER.error("Error executing data upgrades", e);
    }
  }
}

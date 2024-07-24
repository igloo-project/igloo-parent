package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.datepickersync;

public enum DatePickerSyncActionOnUpdate {

  /**
   * Pas de modification de la date si les dates suivantes et précédentes sont incohérentes avec la
   * date du champ. Toutefois, au moment du clic dans le champ, les mécanismes dateMin / dateMax
   * peuvent mettre à jour le champ.
   */
  NOTHING,

  /**
   * Si les dates suivantes ou précédentes ne sont pas cohérentes avec la date courante, alors la
   * date courante est modifiée par la date mini ou maxi (en fonction du champ modifié, précédent ou
   * suivant)
   */
  UPDATE,

  /**
   * Si les dates suivantes ou précédentes ne sont pas cohérentes avec la date courante, alors la
   * date courante est vidée
   */
  EMPTY;
}

package basicapp.back.business.history.model.atomic;

public enum HistoryEventType {
  CREATE,
  UPDATE,
  DELETE,
  DISABLE,
  ENABLE,
  SIGN_IN,
  SIGN_IN_FAIL,
  PASSWORD_RESET_REQUEST,
  PASSWORD_CREATION_REQUEST,
  PASSWORD_UPDATE;
}

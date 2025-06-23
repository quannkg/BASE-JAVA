package webapi.infrastructure.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

  public static class MoocRegex {
    public static final String PHONE_REGEX =
            "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
    public static final String EMAIL_REGEX = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
  }

  public static class GlobalConfigConstants {
    public static final String NUMBERS_INCORRECT_PASSWORD = "numbers_incorrect_password";
    public static final String MINUTES_LOCK_ACCOUNT = "minutes_lock_account";
    public static final String TIMEOUT = "timeout";
    public static final String DATES_STORE_ACTION_LOGS = "dates_store_action_logs";
    public static final String DATES_NOTIFICATION_CHANGE_PASSWORD =
            "dates_notification_change_password";
    public static final String DATES_MUST_CHANGE_PASSWORD = "dates_must_change_password";
    public static final String PASSWORD_RULE = "password_rule";

    public static final String IS_ENABLE_ENCODE_PASSWORD_BY_RSA = "is_enable_encode_password_by_rsa";

    public static final String IS_FIRST_LOGIN = "isFirstLogin";
  }
  public static class MoocPosition {
    public static final String USER = "is_user";
    public static final String ADMIN = "is_admin";
  }
  public static class APIKeyIntegrated {
    public static final String GEMINI = "api_key_gemini";
    public static final String OPENAI = "api_key_openai";
  }
}

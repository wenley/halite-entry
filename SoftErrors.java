public class SoftErrors {
  private static final boolean swallowErrors = false;

  public static <T> T throwOrDefault(T defaultValue, RuntimeException exception) {
    if (swallowErrors) {
      return defaultValue;
    } else {
      throw exception;
    }
  }
}

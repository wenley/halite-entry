import java.io.PrintWriter;

public class Logger {
  private static PrintWriter writer;

  public static void initialize() throws java.io.IOException {
    writer = new PrintWriter("my-bot.txt", "UTF-8");
  }

  public static void log(String message) {
    if (writer != null) {
      writer.println(message);
    }
  }
}

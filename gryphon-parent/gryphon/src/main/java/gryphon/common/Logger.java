package gryphon.common;

/**
 * <p>
 * Ëמדדונ
 * </p>
 * @author Evgueni Tiourine
 */

public class Logger {
  public static final int DEBUG = 4;
  public static final int INFO = 3;
  public static final int WARN = 2;
  public static final int ERR = 1;
  public static final int NONE = 0;

  private static int level = DEBUG;

  public Logger() {
  }
  public static void init(int level) {
    Logger.level = level;
  }
  public static void log(String s) {
    debug(s);
  }
  public static void debug(String s) {
    if (level >= DEBUG) {
      System.out.println(s);
    }
  }
  public static void info(String s) {
    if (level >= INFO) {
      System.out.println(s);
    }
  }
  public static void warn(String s) {
    if (level >= WARN) {
      System.out.println(s);
    }
  }
  public static void err(String s) {
    if (level >= ERR) {
      System.out.println(s);
    }
  }
  public static void logThrowable(Throwable th) {
    err(th.getClass().getName()+": "+th.getMessage());
    th.printStackTrace();
  }

}
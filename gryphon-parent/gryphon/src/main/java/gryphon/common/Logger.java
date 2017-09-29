package gryphon.common;

import java.util.logging.Level;

/**
 * <p>
 * ������
 * </p>
 * 
 * @author Evgueni Tiourine
 */

public class Logger
{
	public static final int DEBUG = 4;
	public static final int INFO = 3;
	public static final int WARN = 2;
	public static final int ERR = 1;
	public static final int NONE = 0;

	private static int level = DEBUG;

    /** ������ ��� ���������������, ���� ���� */
    private static java.util.logging.Logger delegate;

    public Logger() {
	}

	public static void init(int level)
	{
		Logger.level = level;
	}

	public static void log(String s)
	{
		debug(s);
	}

	public static void debug(String s)
	{
		if (level >= DEBUG) {
			if (delegate==null)
				System.out.println(s);
			else
				delegate.info(s);
		}
	}

	public static void info(String s)
	{
		if (level >= INFO) {
			if (delegate==null)
				System.out.println(s);
			else
				delegate.info(s);
		}
	}

	public static void warn(String s)
	{
		if (level >= WARN) {
			if (delegate==null)
				System.out.println(s);
			else
				delegate.warning(s);
		}
	}

	public static void err(String s)
	{
		if (level >= ERR) {
			if (delegate==null)
				System.out.println(s);
			else
				delegate.severe(s);
		}
	}

	public static void logThrowable(Throwable th)
	{
		if (delegate==null){
			err(th.getClass().getName() + ": " + th.getMessage());
			th.printStackTrace();
		}
		else{
			delegate.log(Level.SEVERE, "", th);
		}
	}

	/** ������������� ����� � java.util.logging.Logger ��� ����� ������� ���������� */
    public static void redirect(String loggerName){
    	delegate = java.util.logging.Logger.getLogger(loggerName);
    }

}
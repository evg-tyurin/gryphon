package gryphon.common;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Дружественное исключение, текст которого можно показывать пользователю.
 * This class is obsoleted. Use FriendlyException
 * @author Evgueni Tiourine
 * 
 */

public class MyException extends Exception
{
    private Throwable throwable;
    /**@deprecated use FriendlyException*/
    public MyException(String message)
    {
        super(message);
    }
    /**@deprecated use FriendlyException*/
    public MyException(String message, Throwable th)
    {
        super(message);
        this.throwable = th;
    }

    public Throwable getThrowable()
    {
        return throwable;
    }

    public void printStackTrace()
    {
        super.printStackTrace();
        if (getThrowable()!=null)
            getThrowable().printStackTrace();
    }

    public void printStackTrace(PrintStream s)
    {
        super.printStackTrace(s);
        if (getThrowable()!=null)
            getThrowable().printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s)
    {
        super.printStackTrace(s);
        if (getThrowable()!=null)
            getThrowable().printStackTrace(s);
    }

}
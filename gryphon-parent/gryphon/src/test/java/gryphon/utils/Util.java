package gryphon.utils;

import gryphon.common.MyException;

/**
 * @author ET
 */

public class Util
{
    public Util()
    {
    }

    public static String getText(Exception e)
    {
        String text = "";
        if (e instanceof MyException)
        {
            text = e.getMessage();
            Throwable th = ((MyException) e).getThrowable();
            if (th != null)
            {
                th.printStackTrace();
            }
        }
        else
        {
            text = e.getClass().getName() + ": " + e.getMessage();
            e.printStackTrace();
        }
        return text;
    }
}
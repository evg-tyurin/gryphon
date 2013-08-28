package gryphon.common;

/**
 * Дружественное исключение, текст которого можно показывать пользователю.
 * @author ET
 */
public class FriendlyException extends MyException
{

    public FriendlyException(String message)
    {
        super(message);
    }
    public FriendlyException(String message, Throwable th)
    {
        super(message,th);
    }

}

package gryphon.common;

/**
 * ������������� ����������, ����� �������� ����� ���������� ������������.
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

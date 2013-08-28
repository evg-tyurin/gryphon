package gryphon.database;

import java.io.Serializable;

/**
 * ������� ��� �������� ���������� ����������� �������.
 * �������� �������� ��������� �������, �������� ��������, ������� ����� ���������
 * � ��������� ������-����������, ������� ����� ������� ������� ���������� ��� ������,
 * ���� ��������, ������� ����� �������, ���� ��� ���-����.
 * 
 * @author ET
 */
public class Command implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * �������� ��������
	 */
	private String operation;
	/**
	 * �������� (��� ������) ��������� �������
	 */
    private String objectName;
    /**
     * ������-����������
     */
    private Object attachment;
    /**
     * ����� ������ ������������, ������� ����������� ���������� ��������.
     */
    private String sessionNumber;

    public static final String STATUS_FAILED = "status.failed";

    public static final String STATUS_OK = "status.ok";

    /**
     * @param operation
     * @param objectClassName
     * @param attachment
     */
    public Command(String operation, String objectClassName, Object attachment)
    {
        this.operation = operation;
        this.objectName = objectClassName;
        this.attachment = attachment;
    }

    public Command(String operation, Class objectClass, Object attachment)
    {
        this.operation = operation;
        this.objectName = objectClass.getName();
        this.attachment = attachment;
    }

    public Command()
    {

    }
    /**
     * @return Returns the attachment.
     */
    public Object getAttachment()
    {
        return attachment;
    }

    /**
     * @param attachment
     *            The attachment to set.
     */
    public void setAttachment(Object attachment)
    {
        this.attachment = attachment;
    }

    /**
     * @return Returns the objectName.
     */
    public String getObjectName()
    {
        return objectName;
    }

    /**
     * @param objectName
     *            The objectName to set.
     */
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getSessionNumber()
    {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber)
    {
        this.sessionNumber = sessionNumber;
    }
}
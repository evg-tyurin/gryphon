package gryphon.database.remote;

import gryphon.database.Command;

import java.io.Serializable;


public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** Флаг означает, что все операции с сообщением в т.ч. буферизацию можно проводить в памяти */  
    public static final long ALL_IN_MEMORY = 1;

	private String sessionId;

    private Command command;
    
    private long processingTime;

    /** Набор флагов для сообщения */
    private long flags = ALL_IN_MEMORY;

    public Message(Command cmd)
    {
        this.command = cmd;
    }

    public Message()
    {
    }

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public long getProcessingTime()
    {
        return processingTime;
    }

    public void setProcessingTime(long processingTime)
    {
        this.processingTime = processingTime;
    }

	public long getFlags()
	{
		return flags;
	}

	public void setFlags(long flags)
	{
		this.flags = flags;
	}

    public boolean isFlag(long flag){
    	return (flags & flag) == flag; 
    }
    
    public void setFlag(long flag, boolean value){
    	// по-любому включить
    	flags |= flag;
    	// если не нужен, то выключить
    	if (!value)
    		flags ^= flag;
    }
}

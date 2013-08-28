package gryphon.database;

public interface DatabaseBroker
{
    /**
     * Executes specified command and returns the result.
     * Calls to this method are preferred in distributed environment 
     * where client requests database by sending messages.
     * @param command
     * @return result as one more Command object
     */
    Command execute(Command command);
    /**
     * Executes specified command, check for exception and then throws the exception 
     * or just returns the result.
     * Calls to this method are preferred in non-distributed environment 
     * where client requests database directly.
     * 
     * @param cmd
     * @return
     * @throws Exception
     */
    Object executeAndGetResult(Command cmd) throws Exception;
}

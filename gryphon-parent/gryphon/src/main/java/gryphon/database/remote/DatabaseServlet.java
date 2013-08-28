package gryphon.database.remote;

import gryphon.common.Logger;
import gryphon.database.Command;
import gryphon.database.DatabaseBroker;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Abstract servlet that handles incoming database messages.
 * Subclasses must implement one method returning DatabaseBroker.
 * 
 * @author ET
 */
public abstract class DatabaseServlet extends HttpServlet
{
    private int requestSequence;
    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0,
     *      HttpServletResponse arg1)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>IcmConn servlet</h1>");
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest arg0,
     *      HttpServletResponse arg1)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String SID = "unknown";
//        RequestParams params = new RequestParams();
//        params.setProperty(RequestParams.IP_ADDRESS, request.getRemoteAddr());
        //получить порядковый номер запроса
        String requestId = "reqId="+getRequestId()+"; "; 
        request.setAttribute(RequestParams.REQUEST_ID, requestId);

        Date start = new Date();
        Logger.debug(requestId + "doPost started");

        Message outcomingMessage = null;
        int errorCount = 0;
        Exception err = null;
        // read and process request
        try
        {
//            ServerManager.checkServerState();
            Date readStart = new Date();
            Logger.debug(requestId + "start reading message");
            Message incomingMessage = MessageUtil.decompressMessage(request.getInputStream());
            if (incomingMessage != null)
            {
//                ServerManager.checkVersion(incomingMessage);
                SID = NVL(incomingMessage.getSessionId(), "unknown");
            }
            request.setAttribute(RequestParams.SID, SID);
            Logger.debug(requestId + "message restored");

            Logger.debug(requestId + "start processing message");
            outcomingMessage = processMessage(incomingMessage, request);
            Date readFinish = new Date();
            outcomingMessage.setProcessingTime(readFinish.getTime() - readStart.getTime());
            Logger.debug(requestId + "end processing message, delay="
                    + timeDelay(readStart));
        }
        catch (Exception e)
        {
            errorCount++;
            Logger.logThrowable(e);
        }

        // send response
        try
        {
            Date writeStart = new Date();
            Logger.debug(requestId + "start writing message");
            writeMessage(response, outcomingMessage, err);
            Logger.debug(requestId + "end writing message, delay=" + timeDelay(writeStart));
        }
        catch (Exception e)
        {
            errorCount++;
            Logger.logThrowable(e);
        }
        finally
        {
            Logger.debug(requestId + "doPost finished with "
                    + errorCount + " errors, delay=" + timeDelay(start));
        }
    }

    private String NVL(String str, String defaultStr)
    {
        return str!=null?str:defaultStr;
    }

    private String timeDelay(Date start)
    {
        long millis = new Date().getTime()-start.getTime();
        return millis+" ms";
    }
    private void writeMessage(HttpServletResponse response, Message outcomingMessage, Exception err)
            throws IOException
    {
        ServletOutputStream out = response.getOutputStream();
        if (err != null)
        {
            outcomingMessage = new Message();
            outcomingMessage.setCommand(new Command(Command.STATUS_FAILED, "Unknown",err));
        }

        MessageUtil.compressMessage(outcomingMessage, out);
        out.close();
    }

    private Message processMessage(Message incomingMessage, HttpServletRequest request)
            throws Exception
    {
        DatabaseBroker dbBroker = getDatabaseBroker();
//        dbBroker.setRequestParams(params);
        Date start = new Date();
        
        Message outcomingMessage = new Message();
        outcomingMessage.setCommand(dbBroker.execute(incomingMessage.getCommand()));
        Logger.debug(
                (String)request.getAttribute(RequestParams.REQUEST_ID)+
                request.getAttribute(RequestParams.SID)+"; "+ 
                "dbBroker executed OK, delay="
                + timeDelay(start));
        return outcomingMessage;
    }
    protected abstract DatabaseBroker getDatabaseBroker();

    /**
     * получение идентификатора запроса, для выполнения метода doPost
     *
     * @return идентификатор
     */
    synchronized private int getRequestId()
    {
        return requestSequence++;
    }
 
}
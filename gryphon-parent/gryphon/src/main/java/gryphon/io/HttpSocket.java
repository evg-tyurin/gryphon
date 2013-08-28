package gryphon.io;

import gryphon.common.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * «амена дл€ URLConnection дл€ тех случаев, когда требуетс€ передать запрос большого объема.
 *  ак известно, как JRE 1.4.2, так и JRE 1.6 буферизует тело HTTP-запроса, поэтому рано или поздно 
 * при увеличении запроса приходитс€ наблюдать {@link OutOfMemoryError}.
 * @author etyurin
 *
 */
public class HttpSocket
{
    /**
     * HTTP response code
     */
    private int responseCode = -1;
    /**
     * HTTP response message
     */
    private String responseMessage;
    /**
     * HTTP response headers
     */
    private HashMap<String, String> responseHeaders = new HashMap<String, String>();
    /**
     * URL for connecting
     */
    private URL url;
    
    private Socket sock;
    
    private OutputStream out;

    public HttpSocket(URL url) throws UnknownHostException, IOException
	{
    	this.url = url;
    	sock = new Socket(url.getHost(),url.getPort());
		out = sock.getOutputStream();
        Logger.debug("POST "+url.getFile()+" HTTP/1.0");
		write(out, "POST "+url.getFile()+" HTTP/1.0\r\n");
	}

    public InputStream getInputStream() throws IOException
    {
		out.flush();
        // read & parse headers
        //    BufferedReader r = new BufferedReader(new InputStreamReader(in));
        //    ByteArrayOutputStream buf = new ByteArrayOutputStream();
        InputStream in = sock.getInputStream();
        String line = null;
        int count = 0;
        while ((line = readLine(in)) != null)
        {
            count++;
            //      log("line="+line);
            if (line.equals(""))
            {
                break;
            }
            // first line is line of status
            if (count == 1)
            {
                //        StringTokenizer st = new StringTokenizer(line, " ");
                String[] args = line.split(" ");
                //        st.nextToken(); //HTTP/1.x
                this.responseCode = Integer.parseInt(args[1]);
                //            st.nextToken());
                String _responseMessage = "";
                for (int i = 2; i < args.length; i++)
                {
                    _responseMessage += args[i];
                    if (i < args.length - 1)
                    {
                        _responseMessage += " ";
                    }
                }
                this.responseMessage = _responseMessage;
                //            st.nextToken();
                continue;
            }
            String delimiter = ": ";
            int delim = line.indexOf(delimiter);
            if (delim < 0)
            {
                // многострочные заголовки идут на 
                continue;
            }
            String key = line.substring(0, delim).toLowerCase();
            String value = line.substring(delim + delimiter.length());
            this.responseHeaders.put(key, value);
        }
        return in;
    }
    private String readLine(InputStream in) throws IOException
    {
        boolean notNull = false;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int b = -1;
        while ((b = in.read()) > -1 && b != '\r' && b != '\n')
        {
            buf.write(b);
        }
        if (b == '\r')
        {
            in.read();//skip '\n'
            notNull = true;
        }
        if (b == '\n')
        {
            notNull = true;
        }
        String s = null;
        if (notNull)
        {
            s = buf.toString();
        }
        //      System.out.println(s);
        return s;
    }

	public int getResponseCode()
	{
		return responseCode;
	}

	public String getResponseMessage()
	{
		return responseMessage;
	}

	public HashMap<String, String> getResponseHeaders()
	{
		return responseHeaders;
	}

	public Socket getSock()
	{
		return sock;
	}

	public URL getUrl()
	{
		return url;
	}

	public OutputStream getOutputStream() throws IOException
	{
		write(out, "\r\n");
		return out;
	}

	public void close()
	{
		try
		{
			sock.close();
		}
		catch (IOException e)
		{
			Logger.logThrowable(e);
		}
		sock = null;
	}

    private void write(OutputStream outputStream, String string) throws IOException
    {
        outputStream.write(string.getBytes());
    }

	public void setRequestProperty(String name, String value) throws IOException
	{
		write(out, name+": "+value+"\r\n");
	}
	
}

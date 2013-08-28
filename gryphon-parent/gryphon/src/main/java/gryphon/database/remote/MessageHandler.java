package gryphon.database.remote;

import gryphon.common.Logger;
import gryphon.io.HttpSocket;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


/**
 * The object is responsible for sending messages to the server and receiving reply
 * @author Evgeuni Tiourine
 */
public class MessageHandler
{
	private String HTTP_LISTENER_URL;
	/**
	 * Timeout for the connection to the server. In seconds.
	 */
	private int timeout = 60;

	private static HashMap instances = new HashMap();

	private SenderThread sender;
	private long senderStarted;

	private MessageHandler(String prefix)
	{
        ResourceBundle b = ResourceBundle.getBundle(prefix+"messagehandler");
		//get config parameters
		HTTP_LISTENER_URL = b.getString("messagehandler.server.url");
        if (HTTP_LISTENER_URL.startsWith("/"))
            HTTP_LISTENER_URL = System.getProperty("messagehandler.server.url")+HTTP_LISTENER_URL;
		timeout = Integer.parseInt(b.getString("messagehandler.timeout"));
	}

	private MessageHandler(String serverUrl, int timeout)
	{
		HTTP_LISTENER_URL = serverUrl;
		this.timeout = timeout;
	}

	/** Создать экземпляр с адресом сервера из настроечного бандла */
	public static MessageHandler getInstance()
	{
		return getInstance("");
	}
	/** Создать экземпляр с указанием префикса ресурсного бандла */
	public static MessageHandler getInstance(String prefix)
	{
		MessageHandler instance = (MessageHandler) instances.get(prefix);
		if (instance == null)
		{
			instance = new MessageHandler(prefix);
			instances.put(prefix, instance);
		}
		return instance;
	}
	/** Создать экземпляр с явным указанием адреса сервера */
	public static MessageHandler getInstance(String serverUrl, int timeout)
	{
		MessageHandler instance = (MessageHandler) instances.get(serverUrl);
		if (instance == null)
		{
			instance = new MessageHandler(serverUrl, timeout);
			instances.put(serverUrl, instance);
		}
		instance.timeout = timeout;
		return instance;
	}
	/**
	 * Sends given message to the server and returns another <i>Message</i> object as reply.
	 *
	 * @param m message to be sent to the server
	 * @return message returned from the server
	 * @throws Exception
	 */
	public final Message send(Message m) throws Exception
	{
		// можно ли посылать?
		if (getSender() != null)
		{
			// низя
			throw new Exception("Передача данных невозможна, т.к. уже выполняется одна операция.");
		}
		// установим версию клиентского приложения
//		m.setVersion(Util.getSystemVersion());
		// Позырим, не задал ли кто-нибудь специальное значение таймаута. Если да - будем
		// использовать его. Если нет - используем значение из бандла.
		int actualTimeout;
//		if (m.getTimeout()!=null)
//			actualTimeout = m.getTimeout().intValue();
//		else
			actualTimeout = timeout;
		SenderThread sender = new SenderThread(HTTP_LISTENER_URL);
		setSender(sender);
		sender.setRequest(m);
		sender.start();
		///////////// wait for sender stopped ////////////////////
		Logger.debug("wait for sender stopped using timeout = " + actualTimeout);
		for (int i = 1; i <= actualTimeout && sender.isAlive(); i++)
		{
			Logger.debug("wait for sender stopped: i=" + i);
			Thread.sleep(1000);//sleep for 1 sec
		}

		Logger.debug("check ready");
		if (!sender.isReady())
		{
			// или ошибка или тайм-аут кончился
			if (sender.isAlive())
			{
				sender.interrupt();
				setSender(null);
				throw new Exception("Не удается передать данные: закончилось время ожидания.");
			}
			Exception e = sender.getException();
			setSender(null);
			if (e != null)
			{
				e = new Exception("Неизвестная ошибка при передаче данных");
			}
			throw e;
		}
		setSender(null);
		if (sender.getException() != null)
		{
			throw sender.getException();
		}
		return sender.getResponse();
	}

	private synchronized SenderThread getSender()
	{
		return sender;
	}

	private synchronized void setSender(SenderThread senderThread)
	{
		this.sender = senderThread;
	}

	private synchronized long getDelay()
	{
		return System.currentTimeMillis() - senderStarted;
	}

	private void checkConnection() throws Exception
	{
		try
		{
			URL url = new URL(HTTP_LISTENER_URL);
			String protocol = url.getProtocol();
			int port = url.getPort();
			if (port == -1)
			{
				if (protocol.equals("https"))
				{
					port = 443;
				}
				else if (protocol.equals("http"))
				{
					port = 80;
				}
			}
			Logger.debug("protocol=" + protocol);
			Logger.debug("host=" + url.getHost());
			Logger.debug("port=" + port);
			Socket socket = new Socket(url.getHost(), port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("Не удается установить соединение с сервером", e);
		}
	}

	private class SenderThread extends Thread
	{
		private String httpListenerUrl;

		private Message request;
		private Message response;

		private Exception exception;

		private boolean ready = false;
		private boolean outputReady = false;
		private boolean requestSent = false;
		private boolean inputReady = false;

		SenderThread(String httpListenerUrl)
		{
			this.httpListenerUrl = httpListenerUrl;
			senderStarted = System.currentTimeMillis();
		}

		private void send() throws Exception
		{
			Message m = getRequest();
			MessageBuffer buf = new MessageBuffer(m);
			
			HttpSocket conn = null;

			try
			{
				///////////////////////////// COMPRESSING ///////////////////////////////
				buf.compress();
//				ByteArrayOutputStream buf = new ByteArrayOutputStream(1024);
//				MessageUtil.compressMessage(m, buf);
				long outcomingSize = buf.size();
				Logger.debug("Outcoming size: " + outcomingSize);
				///////////////// END COMPRESSING ////////////////////

				URL url = new URL(httpListenerUrl);

				/* prepare HTTP request */

				long startTime = System.currentTimeMillis();
		        conn = new HttpSocket(url);
				startTime = debugTime("connect", startTime);
		        
				String boundary = "---------------------------7d1367273062e";

//				URLConnection conn = url.openConnection();
//				conn.setDoOutput(true);
				conn.setRequestProperty("User-Agent", "Message_Transport");
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				conn.setRequestProperty("Accept", "*/*");
				//conn.setRequestProperty("Content-length", Integer.toString(body.size()));//было так
				conn.setRequestProperty("Content-Length", Long.toString(outcomingSize));//теперь так
				//Util.toOut("setRequestProperty()");
				OutputStream out = conn.getOutputStream();
				outputReady = true;
//				out.write(buf.toByteArray());//теперь так
				buf.copyTo(out);
//				out.close();
				requestSent = true;
				//Util.toOut("os closed.");

				startTime = debugTime("send", startTime);
				InputStream in = conn.getInputStream();
				inputReady = true;

				// эта ботва с буфером нужна [была] только для того, чтобы замерить объем принимаемого сообщения 
				// и затем вычислить скорость передачи
//				buf.reset();
//				FileUtil.copy(in, buf);
//				long receiveDelay = System.currentTimeMillis() - startTime;
//				int incomingSize = buf.size();

//				Message response = MessageUtil.decompressMessage(new ByteArrayInputStream(buf.toByteArray()));
				Message response = MessageUtil.decompressMessage(in);
				// здесь задержка складывается уже из времени приема сообщения и его десериализации 
				startTime = debugTime("receive+deserialize", startTime);
				Logger.debug("Server processing delay: " + response.getProcessingTime());
//				long totalDelay = sendDelay + receiveDelay - response.getProcessingTime();
//				if (totalDelay==0) totalDelay = 1;
//				int speed = Math.round(((outcomingSize + incomingSize) / totalDelay) * 1000);
//				Logger.debug("Speed, bytes/s: " + speed);
//				response.setRoundTripSpeed(speed);
				setResponse(response);
				Logger.debug("message deserialized");
				ready = true;
			}
//			catch (LopsidedMessageException e)
//			{
//				throw new Exception("C сервера получено невосстанавливаемое сообщение", e);
//			}
			catch (FileNotFoundException e)
			{
				throw new Exception("Не удается установить соединение с сервером. Адрес не найден: " + httpListenerUrl, e);
			}
			catch (Exception e)
			{
				Logger.logThrowable(e);
				throw new Exception("Ошибка при соединении с сервером.", e);
			}
			finally{
				if (conn!=null)
					conn.close();
				buf.close();
			}
		}

	    private long debugTime(String text, long startTime)
		{
			Logger.debug(text+", ms: " + (System.currentTimeMillis()-startTime));
			return System.currentTimeMillis();
		}

		public void run()
		{
			try
			{
				this.send();
			}
			catch (Exception ex)
			{
//				exception.printStackTrace();
				setException(ex);
				ready = true;
			}
		}

		synchronized void setResponse(Message response)
		{
			this.response = response;
		}

		synchronized void setRequest(Message request)
		{
			this.request = request;
		}

		synchronized Message getResponse()
		{
			return this.response;
		}

		synchronized Message getRequest()
		{
			return this.request;
		}

		synchronized Exception getException()
		{
			return this.exception;
		}

		synchronized void setException(Exception exception)
		{
			this.exception = exception;
		}

		synchronized boolean isReady()
		{
			return ready;
		}

		synchronized boolean isOutputReady()
		{
			return outputReady;
		}

		synchronized boolean isRequestSent()
		{
			return requestSent;
		}

		synchronized boolean isInputReady()
		{
			return inputReady;
		}
	}
}


package gryphon.database.remote;

import gryphon.common.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;


/**
 * @author Evgueni Tiourine
 *  
 */
public class MessageUtil
{

	public static void writeMessage(Message m, OutputStream out)
			throws IOException
	{
		ObjectOutputStream obj = new ObjectOutputStream(out);
		obj.writeObject(m);
		obj.flush();
	}

	public static void compressMessage(Message m, OutputStream out)
			throws IOException
	{
		DeflaterOutputStream dos = new DeflaterOutputStream(out, new Deflater());
		// Alternately set the dictionary to see its effects.
		//def.setDictionary(dictionary.getBytes());
		//Util.toOut("checksum of dictionary: " + def.getAdler());
		ObjectOutputStream obj = new ObjectOutputStream(dos);
		obj.writeObject(m);
		obj.flush();
		// Finish but don't close
		dos.finish();
		dos.flush();
	}

	public static Message decompressMessage(InputStream in)
			throws Exception
	{
		try {
			Inflater inflater = new Inflater();
			InflaterInputStream iis = new InflaterInputStream(in, inflater);
			ObjectInputStream obj = new ObjectInputStream(iis);
			return (Message) obj.readObject();
		}
		catch (Exception e) {
			Logger.logThrowable(e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * @param in
	 * @return
	 * @throws LopsidedMessageException
	 */
	public static Message readMessage(InputStream in)
			throws Exception
	{
		try {
			ObjectInputStream obj = new ObjectInputStream(in);
			return (Message) obj.readObject();
		}
		catch (Exception e) {
			Logger.logThrowable(e);
			throw new Exception(e.getMessage());
		}
	}

}
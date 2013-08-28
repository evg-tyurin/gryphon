package gryphon.database.remote;

import gryphon.io.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageBuffer
{
	private ByteArrayOutputStream byteArray;
	private File bufFile;
	private FileOutputStream fileStream;
	private Message message;
	
	public MessageBuffer(Message message){
		this.message = message;
	}

	public void compress() throws IOException
	{
		if (message.isFlag(Message.ALL_IN_MEMORY)){
			byteArray = new ByteArrayOutputStream(1024);
			MessageUtil.compressMessage(message, byteArray);		
		}
		else{
			bufFile = File.createTempFile("message", ".dat");
			fileStream = new FileOutputStream(bufFile);
			MessageUtil.compressMessage(message, fileStream);
			fileStream.close();
		}
	}

	public long size()
	{
		if (byteArray!=null)
			return byteArray.size();
		return bufFile.length();
	}

	public void copyTo(OutputStream out) throws IOException
	{
		if (byteArray!=null){
			byteArray.writeTo(out);
		}
		else{
			FileInputStream in = new FileInputStream(bufFile);
			FileUtil.copy(in, out);
			in.close();
		}
	}

	public void close()
	{
		if (byteArray!=null){
			byteArray.reset();
			byteArray = null;
		}
		if (bufFile!=null){
			if (!bufFile.delete())
				bufFile.deleteOnExit();
		}
	}
}

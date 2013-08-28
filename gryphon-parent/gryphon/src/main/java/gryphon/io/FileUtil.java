package gryphon.io;

import gryphon.common.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FileUtil
{
    private static HashMap<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();
    /**
     * @param in
     * @return массив байт
     * @throws IOException
     */
    public static byte[] read(InputStream in) throws IOException
    {
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		copy(in,response);
		in.close();
        return response.toByteArray();
    }
    /**
     * Ищет файл в списке директорий
     * @param dirs где искать
     * @param filename что искать
     * @return полный путь к первому найденному файлу
     */
    public static String lookFile(String[] dirs, String filename)
    {
        for (int i=0; i<dirs.length; i++) {
            String fname = dirs[i]+File.separatorChar+filename;
            File file = new File(fname);
            if (file.exists()) {
                return fname;
            }
        }
        return null;
    }
    /**
     * Копирует ресурс из урла в указанный файл
     * @param resource откуда брать
     * @param target куда положить
     * @throws IOException
     */
    public static void copyResource(String resource, String target) throws IOException
    {
        FileOutputStream targetFile = new FileOutputStream(target);
        InputStream in = FileUtil.class.getResourceAsStream(resource);
//      new FileInputStream("C:\\projects\\eapo\\implementation\\EAPOAPPLICATION\\lib\\jacob.jar");
		int b = -1;
		while ((b=in.read())>-1) {
			targetFile.write(b);
		}
		in.close();
		targetFile.close();
    }
    /**
     * @param source
     * @param out
     * @throws IOException
     */
    public static void copyFile(File source, OutputStream out) throws IOException
    {
    	FileInputStream in = new FileInputStream(source);
    	copy(in,out);
    	in.close();
    }
    /**
     * Копирует данные с входного потока на выходной.
     * @param in вход
     * @param out выход
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException
    {
        int b = -1;
        int bufLen = 64*1024;
        byte[] buf = new byte[bufLen];
        while ((b=in.read(buf)) > 0)
        {
            out.write(buf,0,b);
            out.flush();
        }
    }
    public static void copyBytes(InputStream in, OutputStream out, long offset, long length) throws IOException
    {
        int b = -1;
        long bufLenL = 64*1024L;
        int bufLen = (int) bufLenL;
        byte[] buf = new byte[bufLen];
        long total = 0L;
        int len = buf.length;
        if (length-total<bufLenL)
        	len = (int) (length-total);

        in.skip(offset);
        while ((b=in.read(buf,0,len)) > 0)
        {
            out.write(buf,0,b);
            out.flush();
            total += b;
            if (length-total<bufLenL)
            	len = (int) (length-total);
        }
        if (total!=length)
        	throw new RuntimeException("Can't read specified number of bytes: read "+total+" instead of "+length);
    }
    public static void copyBytes(ObjectInput in, OutputStream out, long length) throws IOException
    {
        int b = -1;
        long bufLenL = 64*1024L;
        int bufLen = (int) bufLenL;
        byte[] buf = new byte[bufLen];
        long total = 0L;
        int len = buf.length;
        if (length-total<bufLenL)
        	len = (int) (length-total);

        while ((b=in.read(buf,0,len)) > 0)
        {
            out.write(buf,0,b);
            out.flush();
            total += b;
            if (length-total<bufLenL)
            	len = (int) (length-total);
        }
        if (total!=length)
        	throw new RuntimeException("Can't read specified number of bytes: read "+total+" instead of "+length);
    }
    public static void copy(InputStream in, ObjectOutput out) throws IOException
    {
        int b = -1;
        int bufLen = 64*1024;
        byte[] buf = new byte[bufLen];
        while ((b=in.read(buf)) > 0)
        {
            out.write(buf,0,b);
            out.flush();
        }
    }
    /**
     * @param in
     * @param targetFile
     * @throws IOException
     */
    public static void save2File(InputStream in, File targetFile) throws IOException
    {
        FileOutputStream out = new FileOutputStream(targetFile);
        copy(in,out);
		in.close();
		out.close();
    }
    
    /**
     * достает значение ключа из названного бандла. 
     * @param bundle
     * @param key
     * @return значение ключа из бандла
     */
    public static String getProperty(String bundle, String key) {
        ResourceBundle b = bundles.get(bundle);
        if (b==null) { 
            b = ResourceBundle.getBundle(bundle);
            bundles.put(bundle, b);
        }
        return b.getString(key);
    }
    /**
     * @param j
     * @return число, отформатированное к виду "0000"
     */
    public static String padd4(int j)
    {
//        return padd4(j, "0000");
        String s = ""+j;
        while (s.length()<4) {
            s = "0"+s;
        }
        return s;
    }

    /**
     * Доработка 30.10.2008. abaykov. 8154
     * Если префикс более 4-х знаков (желание заказчика),
     * приводим общее имя к 8-значному
     * @param j
     * @return число, отформатированное к виду "0000"
     */
    public static String padd4(int j, String prefix){
        int prefixSense = 4;
        if(prefix!=null) {
            prefixSense = prefix.length();
            if(prefixSense==0 || prefixSense>7) prefixSense = 4;
        }
        int sense = 8 - prefixSense;
        StringBuffer s = new StringBuffer();
        for(int i=0;i<sense-1;i++){
            s.append("0");
        }
        s.append(j);
        return s.toString();
    }

    /**
     * увеличивает версию, записанную в данном файле под данным ключом.
     * если в файле есть другие ключи, они затираются.
     * @param file
     * @param key
     * @param delta на сколько прирастает версия за вызов метода
     * @throws Exception
     */
    public static void changeVersion(String file, String key, int delta, int max) throws Exception {
        File f = new File(file);
        String s = read(f);
        s = s.replaceAll("[\n\r]","");
        if (!f.delete()) 
            throw new Exception ("can't delete file: "+file);
        String[] keyValue = s.split("=");
        Logger.debug("old "+keyValue[0]+"="+keyValue[1]);
        if (!keyValue[0].equals(key)) 
            throw new Exception ("incorrect key: "+keyValue[0]);
        String[] sVersions = keyValue[1].split("\\.");
        int[] versions = new int[sVersions.length];
        for (int i=0; i<sVersions.length; i++) {
            versions[i] = Integer.parseInt(sVersions[sVersions.length-i-1]);
        }
        for (int i=0; i<versions.length; i++) {
            // переносится ли добавка на следующий разряд
            boolean shift = versions[i] + delta >= max;
            versions[i] = (versions[i] + delta) % max;
	        if (shift) { 
	            delta = 1; 
	        }
	        else {
	            delta = 0;
	        }
        }
        s = key+"=";
        for (int i=versions.length-1; i>=0; i--) {
            s += ""+versions[i];
            if (i>0) {
                s += ".";
            }
        }
        Logger.debug("write new "+s);
        PrintStream out = new PrintStream(new FileOutputStream(file));
        out.println(s);
        out.close();
    }
    /**
     * @param text
     * @param file
     * @throws IOException
     */
    public static void save2File(byte[] content, File file) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        out.write(content);
        out.close();
    }
    /**
     * @param fullpath
     * @throws Exception
     */
    public static void ensureDirExists(String fullpath) throws IOException
    {
        File f = new File(fullpath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw new IOException ("Can't create directory: "+fullpath);
            }
        }
    }
    /**
     * @param file
     * @return содержимое файла в виде строки
     * @throws IOException
     */
    public static String read(File file) throws IOException
    {
        FileInputStream in = new FileInputStream(file);
        byte[] buf = new byte[in.available()];
        in.read(buf);
        in.close();
        return new String(buf);
    }
    /**
     * отсекает расширение файла от имени или от пути.
     * то есть отсекает все что находится после последней точки включая саму точку.
     * @param nameOrPath
     * @return всё, кроме расширения
     */
    public static String trimExt(String nameOrPath)
    {
        return nameOrPath.substring(0,nameOrPath.lastIndexOf("."));
    }
    /**
     * @param name имя файла
     * @return расширение файла
     */
    public static String getExt(String name)
    {
        int pos = name.lastIndexOf('.');
        if (pos>=0) {
            return name.substring(pos+1);            
        }
        return "";
    }
    /**
     * Рекурсивно удаляет директорию со всем содержимым.
     * @param dir что удалять
     * @return true, если удалена, false в остальных случаях.
     */
    public static boolean rmdir(File dir)
    {
        if (!dir.exists()) return true;
        if (dir.isFile()) return dir.delete();
        
        File[] files = dir.listFiles();
        for (int i=0; i<files.length; i++) 
        {
            if (files[i].isDirectory())
            {
                rmdir(files[i]);
            }
            else 
            {
                files[i].delete();
            }
        }
        return dir.delete();
    }    
    /**
     * @return полный путь к вновь созданной директории с увеличивающимся порядковым номером
     * @throws Exception
     */
    public static String getNumberedDir(String parent) throws IOException
    {
        String tempDir = parent;
        for (int i=1; ; i++) {
            File dir = new File(tempDir+i+File.separator);
            if (!dir.exists()) {
                FileUtil.ensureDirExists(dir.getAbsolutePath());
                tempDir = dir.getAbsolutePath()+"/";
                break;
            }
        }
        return tempDir;
    }
    public static void copy(File src, File dst) throws IOException
    {
        InputStream from = new FileInputStream(src);
        OutputStream to = new FileOutputStream(dst);
        FileUtil.copy(from, to);
        from.close();
        to.close();
    }

}

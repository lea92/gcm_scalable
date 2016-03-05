package fr.scale.gcm_scalable.operation.mapreduce.inputSplit;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

public class FileInputFormat implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static long formatMaxSplitSize = 64000000;
	
	protected File file;
	
	public FileInputFormat(Path path){
		this.file = path.toFile();
	}
	
	public File getFile(){
		return file;
	}
	
	public static long getFormatMaxSplitSize(){
		return formatMaxSplitSize;
	}

}

package fr.scale.gcm_scalable.operation.mapreduce.inputSplit;

import java.nio.file.Path;

interface  IFileSplitInfo {

	/**
	 * The file containing this split's data.
	 * @return
	 */
	Path getPath();
	

	/**
	 * The position of the first byte in the file to process.
	 */
	long getStart();
	
	
	/**
	 * 
	The number of bytes in the file to process.
	 */
	long getLength();
}

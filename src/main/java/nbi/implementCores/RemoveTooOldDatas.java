package nbi.implementCores;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class RemoveTooOldDatas {
	private final static SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
	
	private final static long interval =100000000L;
	private static String filePatternString="";
	private static String compressedFilePatternString=".*/*\\d{8}\\.zip";;
	private static Pattern filePattern=null; 
	private static Pattern compressedFfilePattern=Pattern.compile(compressedFilePatternString);;
	/**
	 * Method removeTooOldDatas.
	 * @param path String
	 */
	public static void removeTooOldDatas(final String path){
		 filePattern = Pattern.compile(filePatternString);
//		 compressedFfilePattern=Pattern.compile(compressedFilePatternString);
		File dir = new File(path);
		if(dir.isDirectory()){
			subRemoveTooOldDatas(dir);
		}
	}
	/**
	 * Method condition.
	 * @param dir File
	
	 * @return boolean */
	private  static  boolean condition(final File dir){
		String dirName = dir.getName();
		boolean condition=false;
		if(filePattern.matcher(dirName).matches()){
			try {
				Date itDate = sdf.parse(dirName);
				if((System.currentTimeMillis() -itDate.getTime()) >interval){
					condition=true;
				}				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(!condition){
			return true;
		}else{
			dir.delete();
			return false;
		}
	}
	/**
	 * Method subRemoveTooOldDatas.
	 * @param dir File
	 */
	private static  void subRemoveTooOldDatas(final File dir){
		File[] subFiles = dir.listFiles();
		for(File subFile :subFiles){
			String subFileName =subFile.getAbsoluteFile().getName();
			if(subFile.isDirectory() && condition(subFile)){
				subRemoveTooOldDatas(subFile);
			}else if(subFile.isFile() && compressedFfilePattern.matcher(subFileName).matches()){
				subFile.delete();
			}
		}
	}
}

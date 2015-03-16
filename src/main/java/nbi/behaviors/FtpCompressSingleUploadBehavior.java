package nbi.behaviors;
 
import java.io.File;
import java.io.IOException; 
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import nbi.utils.FtpUtils;
import nbi.utils.ZipUtils;


/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpCompressSingleUploadBehavior extends AbstractFtpBehavior{
	/**
	 * Method exec.	
	 * @throws IOException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws IOException {
		ftp.changeWorkingDirectory(remoteFolderName);
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		filePattern=Pattern.compile(filePatternString);
		final Set<String> processList = new HashSet<String>();
		if(dir.isDirectory()){
 			File[] fileList = dir.listFiles();
 			File srcFile;
 			
 			for(int i =0;i<fileList.length && (srcFile =fileList[i]).isFile()  && filePattern.matcher(srcFile.getName()).matches() ; ++i){
 				String fileName = srcFile.getAbsoluteFile().getAbsolutePath();
 				processList.add(fileName);
 			}
 			if(fileList.length >0){
 				String prefixName=filePatternString.substring(0, filePatternString.indexOf("."));
 				String outZipFile = dir.getAbsolutePath()+"/"+prefixName+"_"+today+".zip" ;
	    		outZipFile =  outZipFile.replace("//", "/").replace("//", "/");
	    		ZipUtils.compress(processList, outZipFile);
	    		File file =new File(outZipFile);
	    		FtpUtils.uploadProcess(file, ftp,remoteFolderName);
 			}
 			
 		}
	}
}

package nbi.behaviors;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import nbi.utils.ZipUtils;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;



/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SFtpCompressSingleUploadBehavior  extends AbstractSFtpBehavior{
	
	/**
	 * Method exec.
	 * @throws FileSystemException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws FileSystemException{
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);

		Pattern filePattern = Pattern.compile(filePatternString);
		final Set<String> processList = new HashSet<String>();
		 
 		if(dir.isDirectory()){
 			File[] fileList = dir.listFiles();
 			File srcFile;
 			for(int i =0;i<fileList.length && (srcFile =fileList[i]).isFile()  &&  filePattern.matcher(srcFile.getName()).matches() ; ++i){
 				String fileName = srcFile.getAbsoluteFile().getAbsolutePath();
 				processList.add(fileName);
 			}
 			if(fileList.length >0){
 				String prefixName = filePatternString.substring(0, filePatternString.indexOf(".")).replace("*", ""); 			 
 				String outZipFile = dir.getAbsolutePath()+"/"+prefixName+"_"+today+".zip" ;
	    		outZipFile =  outZipFile.replace("//", "/").replace("//", "/");
	    		ZipUtils.compress(processList, outZipFile);
	    		File file =new File(outZipFile);
	    		
				FileObject fileObject = manager.resolveFile(sftpUri + "/"+remoteFolderName  +"/"+ file.getAbsoluteFile().getName(), opts);
				FileObject localFileObject = manager.resolveFile(file.getAbsoluteFile().getAbsolutePath());
				fileObject.copyFrom(localFileObject,  new AllFileSelector() );
 			}
 			
 		}
	}
}

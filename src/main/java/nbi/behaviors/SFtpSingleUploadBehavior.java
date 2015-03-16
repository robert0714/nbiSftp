package nbi.behaviors;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SFtpSingleUploadBehavior  extends AbstractSFtpBehavior{
	 
	/**
	 * Method exec.	
	 * @throws FileSystemException * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws FileSystemException{
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		Pattern filePattern = Pattern.compile(filePatternString);
		if(dir.isDirectory()){
 			File[] fileList = dir.listFiles();
 			File srcFile;
 			for(int i =0;i<fileList.length && (srcFile =fileList[i]).isFile()  &&  filePattern.matcher(srcFile.getName()).matches() ; ++i){
// 				String fileName = srcFile.getAbsoluteFile().getAbsolutePath(); 				
 				FileObject fileObject = manager.resolveFile(sftpUri +"/"+ remoteFolderName +"/"+ srcFile.getAbsoluteFile().getName(), opts);
				FileObject localFileObject = manager.resolveFile( srcFile.getAbsoluteFile().getAbsolutePath());
				fileObject.copyFrom(localFileObject,  new AllFileSelector() );
 			}
 			
 			
 		}
		
	}
}

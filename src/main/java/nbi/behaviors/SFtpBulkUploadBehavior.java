package nbi.behaviors;

import java.io.File;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException; 
 

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SFtpBulkUploadBehavior  extends AbstractSFtpBehavior{
	 
	/**
	 * Method exec.
	 * @throws FileSystemException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws FileSystemException{
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		if(dir.listFiles()!=null)
		for(File file:dir.listFiles()){
			String fileName = file.getAbsoluteFile().getName();
			FileObject fileObject = manager.resolveFile(sftpUri + "/"+remoteFolderName  + fileName, opts);
			FileObject localFileObject = manager.resolveFile(dir.getAbsolutePath()+ "/" + fileName);
			fileObject.copyFrom(localFileObject,  new AllFileSelector() );
		}
		
	}
}

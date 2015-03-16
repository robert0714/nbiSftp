package nbi.behaviors;

import java.io.File;
import java.io.IOException;
import nbi.utils.FtpUtils;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpBulkUploadBehavior extends AbstractFtpBehavior{
	/**
	 * Method exec.
	 * @throws IOException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws IOException {
		ftp.changeWorkingDirectory(remoteFolderName);
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		File[] ListFiles = dir.listFiles();
		if(ListFiles!=null)
		for(File aFile:ListFiles){
			FtpUtils.uploadProcess(aFile, ftp,remoteFolderName);
		}
	}
	 
}

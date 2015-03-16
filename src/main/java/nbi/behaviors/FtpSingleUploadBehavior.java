package nbi.behaviors;
 
import java.io.File;
import java.io.IOException;

import nbi.utils.FtpUtils;
 

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpSingleUploadBehavior extends AbstractFtpBehavior{
	/**
	 * Method exec.
	 * @throws IOException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws IOException {
		ftp.changeWorkingDirectory(remoteFolderName);
		File dir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		for(File aFile:dir.listFiles()){
			FtpUtils.uploadProcess(aFile, ftp,remoteFolderName);
		}
	}
}

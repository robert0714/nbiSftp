package nbi.behaviors;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.local.LocalFile;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SFtpSingleDownloadBehavior  extends AbstractSFtpBehavior{
	
	/**
	 * Method exec.	
	 * @throws FileSystemException  
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws FileSystemException {
		FileObject startPath = manager.resolveFile(sftpUri, opts);
		Pattern filePattern = Pattern.compile(filePatternString);
		final File mkdir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		if(!mkdir.exists() && !mkdir.isFile()){
			mkdir.mkdir();
		}
		
		FileObject[] aChildren = startPath. getChildren();
		for (FileObject aFileObject : aChildren) {
			if (aFileObject.getType() == FileType.FILE) {
				String baseName = aFileObject.getName().getBaseName();
				if (filePattern.matcher(baseName).matches()) {
					String localUrl = "file://" + mkdir.getAbsolutePath()+"/"+ baseName;
					LocalFile localFile =  (LocalFile) manager.resolveFile(localUrl);
					localFile.copyFrom(aFileObject, new AllFileSelector());
				}
			}
		}
	}
}

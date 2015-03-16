package nbi.behaviors;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import nbi.utils.FtpUtils;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;


/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpSingleDownloadBehavior extends AbstractFtpBehavior{
	
	/**
	 * Method exec.
	 * @throws IOException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws IOException {
		this.filePattern = Pattern.compile(filePatternString);
		ftp.changeWorkingDirectory(remoteFolderName);
		final Set<String> fileSet = new HashSet<String>();
		final FTPFileFilter filter=new FTPFileFilter() {
			public boolean accept(FTPFile ftpfile) {
				String realFileName = ftpfile.getName();
				if(ftpfile.isFile() &&   filePattern.matcher(realFileName).matches() ){										
					fileSet.add(realFileName);
				}
				return false;
			}
		};
		String pathname="";
		ftp.listFiles(pathname, filter);
		final File mkdir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		if(!mkdir.exists() && !mkdir.isFile()){
			mkdir.mkdir();
		}
		for(String fileName:fileSet){
			File  file = new File(mkdir.getAbsolutePath()+"/"+fileName);
			FtpUtils.downloadProcess(file, ftp, remoteFolderName);
			if(file.length()==0){
				messageList.add(file.getAbsoluteFile().getAbsolutePath()+"file length is 0");
	    		}
		}
	}
}

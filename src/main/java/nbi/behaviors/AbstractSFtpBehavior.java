package nbi.behaviors;

import java.util.List;

import nbi.protocols.DynamicSFtpBehavior;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.log4j.Logger;


/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public abstract class AbstractSFtpBehavior implements DynamicSFtpBehavior{
	protected  StandardFileSystemManager manager;
	protected String filePatternString;
	protected String localFolderName;
	protected String remoteFolderName;
	protected String sftpUri;
	protected FileSystemOptions opts ;
	protected List<String> messageList  ;
	private String clientRootPath="";	
	protected  static Logger  log ;
	protected String today;
	/**
	 * Method setToday.
	 * @param today String
	 * @see nbi.protocols.ProcessBehavior#setToday(String)
	 */
	public void setToday(String today){
		this.today =today;
	}
	/**
	 * Method setFilePatternString.
	 * @param filePatternString String	
	 * @see nbi.protocols.ProcessBehavior#setFilePatternString(String) */
	public void setFilePatternString(String filePatternString) {
		this.filePatternString = filePatternString;
	}
	/**
	 * Method setLocalFolderName.
	 * @param localFolderName String	
	 * @see nbi.protocols.ProcessBehavior#setLocalFolderName(String) */
	public void setLocalFolderName(String localFolderName) {
		this.localFolderName = localFolderName;
	}
	/**
	 * Method setRemoteFolderName.
	 * @param remoteFolderName String	
	 * @see nbi.protocols.ProcessBehavior#setRemoteFolderName(String) */
	public void setRemoteFolderName(String remoteFolderName) {
		this.remoteFolderName = remoteFolderName;
	}
	/**
	 * Method getClientRootPath.	
	 * @return String */
	public String getClientRootPath() {
		return clientRootPath;
	}
	/**
	 * Method setClientRootPath.
	 * @param clientRootPath String
	 */
	public void setClientRootPath(String clientRootPath) {
		this.clientRootPath = clientRootPath;
	}
	/**
	 * Method setResources.
	 * @param manager StandardFileSystemManager
	 * @param sftpUri String
	 * @param opts FileSystemOptions
	 * @param log Logger
	 * @param messageList List<String>	
	 * @see protocols.SFTPBehavior#setResources(StandardFileSystemManager, String, FileSystemOptions, Logger, List<String>) */
	public void setResources(final  StandardFileSystemManager manager ,String sftpUri, FileSystemOptions opts, Logger log, List<String> messageList) {
		this.manager =manager;
		this.sftpUri =sftpUri;
		this.opts =opts;
		this.log=log;
		this.messageList =messageList;
	}
	/**
	 * Method behavior.
	 * @param srcFileLink String
	 * @param destiFileLink String
	 * @param opts FileSystemOptions
	 * @param manager FileSystemManager	
	 * @throws FileSystemException */
	protected void behavior(final String srcFileLink, final String destiFileLink, final FileSystemOptions opts, final FileSystemManager manager) throws FileSystemException {
		FileObject destiFile = manager.resolveFile(destiFileLink, opts);
		FileObject srcFile = manager.resolveFile(srcFileLink);
		destiFile.copyFrom(srcFile, Selectors.SELECT_SELF);
	}
}

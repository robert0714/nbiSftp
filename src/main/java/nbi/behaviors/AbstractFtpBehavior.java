package nbi.behaviors;

import java.util.List;
import java.util.regex.Pattern;

import nbi.protocols.DynamicFtpBehavior;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;


/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public abstract class AbstractFtpBehavior implements DynamicFtpBehavior{
	protected  static Logger  log ;
	protected  FTPClient ftp;
	protected    List<String> messageList  ;
	private String clientRootPath="";
	protected Pattern filePattern;
	protected String filePatternString;
	protected String localFolderName;
	protected String remoteFolderName;
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
	 * @param ftp FTPClient
	 * @param log Logger
	 * @param messageList List<String>	
	 * @see protocols.FTPBehavior#setResources(FTPClient, Logger, List<String>) */
	public void setResources(FTPClient ftp, Logger log, List<String> messageList) {
		this.ftp =ftp;
		this.log=log;
		this.messageList =messageList;
	}
}

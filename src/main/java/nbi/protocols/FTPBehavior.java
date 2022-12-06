package nbi.protocols;

import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public interface FTPBehavior {
	/**
	 * Method setResources.
	 * @param ftp FTPClient
	 * @param log Logger
	 * @param messageList List<String>
	 */
	void setResources(final FTPClient ftp, final  Logger  log,final   List<String> messageList);
	
	
}

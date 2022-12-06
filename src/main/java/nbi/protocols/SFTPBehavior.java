package nbi.protocols;

import java.util.List;

import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public interface SFTPBehavior {
	/**
	 * Method setResources.
	 * @param manager StandardFileSystemManager
	 * @param sftpUri String
	 * @param opts FileSystemOptions
	 * @param log Logger
	 * @param messageList List<String>
	 */
	void setResources(final StandardFileSystemManager manager ,final String sftpUri,final FileSystemOptions opts, final  Logger  log,final   List<String> messageList);
}

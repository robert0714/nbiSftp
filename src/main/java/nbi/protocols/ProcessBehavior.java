package nbi.protocols;

import java.io.IOException;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public interface ProcessBehavior {

	/**
	 * Method setRemoteFolderName.
	 * @param remoteFolderName String
	 */
	void setRemoteFolderName(String remoteFolderName);

	/**
	 * Method setLocalFolderName.
	 * @param localFolderName String
	 */
	void setLocalFolderName(String localFolderName);

	/**
	 * Method setFilePatternString.
	 * @param filePatternString String
	 */
	void setFilePatternString(String filePatternString);

	/**
	 * Method exec.
	
	 * @throws IOException */
	void exec()throws IOException;
	
	/**
	 * Method setToday.
	 * @param today String
	 */
	void setToday(String today);
	void setClientRootPath(String clientRootPath);
}

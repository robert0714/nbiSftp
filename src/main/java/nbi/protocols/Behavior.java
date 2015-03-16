package nbi.protocols;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public interface Behavior {
	/**
	 * Method getFilePatternString.
	 * @return String
	 */
	String getFilePatternString();

	/**
	 * Method getRemoteFolderName.
	 * @return String
	 */
	String getRemoteFolderName();

	/**
	 * Method getLocalFolderName.
	 * @return String
	 */
	String getLocalFolderName();

	/**
	 * Method isBulkTransport.
	 * @return boolean
	 */
	boolean isBulkTransport();

	/**
	 * Method isCompressPut.
	 * @return boolean
	 */
	boolean isCompressPut();

	/**
	 * Method isPutOrGet.
	 * @return boolean
	 */
	boolean isPutOrGet();

	/**
	 * Method getCustomLogicClassName.
	 * @return String
	 */
	String getCustomLogicClassName();	
}

package nbi.adapter;

import nbi.protocols.Behavior;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FilePatternBehaviorAdapter implements Behavior {
	// Look for a file path like "smoke20070128_wkt.txt"
	// filePatternString = ".*/smoke\\d{8}_wkt\\.txt";
	private String filePatternString = "";
	private String remoteFolderName = "/";
	private String localFolderName = "/";
	private String customLogicClassName = "";
	private boolean bulkTransport;
	private boolean compressPut;
	private boolean putOrGet;

	/**
	 * Method getFilePatternString.
	 * @return String * @see nbi.protocols.Behavior#getFilePatternString()
	 */
	public String getFilePatternString() {
		return filePatternString;
	}

	/**
	 * Method setFilePatternString.
	 * @param filePatternString   String
	 */
	public void setFilePatternString(String filePatternString) {
		this.filePatternString = filePatternString;
	}

	/**
	 * Method getRemoteFolderName.
	 * 
	 * 
	 * @return String * @see nbi.protocols.Behavior#getRemoteFolderName()
	 */
	public String getRemoteFolderName() {
		return remoteFolderName;
	}

	/**
	 * Method setRemoteFolderName.
	 * @param remoteFolderName String
	 */
	public void setRemoteFolderName(String remoteFolderName) {
		this.remoteFolderName = remoteFolderName;
	}

	/**
	 * Method getLocalFolderName.
	 * @return String * @see nbi.protocols.Behavior#getLocalFolderName()
	 */
	public String getLocalFolderName() {
		return localFolderName;
	}

	/**
	 * Method setLocalFolderName.
	 * 
	 * @param localFolderName
	 *            String
	 */
	public void setLocalFolderName(String localFolderName) {
		this.localFolderName = localFolderName;
	}

	/**
	 * Method isBulkTransport.
	 * 
	 * 
	 * @return boolean * @see nbi.protocols.Behavior#isBulkTransport()
	 */
	public boolean isBulkTransport() {
		return bulkTransport;
	}

	/**
	 * Method setBulkTransport.
	 * 
	 * @param bulkTransport
	 *            boolean
	 */
	public void setBulkTransport(boolean bulkTransport) {
		this.bulkTransport = bulkTransport;
	}

	/**
	 * Method isCompressPut.
	 * 
	 * 
	 * @return boolean * @see nbi.protocols.Behavior#isCompressPut()
	 */
	public boolean isCompressPut() {
		return compressPut;
	}

	/**
	 * Method setCompressPut.
	 * 
	 * @param compressPut
	 *            boolean
	 */
	public void setCompressPut(boolean compressPut) {
		this.compressPut = compressPut;
	}

	/**
	 * Method isPutOrGet.
	 * 
	 * 
	 * @return boolean * @see nbi.protocols.Behavior#isPutOrGet()
	 */
	public boolean isPutOrGet() {
		return putOrGet;
	}

	/**
	 * Method setPutOrGet.
	 * 
	 * @param putOrGet
	 *            boolean
	 */
	public void setPutOrGet(boolean putOrGet) {
		this.putOrGet = putOrGet;
	}

	/**
	 * Method getCustomLogicClassName.
	 * 
	 * @return String
	 */
	public String getCustomLogicClassName() {
		return customLogicClassName;
	}

	/**
	 * Method setCustomLogicClassName.
	 * 
	 * @param customLogicClassName
	 *            String
	 */
	public void setCustomLogicClassName(String customLogicClassName) {
		this.customLogicClassName = customLogicClassName;
	}
}

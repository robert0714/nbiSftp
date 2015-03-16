package nbi.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class ServerAdapter {
	protected boolean sFtp; 
	protected String ip;
    protected String port;
    protected String userName;
    protected String passwd;
    private List<FilePatternBehaviorAdapter> behaviors;
    
	/**
	 * Method getBehaviors.
	
	 * @return List<FilePatternBehaviorAdapter> */
	public List<FilePatternBehaviorAdapter> getBehaviors() {
		if(behaviors ==null){
			behaviors = new ArrayList<FilePatternBehaviorAdapter>();
		}
		return behaviors;
	}
	/**
	 * Method setBehaviors.
	 * @param behaviors List<FilePatternBehaviorAdapter>
	 */
	public void setBehaviors(List<FilePatternBehaviorAdapter> behaviors) {
		this.behaviors = behaviors;
	}
	/**
	 * Method getIp.
	
	 * @return String */
	public String getIp() {
		return ip;
	}
	/**
	 * Method setIp.
	 * @param ip String
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * Method getPort.
	
	 * @return String */
	public String getPort() {
		return port;
	}
	/**
	 * Method setPort.
	 * @param port String
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * Method getUserName.
	
	 * @return String */
	public String getUserName() {
		return userName;
	}
	/**
	 * Method setUserName.
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * Method getPasswd.
	
	 * @return String */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * Method setPasswd.
	 * @param passwd String
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	/**
	 * Method issFtp.
	
	 * @return boolean */
	public boolean issFtp() {
		return sFtp;
	}
	/**
	 * Method setsFtp.
	 * @param sFtp boolean
	 */
	public void setsFtp(boolean sFtp) {
		this.sFtp = sFtp;
	}

}

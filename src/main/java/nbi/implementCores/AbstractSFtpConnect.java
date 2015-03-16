package nbi.implementCores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

import nbi.adapter.ServerAdapter;
import nbi.protocols.Behavior;
import nbi.protocols.Connection;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.log4j.Logger; 


/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public abstract class AbstractSFtpConnect implements Connection{
	protected StandardFileSystemManager manager;
	protected FileSystemOptions opts ;
	protected final ServerAdapter server;
	protected final  Behavior[] behaviors;
	protected final static Logger  log = Logger.getLogger(AbstractSFtpConnect.class);
	private final   List<String> messageList = new ArrayList<String>();
	protected String today;
	/**
	 * Method setToday.
	 * @param today String
	 */
	public void setToday(String today){
		this.today =today;
	}
	/**
	 * Method getMessageList.
	
	 * @return List<String> */
	public  List<String> getMessageList() {
		return messageList;
	}
	/**
	 * Constructor for AbstractSFtpConnect.
	 * @param server ServerAdapter
	 */
	public AbstractSFtpConnect(ServerAdapter server) {
		super();
		this.server = server;
		this.behaviors = server.getBehaviors().toArray(new Behavior[]{});		
	}
	/**
	 * Method processLogic.
	 * @param singleBehavior Behavior
	
	
	
	 * @throws FileNotFoundException * @throws IOException * @see nbi.protocols.Connection#processLogic(Behavior) */
	public abstract void processLogic(Behavior singleBehavior) throws FileNotFoundException,IOException;	
	/**
	 * Method connect.
	 * @see nbi.protocols.Connection#connect()
	 */
	public  void connect(){
		manager = new StandardFileSystemManager();
		try {
			final String ip = server.getIp(); 
			String username =server.getUserName();
			String password=server.getPasswd();
			 
			opts = createDefaultOptionsV2(username, password);
			manager.init();
			
			for(Behavior singleBehavior: behaviors){
	    		try {
					processLogic(singleBehavior);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					messageList.add( ip +" sFTP Port Error.");
				} catch (IOException e) {
					e.printStackTrace();
					messageList.add( ip +" sFTP Port Error.");
				}catch (Exception e) {
					e.printStackTrace();
					messageList.add( ip +" sFTP Port Error.");
				}
	    	}			
			
		} catch (FileSystemException ex) {
			ex.printStackTrace();
			messageList.add(  "sftp initialization failed");
//			throw new RuntimeException("sftp initialization failed", ex);			
		} catch (Exception ex) {
			ex.printStackTrace();
			messageList.add(  "sftp initialization failed");
//			throw new RuntimeException("sftp initialization failed", ex);			
		} finally{
			manager.close();
		}
	}	
	
	
	
	/**
	 * Method createDefaultOptionsV2.
	 * @param username String
	 * @param password String
	
	
	 * @return FileSystemOptions * @throws FileSystemException */
	private FileSystemOptions createDefaultOptionsV2(String username, String password) throws FileSystemException {
		// Create SFTP options
		FileSystemOptions opts = new FileSystemOptions();

		try {
			UserAuthenticator auth = new StaticUserAuthenticator(null, username, password);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		} catch (FileSystemException ex) {
			throw new RuntimeException("setUserAuthenticator failed", ex);
		}

		return opts;
	}
	/**
	 * Method createConnectionStringFromRemoteFilePath.
	 * @param server ServerAdapter
	 * @param remoteFilePathA String
	 * @return String
	 */
	public String createConnectionStringFromRemoteFilePath(final ServerAdapter server , String remoteFilePathA){
		final String username =server.getUserName();
		final String password=server.getPasswd();
		final String ip = server.getIp();
		
		String remoteFilePath =remoteFilePathA;
		if (remoteFilePath  == null) {
			remoteFilePath = "";
		} else {
			remoteFilePath = remoteFilePathA.trim();
		}
		return "sftp://" + username + ":" + password + "@" + ip + "/" + remoteFilePath;
	}
	
	/**
	 * Method getOpts.
	
	 * @return FileSystemOptions */
	public FileSystemOptions getOpts() {
		return opts;
	}
	/**
	 * Method getServer.
	
	 * @return ServerAdapter */
	public ServerAdapter getServer() {
		return server;
	}	
}

package nbi.implementCores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nbi.adapter.ServerAdapter;
import nbi.protocols.Behavior;
import nbi.protocols.Connection;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.LoggerFactory;import org.slf4j.Logger;


 


 

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public abstract class AbstractFtpConnect implements Connection{
	protected final ServerAdapter server;
	protected final  Behavior[] behaviors;
	protected final static Logger  log = LoggerFactory.getLogger(AbstractFtpConnect.class);
	protected final FTPClient ftp = new FTPClient();
	private final   List<String> messageList = new ArrayList<String>();
	/**
	 * Method getMessageList.
	
	 * @return List<String> */
	public  List<String> getMessageList() {
		return messageList;
	}
	/**
	 * Constructor for AbstractFtpConnect.
	 * @param server ServerAdapter
	 */
	public AbstractFtpConnect(ServerAdapter server ) {
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
		ftp.setControlKeepAliveTimeout(300);
		final String ip = server.getIp();
		String port= server.getPort();
		String username =server.getUserName();
		String password=server.getPasswd();
		if(port ==null || port.length()==0){
			port ="21";
		}
		try {
			ftp.connect(ip,Integer.parseInt(port));
			boolean authentication = ftp.login(username, password);
			log.info("Connected to " + ip + "/"+ftp.getReplyString());
		    if(!authentication){
		    	String error = "Connected to " + ip + "/"+ftp.getReplyString();
		    	log.info(error);  
		    	messageList.add(error);
		    }else{
		    	for(Behavior singleBehavior: behaviors){
		    		processLogic(singleBehavior);
		    	}
		    }
		   
		} catch (NumberFormatException e) {
			log.info("FTP Port Error.");
			messageList.add( ip +" FTP Port Error.");
			e.getStackTrace();
		} catch (FileNotFoundException e) {
			messageList.add( ip +" FileNotFoundException.");
			log.info("FileNotFoundException");
			log.info(e.getMessage(),e);
		}catch (IOException e) {
			StackTraceElement[] aStackTraceElements = e.getStackTrace();
			for(StackTraceElement aStackTrace :aStackTraceElements){
				log.info("username: "+username+ ",connection: "+ip+" ,"+aStackTrace.toString());
			}
			messageList.add( ip +" username: "+username+ ",connection: "+ip+" ,"+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				log.info(ftp.getReplyString());  
				try {
					ftp.disconnect();
					log.info("Success to  close the connection: "+ip);
				} catch (IOException e) {					
					log.info("Failed to  close the connection: "+ip);
					log.info(e.getMessage(),e);
					messageList.add("Failed to  close the connection: "+ip);
				}
			}
		}
	    
	}
}

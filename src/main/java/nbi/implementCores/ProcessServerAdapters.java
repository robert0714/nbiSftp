package nbi.implementCores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nbi.adapter.ServerAdapter;
import nbi.protocols.Behavior;
import nbi.protocols.DynamicFtpBehavior;
import nbi.protocols.DynamicSFtpBehavior;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class ProcessServerAdapters {
	private String processToday;
	private String clientRootPath;
	private List<String> messageList = new ArrayList<String>();
	
	/**
	 * Method getMessageList.
	 * @return List<String>
	 */
	public List<String> getMessageList() {
		return messageList;
	}	
	/**
	 * Method setToday.
	 * @param today String
	 */
	public void setToday(String today) {
		this.processToday = today;
	}
	/**
	 * Method setClientRootPath.
	 * @param clientRootPath String
	 */
	public void setClientRootPath(String clientRootPath) {
		this.clientRootPath = clientRootPath;
	}

	/**
	 * Method processServerAdapterTasks.
	 * @param serverAdapterList List<ServerAdapter>
	 */
	public void processServerAdapterTasks(final List<ServerAdapter> serverAdapterList){
		List<ServerAdapter> sFtpTaskList = new ArrayList<ServerAdapter>();
		List<ServerAdapter> ftpTaskList=new ArrayList<ServerAdapter>();
		for(ServerAdapter server :serverAdapterList){
			if(server.issFtp()){
				sFtpTaskList.add(server);
			}else{
				ftpTaskList.add(server);
			}
		}
		processSFTP(sFtpTaskList);
		processFTP(ftpTaskList);
	}
	/**
	 * Method processFTP.
	 * @param serverAdapterList List<ServerAdapter>
	 */
	private void processFTP(final List<ServerAdapter> serverAdapterList){
		for(ServerAdapter server :serverAdapterList){
			if(server.getBehaviors().size()>0){
				AbstractFtpConnect connectServer = new FtpConnect(server );
				connectServer.connect();
				messageList.addAll(connectServer.getMessageList());
			}
		}
	}
	/**
	 * Method processSFTP.
	 * @param serverAdapterList List<ServerAdapter>
	 */
	private void processSFTP(final List<ServerAdapter> serverAdapterList){
		for(int i = 0 ;i<serverAdapterList .size();i++){
			ServerAdapter server =serverAdapterList.get(i);
			if(server.getBehaviors().size()>0){
				AbstractSFtpConnect connectServer = new SFtpConnect(server );
				connectServer.connect();
				messageList.addAll(connectServer.getMessageList());
			}
		}
	}
	/**
	 * @author robert.lee
	 * @version $Revision: 1.0 $
	 */
	class SFtpConnect extends AbstractSFtpConnect {
		/**
		 * Constructor for SFtpConnect.
		 * @param server ServerAdapter
		 */
		public SFtpConnect(ServerAdapter server ) {
			super(server );
		}
		/**
		 * Method processLogic.
		 * @param singleBehavior Behavior		
		 * @throws FileNotFoundException 
		 * @throws IOException 
		 * @see nbi.protocols.Connection#processLogic(Behavior) 
		 * */
		public void processLogic(Behavior singleBehavior) throws FileNotFoundException, IOException {
			final long start = System.nanoTime();		    
			DynamicSFtpBehavior logicalBehavior = (DynamicSFtpBehavior) JudgeBehaviors.judgementBehavior(singleBehavior, true);
			log.info("filePatternString: "+singleBehavior.getFilePatternString());
			log.info("behavior: "+logicalBehavior.getClass().getName());
			logicalBehavior.setClientRootPath(clientRootPath);
			logicalBehavior.setToday(processToday);
			String sftpUri = createConnectionStringFromRemoteFilePath(server, singleBehavior.getRemoteFolderName());
			logicalBehavior.setResources(  manager,  sftpUri, opts,log, messageList);
			logicalBehavior.exec();			
			final long end = System.nanoTime();
			log.info("Time (seconds) taken is " + (end - start)/1.0e9);
		}}
	
	/**
	 * @author robert.lee
	 * @version $Revision: 1.0 $
	 */
	class FtpConnect extends AbstractFtpConnect {		
		/**
		 * Constructor for FtpConnect.
		 * @param server ServerAdapter
		 */
		public FtpConnect(ServerAdapter server ) {
			super(server );
		}
		/**
		 * Method processLogic.
		 * @param singleBehavior Behavior
		 * @throws FileNotFoundException 
		 * @throws IOException 
		 * @see nbi.protocols.Connection#processLogic(Behavior) 
		 * */
		public void processLogic(Behavior singleBehavior) throws FileNotFoundException, IOException {
			final long start = System.nanoTime();
			log.info(ftp.getReplyString());  
    		ftp.noop(); // check that control connection is working OK
 		    log.info(ftp.getReplyString()); 		    
 		    DynamicFtpBehavior logicalBehavior = (DynamicFtpBehavior) JudgeBehaviors.judgementBehavior(singleBehavior, false);
 		    log.info("filePatternString: "+singleBehavior.getFilePatternString());
 		    log.info("behavior: "+logicalBehavior.getClass().getName());
 		    logicalBehavior.setClientRootPath(clientRootPath);
 		    logicalBehavior.setToday(processToday);
 		    logicalBehavior.setResources(ftp, log, messageList);
			logicalBehavior.exec();
			final long end = System.nanoTime();
			log.info("Time (seconds) taken is " + (end - start)/1.0e9);
		}		
	}	
}

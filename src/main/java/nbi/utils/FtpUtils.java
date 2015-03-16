package nbi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpUtils {
	final static Logger  log = Logger.getLogger(FtpUtils.class);
	private final static   List<String> messageList = new ArrayList<String>();
	/**
	 * Method getMessageList.	
	 * @return List<String> */
	public static List<String> getMessageList() {
		return messageList;
	}
	
	/**
	 * @param srcFile 
	 * @param distFolder 
	 * @param ftpClient FTPClient
	 **/
	public static  void  uploadProcess(final File srcFile,final FTPClient ftpClient, final String distFolder) {
		if(ftpClient.isAvailable()){
			log.info(ftpClient.getReplyString());
			InputStream inputStream=null;
			try {
				ftpClient.changeWorkingDirectory(distFolder);
				log.info(ftpClient.getReplyString());
				log.info("remoteFolderPath: "+distFolder);
				inputStream = new FileInputStream(srcFile);
				final String localAbsolutePath = srcFile.getParentFile().getAbsoluteFile().toURI().toString(); 
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
				log.info(ftpClient.getReplyString());
				ftpClient.enterLocalPassiveMode(); 
				log.info(ftpClient.getReplyString());
				ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); 
				log.info(ftpClient.getReplyString());
				log.debug("filename: "+srcFile.getName());
				boolean flag = ftpClient.storeFile(srcFile.getName(), inputStream); 
				log.info(ftpClient.getReplyString());  
                if (flag) { 
                	log.info("Success to  upload  "+localAbsolutePath);
                } else { 
                	log.info("Failed to  upload  "+localAbsolutePath);
                } 
			} catch (FileNotFoundException e) {
				log.info("FileNotFoundException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			} catch (IOException e) {
				log.info("IOException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			}finally{
				try {
					if(inputStream!=null)
						inputStream.close();
				} catch (IOException e) {
					log.info("IOException");
					log.info(e.getMessage()==null?e:e.getMessage());
					if(e.getMessage()!=null){
						log.info( e.getMessage());
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
					}else{
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
					}
				}		
			}
			
		}else{
			messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString());			
		}
	}
	/**
	 * @param destiFile 
	 * @param remoteFolder
	 * * @param ftpClient FTPClient
	 **/
	public static  void  downloadProcess(final File destiFile,final FTPClient ftpClient, final String remoteFolder) {
		if(ftpClient.isAvailable()){
			log.info(ftpClient.getReplyString());			
			OutputStream outStream=null;
			try {
				outStream=new FileOutputStream(destiFile);
				final String remoteAbsolutePath = destiFile.getParentFile().getAbsoluteFile().toURI().toString(); 
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
				log.info(ftpClient.getReplyString());
				ftpClient.enterLocalPassiveMode(); 
				log.info(ftpClient.getReplyString());
				ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); 
				log.info(ftpClient.getReplyString());
				ftpClient.changeWorkingDirectory(remoteFolder);
				log.info("destiFile.getName(): "+destiFile.getName());
				boolean flag = ftpClient.retrieveFile(destiFile.getName(), outStream);				
				log.info(ftpClient.getReplyString());  
                if (flag) { 
                	log.info("Success to  download  "+remoteAbsolutePath);
                } else { 
                	log.info("Failed to  download  "+remoteAbsolutePath);
                } 
			} catch (FileNotFoundException e) {
				log.info("FileNotFoundException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			} catch (IOException e) {
				log.info("IOException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			}finally{
				try {
					if(outStream!=null)
						outStream.flush();
					if(outStream!=null)
						outStream.close();
				} catch (IOException e) {
					log.info("IOException");
					if(e.getMessage()!=null){
						log.info( e.getMessage());
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
					}else{
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
					}
				}				
			}
			
		}else{
			System.out.println(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString());
			messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString());			
		}
	}
	/**
	 * @param destiFile
	 * @param remoteFolder
	 * @param ftpClient FTPClient	
	 * @return Map<String,Calendar> 
	 * **/
	public static  Map<String ,Calendar >  downloadProcessIfNoSuchFileReturnAllCandidtate(final File destiFile,final FTPClient ftpClient, final String remoteFolder) {
		Map<String ,Calendar > candidateMap = new HashMap<String, Calendar>();
		if(ftpClient.isAvailable()){
			log.info(ftpClient.getReplyString());			
			OutputStream outStream=null;
			try {
				outStream=new FileOutputStream(destiFile);
				final String remoteAbsolutePath = destiFile.getParentFile().getAbsoluteFile().toURI().toString(); 
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
				log.info(ftpClient.getReplyString());
				ftpClient.enterLocalPassiveMode(); 
				log.info(ftpClient.getReplyString());
				ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); 
				log.info(ftpClient.getReplyString());
				ftpClient.changeWorkingDirectory(remoteFolder);
				log.info("destiFile.getName(): "+destiFile.getName());
				boolean flag = ftpClient.retrieveFile(destiFile.getName(), outStream);				
				log.info(ftpClient.getReplyString());  
                if (flag) { 
                	log.info("Success to  download  "+remoteAbsolutePath);
                } else {                	
                	log.info("Failed to  download  "+remoteAbsolutePath);
                	FTPFile[] files = ftpClient.listFiles(remoteFolder);
                	log.info("candidate files:  "+files.length);
                	for(FTPFile aFile:files ){
                		Calendar aTimestamp = aFile.getTimestamp();
                		String name = aFile.getName();
                		candidateMap.put(name, aTimestamp);
                	}
                } 
			} catch (FileNotFoundException e) {
				log.info("FileNotFoundException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			} catch (IOException e) {
				log.info("IOException");
				if(e.getMessage()!=null){
					log.info( e.getMessage());
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
				}else{
					messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
				}
			}finally{
				try {
					if(outStream!=null)
						outStream.flush();
					if(outStream!=null)
						outStream.close();
				} catch (IOException e) {
					log.info("IOException");
					if(e.getMessage()!=null){
						log.info( e.getMessage());
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString() +" "+e.getMessage());
					}else{
						messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString()  );
					}
				}				
			}
			
		}else{
			System.out.println(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString());
			messageList.add(ftpClient.getRemoteAddress()+" : "+ftpClient.getRemotePort()+" "+ftpClient.getReplyString());			
		}
		return candidateMap;
	}
}

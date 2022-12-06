package nbi.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nbi.adapter.ServerAdapter;
import nbi.implementCores.LoadServerConfigXML;
import nbi.implementCores.ProcessServerAdapters;
import nbi.implementCores.SendEmailNotification;
import nbi.implementCores.SendEmailNotification.Notification;
import nbi.utils.FtpUtils;
import nbi.xsd.model.ServerType;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 */
public class ConnectionProcess {
	final static Logger  log = LoggerFactory.getLogger(ConnectionProcess.class);
	final static SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
	final static String today = sdf.format(new Date());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args==null || args.length<2){
			log.info("\r arg[0] serverfile format file(*.xml).  \r arg[1] client file root location.   ");
			return;
		}
		 final long start = System.nanoTime();
		 boolean error=false;
		 List<String> messages = new ArrayList<String>();
		 File tmpFile =new File(args[0]);
		 File configFile =new File(tmpFile.getAbsoluteFile().getAbsolutePath().toString().replace(tmpFile.getName(), "")+"smtp.properties");
		 log.info("\r configFile:  "+configFile.getAbsoluteFile().getAbsolutePath());
		 SendEmailNotification sender = new SendEmailNotification(configFile); 
		 
		 try {				
				final LoadServerConfigXML loadXML = new LoadServerConfigXML();
				final List<ServerType> serverList = loadXML.loadXML(args[0]);
				List<ServerAdapter>[] initialDatas = loadXML.getUploadAndDownloadTasks(serverList);
				if(initialDatas!=null){
					List<ServerAdapter> uploadList =initialDatas[0];
					List<ServerAdapter> downloadList =initialDatas[1];
					
					ProcessServerAdapters aProcessServerAdapters =new ProcessServerAdapters();
					aProcessServerAdapters.setToday(today);
					aProcessServerAdapters.setClientRootPath( args[1]);
					log.info("\rprocess : DOWNLOAD....  ");
					aProcessServerAdapters.processServerAdapterTasks(downloadList);
					log.info("\rprocess : UPLOAD....  ");
					aProcessServerAdapters.processServerAdapterTasks(uploadList);
					
					List<String> messageList = aProcessServerAdapters.getMessageList();
					messages.addAll(messageList);
				}			
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage(),e);
				error=true;
			}finally{
				log.info("messages size: "+messages.size());
				StringBuffer sbf = new StringBuffer();
				for(String contents:messages){
					sbf.append("<br/>"+contents+"<br/>\n\r");
				}
				for(String contents:FtpUtils.getMessageList()){
					sbf.append("<br/>"+contents+"<br/>\n\r");
				}
				Notification aNotification = sender.makeEmail(today+"iRM Ftp Transport Report", sbf.toString());
				if(messages.size()>0 || FtpUtils.getMessageList().size()>0){
					sender.sendEmail(aNotification);
					log.info("message  sent.....");
				}else{
					log.info("no message need to sent.....");
				}
				log.info(error ?"Errors Happened":"Success");
				FtpUtils.getMessageList().clear();
				final long end = System.nanoTime();
				log.info("NBI Ftp's Time (seconds) taken is " + (end - start)/1.0e9);
				System.exit(error ? 1 : 0);
			}
	}
}
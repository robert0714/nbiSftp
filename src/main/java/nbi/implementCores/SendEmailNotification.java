package nbi.implementCores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;



/**
 */
public class SendEmailNotification {
	final static Logger  log = Logger.getLogger(SendEmailNotification.class);
	private String emailSmtpHost ;
	private int emailSmtpPort = 25;
	private String senderName ;
	private String senderChannelAccount; 
	private String[] receivers  ;
	
	/**
	 * Method main.
	 * @param args String[]
	 */
	public static void main(String[] args) {
		SendEmailNotification sender = new SendEmailNotification();
		sender.sendEmail(sender.getPseudoEmail());
	}
	public SendEmailNotification(){
		try {
			getProperties();
		} catch (Exception e) {
			log.info("SendEmailNotification retrieve properties failed");
			e.printStackTrace();
		}
	}
	/**
	 * Constructor for SendEmailNotification.
	 * @param configFile File
	 */
	public SendEmailNotification(final File configFile){
		try {
			getProperties(configFile);
		} catch (Exception e) {
			log.info("SendEmailNotification retrieve properties failed");
			e.printStackTrace();
		}
	}
	/**
	 * Method getReceiverArray.
	 * @param receivers String
	 * @return String[]
	 */
	private static String[] getReceiverArray(String receivers){
		StringTokenizer st = new StringTokenizer(receivers, ",");
		Set<String> initData = new  HashSet<String>();
		while(st.hasMoreElements()){
			String tmp =(String)st.nextElement();
			initData.add(tmp.trim());
		}
		return initData.toArray(new String[]{});
	}
	private void getProperties(){
		getProperties(new File("smtp.properties"));
	}
	/**
	 * Method getProperties.
	 * @param configFile File
	 */
	private void getProperties(final File configFile){
		if(configFile == null)
			return ;
		Properties aProperties = new Properties();
		InputStream inStream=null;
		try {
			FileInputStream fi = new FileInputStream(configFile);
			aProperties.load(fi);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		emailSmtpHost = aProperties.getProperty("emailSmtpHost");
		emailSmtpPort =Integer.parseInt( aProperties.getProperty("emailSmtpPort"));
		senderName = aProperties.getProperty("senderName");
		senderChannelAccount = aProperties.getProperty("senderChannelAccount");
		String tmp = aProperties.getProperty("receivers");
		receivers = getReceiverArray (tmp);
	}
	/**
	 * Method getPseudoEmail.
	 * @return Notification
	 */
	private Notification getPseudoEmail(){
		Notification aNotification = new Notification();
		aNotification.setTitle("iRM Notify Test  1st");
		aNotification.setContent("iRM Notification Content....");
		aNotification.setSenderName(senderName);
		aNotification.setSenderChannelAccount(senderChannelAccount);
		Hashtable<String, String> aReceivers = aNotification.getReceivers();
		int i=1;
		for(String address:receivers){
			++i;
			aReceivers.put("testUser"+i, address);
		}
		return aNotification;
	}
	/**
	 * Method makeEmail.
	 * @param title String
	 * @param content String
	 * @return Notification
	 */
	public Notification makeEmail(final String title, final String content){
		Notification aNotification = new Notification();
		aNotification.setTitle(title);
		aNotification.setContent(content);
		aNotification.setSenderName(senderName);
		aNotification.setSenderChannelAccount(senderChannelAccount);
		Hashtable<String, String> aReceivers = aNotification.getReceivers();
		int i=1;
		if(receivers!=null)
		for(String address:receivers){
			++i;
			aReceivers.put("testUser"+i, address);
		}
		return aNotification;
	}
	
	/**
	 * Method sendEmail.
	 * @param aNotification Notification
	 */
	public void sendEmail(Notification aNotification){
		StringBuffer emailBody = new StringBuffer(); 
		emailBody.append("<HTML><BODY>").append(aNotification.content).append("</BODY></HTML>");		
		HtmlEmail email = new HtmlEmail();
		email.setHostName(this.emailSmtpHost);
		email.setSmtpPort(this.emailSmtpPort);
		email.setSubject(aNotification.title);
		try{
			//Get receiver accounts and generate an InetAddress collection 
			Enumeration<String> enu = aNotification.receivers.keys();
			ArrayList<InternetAddress> receiverEmails = new ArrayList<InternetAddress>();
			while (enu.hasMoreElements()){
				String receiverEmailStr = aNotification.receivers.get(enu.nextElement());
				receiverEmails.add(new InternetAddress(receiverEmailStr) );
				receiverEmailStr = null;
			}
			
			email.setCharset("UTF-8");
			email.setTo((Collection<InternetAddress>)receiverEmails);
			email.setFrom(aNotification.senderChannelAccount, aNotification.senderName);
			email.setHtmlMsg(emailBody.toString());
			
			log.info("Prepare for sending the following mail:");
			log.info("Subject="+aNotification.title);
			log.info("From="+aNotification.senderChannelAccount + ", " + aNotification.senderName);
			StringBuffer receiverSb = new StringBuffer();
			for (InternetAddress ia:receiverEmails){
				receiverSb.append(ia.toString()).append(",");
			}
			log.info("To="+receiverSb.toString());
			receiverSb = null;
			log.info("Email Body="+emailBody.toString());
			
			email.send();
			log.info("Mail sent.");
			
		}catch (EmailException ee){
			log.info("EmailException happens, the mail is not sent...\n");
			ee.printStackTrace();
		}catch (Exception e){
			log.info("Exception happens , the mail is not sent...\n");
			e.printStackTrace();		
		}
		finally{
			
			aNotification = null;
			emailBody = null;
			email = null;
		}
	}
	
	/**
	 */
	public class Notification{
		public String title;
		public String content;
		//The display name of the entity (person, machine, system...etc) who sents this notification
		public String senderName;	
		//The account of the the entity (person, machine, system...etc) who sents this notification
		//The 'account' depends on the channel used to send the notification, for example, if the 
		//notification channel is email, the 'account' is email address, if the channel is SMS, the account
		//should be a mobile phone number
		public String senderChannelAccount;
		//The name and acount of the notification receiver.
		//The 'key' of the hashtable is receiver's display name, and the 'Value' of the hashtable is
		//receivers's account
		public Hashtable<String, String> receivers = new Hashtable<String, String>();
		
		
		/**
		 * Method getSenderName.
		 * @return String
		 */
		public String getSenderName() {
			return senderName;
		}
		/**
		 * Method setSenderName.
		 * @param senderName String
		 */
		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}
		/**
		 * Method getSenderChannelAccount.
		 * @return String
		 */
		public String getSenderChannelAccount() {
			return senderChannelAccount;
		}
		/**
		 * Method setSenderChannelAccount.
		 * @param senderChannelAccount String
		 */
		public void setSenderChannelAccount(String senderChannelAccount) {
			this.senderChannelAccount = senderChannelAccount;
		}
		/**
		 * Method getContent.
		 * @return String
		 */
		public String getContent() {
			return content;
		}
		/**
		 * Method setContent.
		 * @param content String
		 */
		public void setContent(String content) {
			this.content = content;
		}
		/**
		 * Method getTitle.
		 * @return String
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * Method setTitle.
		 * @param title String
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * Method getReceivers.
		 * @return Hashtable<String,String>
		 */
		public Hashtable<String, String> getReceivers() {
			return receivers;
		}
		/**
		 * Method setReceivers.
		 * @param receivers Hashtable<String,String>
		 */
		public void setReceivers(Hashtable<String, String> receivers) {
			this.receivers = receivers;
		}		
	}
}

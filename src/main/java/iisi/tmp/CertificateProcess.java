package iisi.tmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nbi.adapter.FilePatternBehaviorAdapter;
import nbi.adapter.ServerAdapter;
import nbi.implementCores.AbstractFtpConnect;
import nbi.protocols.Behavior;

public class CertificateProcess {
 
	public static void main(String[] args) {

		CertificateProcess process = new CertificateProcess();
		final String ip = "195.11.31.14";
		List<String> incomingTxIdList = null;
		final List<String> postProcessList = new ArrayList<String>();
	    try {
			 incomingTxIdList = FileUtils.readLines(new File("D:/userDatas/robert/Desktop/新北市/2014_0413_rldf009s_3mounth_retain_data.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	    if(incomingTxIdList != null){
	    	for(String txId :incomingTxIdList){
	    		postProcessList.add(txId.trim()+"_1.PDF");
	    		postProcessList.add(txId.trim()+"_2.PDF");
	    	}
	    }
		String areaCode="67000";
		for(int i=10;i<380;i=i+10){
			String unitSiteId = String.format("%s%03d", areaCode,i);
//			System.out.println(unitSiteId);
//			process.test01(ip, unitSiteId  );
// 			process.test02(ip, unitSiteId ,incomingTxIdList);
			process.test03(ip, unitSiteId  );
//			process.test04(ip, unitSiteId  ,postProcessList);
		}
		
//		String[] specialSiteIds ={"64000121" ,"64000122" ,"64000051" ,"64000052"  };
//		for(String unitSiteId :specialSiteIds){ 
// 			process.test04(ip, unitSiteId ,postProcessList);
//		}
		
		//如果是高雄..記的要處理64000051,64000052, 	64000121, 	64000122
	}
	public void test04(final String ip ,final String siteId,final List<String> retainTxIdList ) {
		final ServerAdapter server = new ServerAdapter();
		server.setBehaviors(null);
		server.setIp(ip);
		server.setPasswd("Sth!aix1");
		server.setPort("21");
		server.setsFtp(false);
		server.setUserName("srismapp");

		final List<FilePatternBehaviorAdapter> behaviors = new ArrayList<FilePatternBehaviorAdapter>();

		final FilePatternBehaviorAdapter behavior = new FilePatternBehaviorAdapter();

		behaviors.add(behavior);
		server.setBehaviors(behaviors);

		final AbstractFtpConnect ftpConnect = new AbstractFtpConnect(server) {
			public void processLogic(final Behavior singleBehavior)
					throws FileNotFoundException, IOException {
				String monthFolder ="201403";
				String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/APPLY/LOCAL/"+monthFolder+"/";
				String moveWorkDir1 = "/MHNFS/DATA/RL/"+siteId+"/APPLY/"; 	
				super.ftp.changeWorkingDirectory(moveWorkDir1);
				super.ftp.makeDirectory("OUT");
				String moveWorkDir2 = "/MHNFS/DATA/RL/"+siteId+"/APPLY/OUT/" ;
				super.ftp.changeWorkingDirectory(moveWorkDir2);

				super.ftp.makeDirectory(monthFolder);
				String moveWorkDir3 = "/MHNFS/DATA/RL/"+siteId+"/APPLY/OUT/"+monthFolder+"/";				
				super.ftp.changeWorkingDirectory(moveWorkDir3);
				super.ftp.changeWorkingDirectory(targetWorkDir);
				
				String[] listNames = super.ftp.listNames();
				 
				 
				for (final String name : listNames) {
					
					if(! retainTxIdList.contains(name) && name.contains(".PDF")){
						System.out.println(name);
						super.ftp.doCommand("DELE", targetWorkDir + name);

					}else  if (  retainTxIdList.contains(name) && name.contains(".PDF"))   {
						super.ftp.doCommand("RNFR", targetWorkDir + name);
						super.ftp.doCommand("RNTO", moveWorkDir3 	 + name);
					}
				} 
				System.out.println(".............");

			}
		};
		ftpConnect.connect();
	}
    
	public void test03(final String ip ,final String siteId) {
		final ServerAdapter server = new ServerAdapter();
		server.setBehaviors(null);
		server.setIp(ip);
		server.setPasswd("Sth!aix1");
		server.setPort("21");
		server.setsFtp(false);
		server.setUserName("srismapp");

		final List<FilePatternBehaviorAdapter> behaviors = new ArrayList<FilePatternBehaviorAdapter>();

		final FilePatternBehaviorAdapter behavior = new FilePatternBehaviorAdapter();

		behaviors.add(behavior);
		server.setBehaviors(behaviors);

		final AbstractFtpConnect ftpConnect = new AbstractFtpConnect(server) {
			public void processLogic(final Behavior singleBehavior)
					throws FileNotFoundException, IOException { 
//				String targetWorkDir = "/MHNFS/DATA/RL/TEMP/"+siteId+"/201402/"; 
				String targetWorkDir = "/MHNFS/DATA/RL/TEMP/"+siteId+"/"; 
//				String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/REPORT/DYNAMIC/"; 
//				String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/REPORT/OTHERS/"; 
//				String targetWorkDir = "/MHNFS/DATA/RL/TEMP/"+siteId+"/201403/";
				super.ftp.changeWorkingDirectory(targetWorkDir);
				String[] listNames = super.ftp.listNames();
				List<String> names = extractNeedProcess(listNames);
				super.ftp.makeDirectory("tmp");
				 
				for (final String name : names) { 
//					if( ( !name.contains("_RLRP2A101.pdf"))  
//							&& ( !name.contains("_RLRP3A101.pdf"))  
//							&& ( !name.contains("_RLRP3A102.pdf"))   
//							&& ( !name.contains("_RLRP1A101.pdf"))   
//							&& ( !name.contains("_RLRP02A20_1.pdf")) 
//							|| ( name.contains("_CSV") ||  name.contains("_PDF") ||  name.contains(".zip") )
//							){
//						System.out.println(name);
//						super.ftp.doCommand("DELE", targetWorkDir + name);
//					}
					if(name.contains(".pdf")|name.contains(".csv")|name.contains(".CSV" )|| ( name.contains("_CSV") ||  name.contains("_PDF") ||  name.contains(".zip")||  name.contains("TXDLRL")||  name.contains("RLDF") )){
						System.out.println(name);
						super.ftp.doCommand("DELE", targetWorkDir + name);
					}
				} 
				System.out.println(".............");

			}
		};
		ftpConnect.connect();
	}
	public void test01(final String ip ,final String siteId) {
		final ServerAdapter server = new ServerAdapter();
		server.setBehaviors(null);
		server.setIp(ip);
		server.setPasswd("Sth!aix1");
		server.setPort("21");
		server.setsFtp(false);
		server.setUserName("srismapp");

		final List<FilePatternBehaviorAdapter> behaviors = new ArrayList<FilePatternBehaviorAdapter>();

		final FilePatternBehaviorAdapter behavior = new FilePatternBehaviorAdapter();

		behaviors.add(behavior);
		server.setBehaviors(behaviors);

		final AbstractFtpConnect ftpConnect = new AbstractFtpConnect(server) {
			public void processLogic(final Behavior singleBehavior)
					throws FileNotFoundException, IOException {
				String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/APPLY/LOCAL/201403/";
//				String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/APPLY/Incoming/10302/";
				///APPLY/Incoming/10302
				super.ftp.changeWorkingDirectory(targetWorkDir);
				String[] listNames = super.ftp.listNames();
				List<String> names = extractNeedProcess(listNames);
				super.ftp.makeDirectory("tmp");
				for (final String name : names) {
					String newName = name.substring(0, 22) + "_2.PDF";
					super.ftp.doCommand("RNFR", targetWorkDir + name);
					super.ftp.doCommand("RNTO", targetWorkDir + "tmp/"
							+ newName);
				}
				super.ftp.changeWorkingDirectory(targetWorkDir + "tmp/");
				listNames = super.ftp.listNames();
				for (final String name : listNames) {
					super.ftp.doCommand("DELE", targetWorkDir + name);
					super.ftp.doCommand("RNFR", targetWorkDir + "tmp/" + name);
					super.ftp.doCommand("RNTO", targetWorkDir + name);
				}
				System.out.println(".............");

			}
		};
		ftpConnect.connect();
	}
    public List<String> extractNeedProcess(final String[] fileNames){
	final List<String> result =new ArrayList<String>();
	for(String name : fileNames){
	    if(name.length() > 28){
		result.add(name);
	    }
	}
	
	
	return result;
    }



	public void test02(final String ip ,final String siteId ,final List<String> incomingTxIdList) {
			final ServerAdapter server = new ServerAdapter();
			server.setBehaviors(null);
			server.setIp(ip);
			server.setPasswd("Sth!aix1");
			server.setPort("21");
			server.setsFtp(false);
			server.setUserName("srismapp");
	
			final List<FilePatternBehaviorAdapter> behaviors = new ArrayList<FilePatternBehaviorAdapter>();
	
			final FilePatternBehaviorAdapter behavior = new FilePatternBehaviorAdapter();
	
			behaviors.add(behavior);
			server.setBehaviors(behaviors);
	
			final AbstractFtpConnect ftpConnect = new AbstractFtpConnect(server) {
				public void processLogic(final Behavior singleBehavior)
						throws FileNotFoundException, IOException {
					String targetWorkDir = "/MHNFS/DATA/RL/"+siteId+"/APPLY/LOCAL/201402/";
					
					super.ftp.changeWorkingDirectory(targetWorkDir);
					String[] listNames = super.ftp.listNames(); 
					super.ftp.makeDirectory("tmp");
					for (final String name : listNames) {
						if(!incomingTxIdList.contains(name) && !name.contains("_1.PDF")){							
							super.ftp.doCommand("RNFR", targetWorkDir + name);
							super.ftp.doCommand("RNTO", targetWorkDir + "tmp/"
									+ name);
						} 
						
					}
					
					super.ftp.changeWorkingDirectory(targetWorkDir + "tmp/");
					listNames = super.ftp.listNames();
					for (final String name : listNames) {
//						super.ftp.dele(name);
						System.out.println(name);
					}
					System.out.println(".............");
	
				}
			};
			ftpConnect.connect();
		}
}

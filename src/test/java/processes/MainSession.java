package processes;


import java.util.ArrayList;
import java.util.List;

import nbi.adapter.FilePatternBehaviorAdapter;
import nbi.adapter.ServerAdapter;
import nbi.implementCores.ProcessServerAdapters;


/**
 */
public class MainSession { 

	private String ip="172.16.180.198";
	private String userName="nbi";
	private String passwd="nbi";
	private String port="21";

	/**
	
	 * @param server ServerAdapter
	 */
	public  void test(ServerAdapter server) {
		ProcessServerAdapters aProcessServerAdapters =new ProcessServerAdapters();
		List<ServerAdapter> serverAdapterList=new ArrayList<ServerAdapter>();
		serverAdapterList.add(server);
		aProcessServerAdapters.setClientRootPath("/home/uim/Desktop/iRMNBI");
		aProcessServerAdapters.processServerAdapterTasks(serverAdapterList);
	}
	/**
	 * Method getBasicPsedudoData.
	 * @return ServerAdapter
	 */
	private ServerAdapter getBasicPsedudoData(){
		ServerAdapter server =new ServerAdapter();
		server.setIp(ip);
		server.setPasswd(passwd);
		server.setPort(port);
		server.setUserName(userName);
		return server;
	}
	/**
	 * Method getPsedudoFtpBulkDownloadBehavior.
	 * @return ServerAdapter
	 * 2012/04/02 PM 06:41 test OK
	 */
	public ServerAdapter getPsedudoFtpBulkDownloadBehavior(){
		ServerAdapter server =getBasicPsedudoData();		
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_AXC");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_AXC.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(false);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/ExtSD7/topodata");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getPsedudoFtpSingleUploadBehavior.
	 * @return ServerAdapter
	 */
	public ServerAdapter getPsedudoFtpSingleUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();		
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_AXC");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_AXC.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/ExtSD7/test");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getPsedudoFtpBulkUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/02 PM 07:27 test OK
	 */
	public ServerAdapter getPsedudoFtpBulkUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();		
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_AXC");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_AXC.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/ExtSD7/test");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getPsedudoFtpCompressBulkUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/02 PM 07:41 test OK
	 */
	public ServerAdapter getPsedudoFtpCompressBulkUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();		
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_AXC");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(true);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_AXC.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/ExtSD7/test2");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getFtpCompressSingleUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/02 PM 02:41 test OK
	 */
	public ServerAdapter getFtpCompressSingleUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("TWM/Lynx/site");
		aFilePatternBehaviorAdapter.setBulkTransport(false);
		aFilePatternBehaviorAdapter.setCompressPut(true);
		aFilePatternBehaviorAdapter.setFilePatternString("siteview3g.xls");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getPsedudoFtpSingleDownloadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 01:33 test OK
	 */
	public ServerAdapter getPsedudoFtpSingleDownloadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("TWM/Lynx/site");
		aFilePatternBehaviorAdapter.setBulkTransport(false);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("siteview3g.xls");
		aFilePatternBehaviorAdapter.setPutOrGet(false);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);
		server.setBehaviors(aBehaviors);
		return server;
	}
	/**
	 * Method getPsedudoSFtpBulkDownloadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 02:08 test OK
	 */
	public ServerAdapter getPsedudoSFtpBulkDownloadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_FTM");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_FTM.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(false);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/Traffic/Nokia_3G/RAN/Xml/WBTS/xml/");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}

	
	/**
	 * Method getPsedudoSFtpBulkUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 02:48 test OK
	 */
	public ServerAdapter getPsedudoSFtpBulkUploadBehavior(){		
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_FTM");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_FTM.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	
	/**
	 * Method getPsedudoSFtpCompressBulkUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 03:11 test OK
	 */
	public ServerAdapter getPsedudoSFtpCompressBulkUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_FTM");
		aFilePatternBehaviorAdapter.setBulkTransport(true);
		aFilePatternBehaviorAdapter.setCompressPut(true);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_FTM.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	
	/**
	 * Method getPsedudoSFtpCompressSingleUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 03:15 test OK
	 */
	public ServerAdapter getPsedudoSFtpCompressSingleUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("/TWM/Lynx/actual_FTM");
		aFilePatternBehaviorAdapter.setBulkTransport(false);
		aFilePatternBehaviorAdapter.setCompressPut(true);
		aFilePatternBehaviorAdapter.setFilePatternString("actual_FTM.*.xml");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);		
		server.setBehaviors(aBehaviors);
		return server;
	}
	
	/**
	 * Method getPsedudoSFtpSingleDownloadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 03:35 test OK
	 */
	public ServerAdapter getPsedudoSFtpSingleDownloadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("TWM/Lynx/site");
		aFilePatternBehaviorAdapter.setBulkTransport(false);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("siteview3g.xls");
		aFilePatternBehaviorAdapter.setPutOrGet(false);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);
		server.setBehaviors(aBehaviors);
		return server;
	}
	
	/**
	 * Method getPsedudoSFtpSingleUploadBehavior.
	 * @return ServerAdapter
	 * 2012/04/03 PM 03:35 test OK
	 */
	public ServerAdapter getPsedudoSFtpSingleUploadBehavior(){
		ServerAdapter server =getBasicPsedudoData();
		server.setsFtp(true);
		List<FilePatternBehaviorAdapter> aBehaviors = server.getBehaviors();
		FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
		aFilePatternBehaviorAdapter.setLocalFolderName("TWM/Lynx/site");
		aFilePatternBehaviorAdapter.setBulkTransport(false);
		aFilePatternBehaviorAdapter.setCompressPut(false);
		aFilePatternBehaviorAdapter.setFilePatternString("siteview3g.xls");
		aFilePatternBehaviorAdapter.setPutOrGet(true);
		aFilePatternBehaviorAdapter.setRemoteFolderName("/");
		aBehaviors.add(aFilePatternBehaviorAdapter);
		server.setBehaviors(aBehaviors);
		return server;
	}
	

}

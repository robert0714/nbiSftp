package nbi.implementCores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import nbi.adapter.FilePatternBehaviorAdapter;
import nbi.adapter.ServerAdapter;
import nbi.xsd.model.FileDetailRecordType;
import nbi.xsd.model.FilePatternType;
import nbi.xsd.model.ServerType;
import nbi.xsd.model.Servers;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 */
public class LoadServerConfigXML {
	final static Logger log = LoggerFactory.getLogger(LoadServerConfigXML.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadServerConfigXML load = new LoadServerConfigXML();
		try {
			List<ServerType> alist = load.loadXML("d://serversSchema.xml");
			for (ServerType server : alist) {
				System.out.println(server.getServerName());
				List<FileDetailRecordType> patternList = server.getFilePatterns().getFilePattern();
				for (FileDetailRecordType fileDetailPattern : patternList) {
					System.out.println("Pattern: " + fileDetailPattern.getPattern());
					System.out.println("LocalFolderName: " + fileDetailPattern.getLocalFolderName());
					System.out.println("RemoteFolderName: " + fileDetailPattern.getRemoteFolderName());					
					System.out.println("BulkTransport: " + fileDetailPattern.isBulkTransport());
					System.out.println("CompressPut: " + fileDetailPattern.isCompressPut());
					System.out.println("Description: " + fileDetailPattern.getDescription());
					if(fileDetailPattern.getCustomLogicClassName()!=null){
						System.out.println("CustomLogicClassName: " + fileDetailPattern.getCustomLogicClassName().getValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method getUploadAndDownloadTasks.
	 * @param alist List<ServerType>
	 * @return List<ServerAdapter>[]
	 */
	public List<ServerAdapter>[] getUploadAndDownloadTasks(final List<ServerType> alist) {
		if (alist == null || alist.size() == 0) {
			return null;
		}
		List<ServerAdapter> uploadList = new ArrayList<ServerAdapter>();
		List<ServerAdapter> downloadList = new ArrayList<ServerAdapter>();
		for (ServerType aServerType : alist) {
			ServerAdapter uploadServer = new ServerAdapter();
			ServerAdapter downloadServer = new ServerAdapter();
			List<FilePatternBehaviorAdapter> uploadBehaviors = uploadServer.getBehaviors();
			List<FilePatternBehaviorAdapter> downloadBehaviors = downloadServer.getBehaviors();

			uploadServer.setIp(aServerType.getIp());
			uploadServer.setUserName(aServerType.getUserName());
			uploadServer.setPasswd(aServerType.getPasswd());
			uploadServer.setPort(aServerType.getPort());
			uploadServer.setsFtp(aServerType.isSFtp());

			downloadServer.setIp(aServerType.getIp());
			downloadServer.setUserName(aServerType.getUserName());
			downloadServer.setPasswd(aServerType.getPasswd());
			downloadServer.setPort(aServerType.getPort());
			downloadServer.setsFtp(aServerType.isSFtp());

			FilePatternType aFilePatterns = aServerType.getFilePatterns();
			List<FileDetailRecordType> aFilePatternList = aFilePatterns.getFilePattern();

			for(FileDetailRecordType aFileDetailRecordType :aFilePatternList){
				FilePatternBehaviorAdapter aFilePatternBehaviorAdapter = new FilePatternBehaviorAdapter();
				aFilePatternBehaviorAdapter.setBulkTransport(aFileDetailRecordType.isBulkTransport());
				aFilePatternBehaviorAdapter.setCompressPut(aFileDetailRecordType.isCompressPut());
				aFilePatternBehaviorAdapter.setFilePatternString(aFileDetailRecordType.getPattern());
				aFilePatternBehaviorAdapter.setLocalFolderName(aFileDetailRecordType.getLocalFolderName());
				aFilePatternBehaviorAdapter.setRemoteFolderName(aFileDetailRecordType.getRemoteFolderName());
				aFilePatternBehaviorAdapter.setPutOrGet(aFileDetailRecordType.isPutOrGet());
				if(aFileDetailRecordType.getCustomLogicClassName()!=null){
					aFilePatternBehaviorAdapter.setCustomLogicClassName(aFileDetailRecordType.getCustomLogicClassName().getValue());
				}
				if(aFilePatternBehaviorAdapter.isPutOrGet()){
					//1 Download
					downloadBehaviors.add(aFilePatternBehaviorAdapter);
				}else{
					//0 Upload
					uploadBehaviors.add(aFilePatternBehaviorAdapter);
				}
			}
			uploadServer.setBehaviors(uploadBehaviors);
			downloadServer.setBehaviors(downloadBehaviors);	
			
			if(uploadServer.getBehaviors().size()>0)
				uploadList.add(uploadServer);
			if(downloadServer.getBehaviors().size()>0)
				downloadList.add(downloadServer);
		}
		List<ServerAdapter>[] result = new List  []{ uploadList,downloadList};
		return result;
	}

	/**
	 * Method loadXML.
	 * @param arg String
	 * @return List<ServerType>
	 */
	public List<ServerType> loadXML(String arg) {
		List<ServerType> serverTypeList = null;
		// create JAXBContext for the server.xsd
		try {
			final JAXBContext context = JAXBContext.newInstance("nbi.xsd.model");
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			Servers servers = (nbi.xsd.model.Servers) unmarshaller.unmarshal(new FileInputStream(arg));
			serverTypeList = servers.getServer();
			log.info("Success Loading configuration XML files.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			if (serverTypeList == null) {
				log.info("Failed to load XML files.");
			}
		}
		return serverTypeList;
	}

}
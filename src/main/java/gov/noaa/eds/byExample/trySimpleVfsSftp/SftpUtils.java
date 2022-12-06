package gov.noaa.eds.byExample.trySimpleVfsSftp;
 
import java.util.regex.Pattern;
 
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.UserAuthenticator; 
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All credits for
 * http://www.memorylack.com/2011/06/apache-commons-vfs-for-sftp.html
 * 
 * nestoru - 2012/01/24: Small changes: 1. Using logging instead of
 * System.out.println() 2. The initial directory is not the user home directory
 * 3. No additional slashes when adding remoteFilePath. 4. Using InputStream
 * instead of path to a local file
 * 
 * If you need extra functionality check out the url above
 * 
 * 
 * 
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SftpUtils { 
	protected final static Logger log = LoggerFactory.getLogger(SftpUtils.class);

	/**
	 * Method upload.
	 * @param hostName String
	 * @param username String
	 * @param password String
	 * @param localFilePath String
	 * @param fileName String
	 * @param remoteFilePath String
	 */
	public static void upload(String hostName, String username, String password, String localFilePath, String fileName, String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			String sftpUri = createConnectionString(hostName, username, password, remoteFilePath);
			FileSystemOptions opts = createDefaultOptionsV2(username, password);
			manager.init();
			FileObject fileObject = manager.resolveFile(sftpUri + "/" + fileName, opts);
			FileObject localFileObject = manager.resolveFile(localFilePath + "/" + fileName);
			fileObject.copyFrom(localFileObject, Selectors.SELECT_SELF);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	/**
	 * Method uploadV2.
	 * @param hostName String
	 * @param username String
	 * @param password String
	 * @param localFilePath String
	 * @param fileName String
	 * @param remoteFilePath String
	 */
	public static void uploadV2(String hostName, String username, String password, String localFilePath, String fileName, String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			String sftpUri = createConnectionString(hostName, username, password, remoteFilePath);
			FileSystemOptions opts = createDefaultOptionsV2(username, password);
			manager.init();
			behavior(localFilePath + "/" + fileName, sftpUri + "/" + fileName, opts, manager);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	/**
	 * Method downloadV1.
	 * @param hostName String
	 * @param username String
	 * @param password String
	 * @param localFilePath String
	 * @param remoteFilePath String
	 */
	public static void downloadV1(String hostName, String username, String password, String localFilePath, String remoteFilePath) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			String sftpUri = createConnectionString(hostName, username, password, remoteFilePath);
			FileSystemOptions opts = createDefaultOptionsV2(username, password);
			manager.init();

			FileObject startPath = manager.resolveFile(sftpUri, opts);
			FileObject[] aChildren = startPath.getParent().getChildren();
			System.out.println("startPath: " + startPath.getParent().getName().getDepth());

			// Look for a file path like "smoke20070128_wkt.txt"
			// filePatternString = ".*/smoke\\d{8}_wkt\\.txt";
			String filePatternString = ".*/*";

			Pattern filePattern = Pattern.compile(filePatternString);			

			for (FileObject aFileObject : aChildren) {
				if (aFileObject.getType() == FileType.FILE) {
//					System.out.println("Examining remote file " + aFileObject.getName());
					if (filePattern.matcher(aFileObject.getName().getPath()).matches()) {
						System.out.println("aFileObject: " + aFileObject.getName());
					}
				}
			}

			// behavior(localFilePath + "/" + fileName, sftpUri + "/" +
			// fileName, opts, manager);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	/**
	 * Method behavior.
	 * @param srcFileLink String
	 * @param destiFileLink String
	 * @param opts FileSystemOptions
	 * @param manager FileSystemManager
	 * @throws FileSystemException
	 */
	public static void behavior(final String srcFileLink, final String destiFileLink, final FileSystemOptions opts, final FileSystemManager manager) throws FileSystemException {
		FileObject destiFile = manager.resolveFile(destiFileLink, opts);
		FileObject srcFile = manager.resolveFile(srcFileLink);
		destiFile.copyFrom(srcFile, Selectors.SELECT_SELF);
	}

	/**
	 * Method createConnectionString.
	 * @param hostName String
	 * @param username String
	 * @param password String
	 * @param remoteFilePath String
	 * @return String
	 */
	public static String createConnectionString(String hostName, String username, String password, String remoteFilePath) {
		// result: "sftp://user:123456@domainname.com/resume.pdf
		if (remoteFilePath == null) {
			remoteFilePath = "";
		} else {
			remoteFilePath = remoteFilePath.trim();
		}
		return "sftp://" + username + ":" + password + "@" + hostName + "/" + remoteFilePath;
	}

	/**
	 * Method createDefaultOptionsV1.
	 * @return FileSystemOptions
	 * @throws FileSystemException
	 */
	public static FileSystemOptions createDefaultOptionsV1() throws FileSystemException {
		// Create SFTP options
		FileSystemOptions opts = new FileSystemOptions();

		// SSH Key checking
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

		// Root directory set to user home
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

		// Timeout is count by Milliseconds
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		return opts;
	}

	/**
	 * Method createDefaultOptionsV2.
	 * @param username String
	 * @param password String
	 * @return FileSystemOptions
	 * @throws FileSystemException
	 */
	public static FileSystemOptions createDefaultOptionsV2(String username, String password) throws FileSystemException {
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

}
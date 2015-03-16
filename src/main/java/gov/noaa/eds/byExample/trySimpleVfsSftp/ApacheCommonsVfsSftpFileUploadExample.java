package gov.noaa.eds.byExample.trySimpleVfsSftp;


import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 */
public class ApacheCommonsVfsSftpFileUploadExample {
	public static final String FTP_URL = "192.168.111.128"; // ftp.xyz.com
	public static final String FTP_USERNAME = "robert"; // ftp_user_01
	public static final String FTP_PASSWORD = "robert"; // secret_123

	/**
	 * Method main.
	 * @param args String[]
	 */
	public static void main(String[] args) {
		String fileName = "guipythonqt.zip";
		String filePath = "d:/tmp";
		upload(filePath, fileName);
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
	 * Method createDefaultOptions.
	 * @return FileSystemOptions
	 * @throws FileSystemException
	 */
	public static FileSystemOptions createDefaultOptions() throws FileSystemException {
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
	 * Method upload.
	 * @param filePath String
	 * @param fileName String
	 */
	public static void upload(final String filePath, final String fileName) {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		try {
			String sftpUri = createConnectionString(FTP_URL, FTP_USERNAME, FTP_PASSWORD, "Desktop");
			FileSystemOptions opts = createDefaultOptions() ;			
			manager.init();
			
			FileObject fileObject = manager.resolveFile(sftpUri + "/" + fileName, opts);
			FileObject localFileObject = manager.resolveFile(filePath + "/" + fileName);
			fileObject.copyFrom(localFileObject, Selectors.SELECT_SELF);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

}

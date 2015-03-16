/*
 * App.java
 */
package gov.noaa.eds.byExample.trySimpleVfsSftp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;
import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;


/**
 * Example use of VFS sftp
 *
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class App {

    // Set these variables for your testing environment:
    private static String host = "192.168.111.128";  // Remote SFTP hostname
    private static String user = "root";      // Remote system login name
    private static String password = "stiroot";    // Remote system password
    private static String remoteDir = "/Desktop";
    // Look for a file path like "smoke20070128_wkt.txt"
//    private String filePatternString = ".*/smoke\\d{8}_wkt\\.txt";
    private static String filePatternString = ".*/UIM_architectures.png";
    // Local directory to receive file
    private static String localDir = "d:/tmp";
    
    
    private File localDirFile;
    private Pattern filePattern;
    private FileSystemManager fsManager = null;
    private FileSystemOptions opts = null;
    private FileObject sftpFile;

    private FileObject src = null; // used for cleanup in release()

    /**
     * Method main.
     * @param args String[]
     */
    public static void main(String[] args) {
     
    	download() ;
    } // main( String[] args )


    /**
     * Creates the download directory localDir if it
     * does not exist and makes a connection to the remote SFTP server.
     * 
     */
    public void initialize() {
        if (localDirFile == null) {
            localDirFile = new File(localDir);
        }
        if (!this.localDirFile.exists()) {
            localDirFile.mkdirs();
        }

        try {
            this.fsManager = VFS.getManager();
        } catch (FileSystemException ex) {
            throw new RuntimeException("failed to get fsManager from VFS", ex);
        }

        UserAuthenticator auth = new StaticUserAuthenticator(null, this.user,
                this.password);
        this.opts = new FileSystemOptions();
        try {
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts,
                    auth);
        } catch (FileSystemException ex) {
            throw new RuntimeException("setUserAuthenticator failed", ex);
        }

        this.filePattern = Pattern.compile(filePatternString);
    } // initialize()


    /**
     * Retrieves files that match the specified FileSpec from the SFTP server
     * and stores them in the local directory.
     */
    public void processDownload() {
        String startPath = "sftp://" + this.host + this.remoteDir;
        FileObject[] children;

        // Set starting path on remote SFTP server.
        try {
            this.sftpFile = this.fsManager.resolveFile(startPath, opts);

            System.out.println("SFTP connection successfully established to " +   startPath);
        } catch (FileSystemException ex) {
            throw new RuntimeException("SFTP error parsing path " +
                    this.remoteDir,
                    ex);
        }
        // Get a directory listing
        try {
           
        	System.out.println(this.sftpFile.getName().getURI());        	
        	System.out.println("children.............");
        	children = this.sftpFile.getChildren();
        	System.out.println("parent...............");
        	this.sftpFile.getParent();
        } catch (FileSystemException ex) {
            throw new RuntimeException("Error collecting directory listing of " +
                    startPath, ex);
        }

        
        filterDownloadFiles(children);
        // Set src for cleanup in release()
        src = children[0];
         
    } // process(Object obj)
    public static void download() {
    	System.out.println("SFTP download");
        App app = new App();
        app.initialize();
        app.processDownload();
        app.release();
    }
    /**
     * Retrieves files that match the specified FileSpec from the SFTP server
     * and stores them in the local directory.
     */
    public void processUpload() {
        String startPath = "sftp://" + this.host + this.remoteDir;
        FileObject[] children;

        // Set starting path on remote SFTP server.
        try {
            this.sftpFile = this.fsManager.resolveFile(startPath, opts);

            System.out.println("SFTP connection successfully established to " +   startPath);
        } catch (FileSystemException ex) {
            throw new RuntimeException("SFTP error parsing path " +
                    this.remoteDir,
                    ex);
        }
        // Get a directory listing
        try {
           
        	System.out.println(this.sftpFile.getName().getURI());
        	System.out.println("children.............");
        	children = this.sftpFile.getChildren();
        	System.out.println("parent...............");
        	this.sftpFile.getParent();
        } catch (FileSystemException ex) {
            throw new RuntimeException("Error collecting directory listing of " +
                    startPath, ex);
        }

//        filterUploadFiles(FileObject srcFile,this.sftpFile);
        // Set src for cleanup in release()
        src = children[0];
         
    } // process(Object obj)
    public static void upload() {
    	System.out.println("SFTP upload");
        App app = new App();
        app.initialize();
//        app.processUpload();
        app.release();
		
    }
    
    /**
     * Method filterUploadFiles.
     * @param srcFile FileObject
     * @param desti FileObject
     */
    public  void filterUploadFiles(FileObject srcFile,FileObject desti) {
        try {
            String relativePath =
                    File.separatorChar + srcFile.getName().getBaseName();

            if (srcFile.getType() == FileType.FILE) {
                System.out.println("Examining remote file " + srcFile.getName());

                if (!this.filePattern.matcher(srcFile.getName().getPath()).matches()) {
                    System.out.println("  Filename does not match, skipping file ." +
                            relativePath);
                   
                }

                String localUrl = "file://" + this.localDir + relativePath;
                String standardPath = this.localDir + relativePath;
                System.out.println("  Standard local path is " + standardPath);
                LocalFile localFile =  (LocalFile) this.fsManager.resolveFile(localUrl);
                System.out.println("    Resolved local file name: " +   localFile.getName());

                if (!localFile.getParent().exists()) {
                    localFile.getParent().createFolder();
                }

                System.out.println("  ### Retrieving file ###");
                localFile.copyFrom(srcFile,new AllFileSelector());
            } else {
                System.out.println("Ignoring non-file " + srcFile.getName());
            }
        } catch (FileSystemException ex) {
            throw new RuntimeException("Error getting file type for " +  srcFile.getName(), ex);
        }
    
      
    }
   
	/**
	 * Method filterDownloadFiles.
	 * @param children FileObject[]
	 */
	private void filterDownloadFiles(final FileObject[] children){
		search:
	        for (FileObject f : children) {
	            try {
	                String relativePath =
	                        File.separatorChar + f.getName().getBaseName();

	                if (f.getType() == FileType.FILE) {
	                    System.out.println("Examining remote file " + f.getName());

	                    if (!this.filePattern.matcher(f.getName().getPath()).matches()) {
	                        System.out.println("  Filename does not match, skipping file ." +
	                                relativePath);
	                        continue search;
	                    }

	                    String localUrl = "file://" + this.localDir + relativePath;
	                    String standardPath = this.localDir + relativePath;
	                    System.out.println("  Standard local path is " + standardPath);
	                    LocalFile localFile =  (LocalFile) this.fsManager.resolveFile(localUrl);
	                    System.out.println("    Resolved local file name: " +   localFile.getName());

	                    if (!localFile.getParent().exists()) {
	                        localFile.getParent().createFolder();
	                    }

	                    System.out.println("  ### Retrieving file ###");
	                    localFile.copyFrom(f,new AllFileSelector());
	                } else {
	                    System.out.println("Ignoring non-file " + f.getName());
	                }
	            } catch (FileSystemException ex) {
	                throw new RuntimeException("Error getting file type for " +  f.getName(), ex);
	            }
	        } // for (FileObject f : children)
	}

    /**
     * Release system resources, close connection to the filesystem. 
     */
    public void release() {
        FileSystem fs = null;

        fs = this.src.getFileSystem(); // This works even if the src is closed.
        this.fsManager.closeFileSystem(fs);
    } // release()
} // class App
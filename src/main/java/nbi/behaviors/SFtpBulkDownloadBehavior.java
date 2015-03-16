package nbi.behaviors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType; 
import org.apache.commons.vfs2.provider.local.LocalFile;
 

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class SFtpBulkDownloadBehavior extends AbstractSFtpBehavior{
	
	/**
	 * Method exec.
	 * @throws FileSystemException 
	 * @see nbi.protocols.ProcessBehavior#exec() */
	public void exec() throws FileSystemException {
		sequentialTransPort();
//		concurrentTransPort();
	}
	private void sequentialTransPort() throws FileSystemException{
		FileObject startPath = manager.resolveFile(sftpUri, opts);
		Pattern filePattern = Pattern.compile(filePatternString);
		FileObject[] aChildren = startPath. getChildren();
		final File mkdir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		if(!mkdir.exists() && !mkdir.isFile()){
			mkdir.mkdir();
		}
		for (FileObject aFileObject : aChildren) {
			if (aFileObject.getType() == FileType.FILE) {
				String baseName = aFileObject.getName().getBaseName();
				if (filePattern.matcher(baseName).matches()) {
					atomicDownloadUnit(mkdir, aFileObject);
				}
			}
		}
	}
	private void concurrentTransPort() throws FileSystemException{
		FileObject startPath = manager.resolveFile(sftpUri, opts);
		Pattern filePattern = Pattern.compile(filePatternString);
		FileObject[] aChildren = startPath. getChildren();
		final File mkdir = new File(getClientRootPath()+"/"+localFolderName+"/"+today);
		if(!mkdir.exists() && !mkdir.isFile()){
			mkdir.mkdir();
		}
		final List<FileObject> fileList = new ArrayList<FileObject>();
		
		for (FileObject aFileObject : aChildren) {
			if (aFileObject.getType() == FileType.FILE) {				
				String baseName = aFileObject.getName().getBaseName();
				if (filePattern.matcher(baseName).matches()) {
					fileList.add(aFileObject);
				}				
			}
		}
		

		final int poolSize =Runtime.getRuntime().availableProcessors();		
		final int numberOfParts = poolSize;
		final int number = fileList.size();
		final FileObject[] fileNameArray = fileList.toArray(new FileObject[]{});		
		final Set<FileObject> transportErrorFile = new HashSet<FileObject>();
		try {
			final List<Callable<List<FileObject>>> partitions = new ArrayList<Callable<List<FileObject>>>();
			final int chunksPerPartition = number / numberOfParts;
			for (int i = 0; i < numberOfParts; i++) {
				final int lower = (i * chunksPerPartition) + 1;
				final int upper = (i == numberOfParts - 1) ? number : lower + chunksPerPartition - 1;
				partitions.add(new Callable<List<FileObject>>() {
					public List<FileObject> call() {
						List<FileObject> errorList = new ArrayList<FileObject>();
						for(int j=lower;j<upper;j++){
							FileObject fileName = fileNameArray[j];
							try {
								atomicDownloadUnit(mkdir, fileName);
							} catch (Exception e) {
								e.printStackTrace();
								errorList.add(fileName);
							}
						}						
						return errorList;
					}
				});
			}
			final ExecutorService executorPool = Executors.newFixedThreadPool(poolSize);
			final List<Future<List<FileObject>>> resultFromParts = executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
			executorPool.shutdown();
			for (final Future<List<FileObject>> result : resultFromParts){
				transportErrorFile.addAll(result.get());
			}				
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
		for (FileObject fileName : transportErrorFile) {
			atomicDownloadUnit(mkdir, fileName);
		}
	}
	private void atomicDownloadUnit(final File mkdir,final FileObject aFileObject) throws FileSystemException{		
		String baseName = aFileObject.getName().getBaseName();
//		System.out.println("File Name: " + baseName);
		String localUrl = "file://" + mkdir.getAbsolutePath()+"//"+ baseName;
		LocalFile localFile =  (LocalFile) manager.resolveFile(localUrl);
		localFile.copyFrom(aFileObject, new AllFileSelector());
			
	}
}

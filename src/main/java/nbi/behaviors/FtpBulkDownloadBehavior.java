package nbi.behaviors;

import java.io.File;
import java.io.IOException;
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

import nbi.utils.FtpUtils;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class FtpBulkDownloadBehavior extends AbstractFtpBehavior {
	/**
	 * Method exec.
	 * 
	 * @throws IOException
	 * @see nbi.protocols.ProcessBehavior#exec()
	 * */
	public void exec() throws IOException {
		sequentialTransPort();
//		concurrentTransPort();
	}

	private void sequentialTransPort() throws IOException {
		this.filePattern = Pattern.compile(filePatternString);
		ftp.changeWorkingDirectory(remoteFolderName);
		final Set<String> fileSet = new HashSet<String>();
		final FTPFileFilter filter = new FTPFileFilter() {
			public boolean accept(FTPFile ftpfile) {
				String realFileName = ftpfile.getName();
				if (ftpfile.isFile() && filePattern.matcher(realFileName).matches()) {
					fileSet.add(realFileName);
				}
				return false;
			}
		};
		String pathname = "";
		ftp.listFiles(pathname, filter);
		final File mkdir = new File(getClientRootPath() + "/" + localFolderName + "/" + today);
		if (!mkdir.exists() && !mkdir.isFile()) {
			mkdir.mkdir();
		}
		for (String fileName : fileSet) {
			atomicDownloadUnit(mkdir, fileName);
		}
	}
	
	/***
	 * @deprecated
	 * Through a Test , we need to arrange concurrent connections instead of transport tasks................
	 * we need to design a function to detect available amount of connection... 
	 * ***/
	private void concurrentTransPort() throws IOException {
		this.filePattern = Pattern.compile(filePatternString);
		ftp.changeWorkingDirectory(remoteFolderName);
		final Set<String> fileSet = new HashSet<String>();
		final FTPFileFilter filter = new FTPFileFilter() {
			public boolean accept(FTPFile ftpfile) {
				String realFileName = ftpfile.getName();
				if (ftpfile.isFile() && filePattern.matcher(realFileName).matches()) {
					fileSet.add(realFileName);
				}
				return false;
			}
		};
		String pathname = "";
		ftp.listFiles(pathname, filter);
		final File mkdir = new File(getClientRootPath() + "/" + localFolderName + "/" + today);
		if (!mkdir.exists() && !mkdir.isFile()) {
			mkdir.mkdir();
		}
		
		final int poolSize =Runtime.getRuntime().availableProcessors();		
		final int numberOfParts = poolSize;
		final int number = fileSet.size();
		final String[] fileNameArray = fileSet.toArray(new String[]{});
		
		final Set<String> transportErrorFile = new HashSet<String>();
		try {
			final List<Callable<List<String>>> partitions = new ArrayList<Callable<List<String>>>();
			final int chunksPerPartition = number / numberOfParts;
			for (int i = 0; i < numberOfParts; i++) {
				final int lower = (i * chunksPerPartition) + 1;
				final int upper = (i == numberOfParts - 1) ? number : lower + chunksPerPartition - 1;
				partitions.add(new Callable<List<String>>() {
					public List<String> call() {
						List<String> errorList = new ArrayList<String>();
						for(int j=lower;j<upper;j++){
							String fileName = fileNameArray[j];
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
			final List<Future<List<String>>> resultFromParts = executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
			executorPool.shutdown();
			for (final Future<List<String>> result : resultFromParts){
				transportErrorFile.addAll(result.get());
			}				
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}		
		for (String fileName : transportErrorFile) {
			atomicDownloadUnit(mkdir, fileName);
		}
	}
	private void atomicDownloadUnit(final File mkdir,final String fileName){
		File file = new File(mkdir.getAbsolutePath() + "/" + fileName);
		FtpUtils.downloadProcess(file, ftp, remoteFolderName);
		if (file.length() == 0) {
			messageList.add(file.getAbsoluteFile().getAbsolutePath() + "file length is 0");
		}
	}
}

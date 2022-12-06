package nbi.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class ZipUtils {
	static final int BUFFER = 2048;
	final static Logger  log = LoggerFactory.getLogger(ZipUtils.class);
	
	
	/**
	 * Method compress.
	 * @param fileNames Set<String>
	 * @param destiName String
	 */
	public static void compress(final Set<String> fileNames , final String destiName){
		ZipOutputStream out=null;
		try {
			BufferedInputStream origin = null;
			final FileOutputStream dest = new FileOutputStream(destiName);
			final CheckedOutputStream checksum = new    CheckedOutputStream(dest, new Adler32());
			out = new ZipOutputStream(new BufferedOutputStream(	checksum));
			// out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
			// get a list of files from current directory
			for (String fileName:fileNames) {
//				log.debug("Adding: " + fileName);
				FileInputStream fi = new FileInputStream(fileName);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(fileName);
				
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
//				log.debug("checksum: "+checksum.getChecksum().getValue());
			}			
		} catch (Exception e) {
			log.info(e.getMessage() ,e );
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					log.info(e.getMessage(),e);
					e.printStackTrace();
				}
			}
		}
	
	}
}

package gov.noaa.eds.byExample.trySimpleVfsSftp;

import java.io.IOException;

/**
 */
public class SftpUtilsTest {
	 /**
	  * Method main.
	  * @param args String[]
	  */
	 public static void main(String[] args) {
	     
		 try {
			new SftpUtilsTest().uploadTest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }    
    /**
     * Method uploadTest.
     * @throws IOException
     * @throws Exception
     */
    public void uploadTest() throws IOException, Exception  {
        String hostName = "192.168.111.128";
        String userName = "robert";
        String password = "robert";
        String localFilePath = "d:/tmp";
        String remoteFilePath = "Desktop";
        //SftpUtils.upload(hostName, userame, password, localFilePath, remoteFilePath);
       
        
//        SftpUtils.uploadV2(hostName, userName, password, localFilePath, "guipythonqt.zip", remoteFilePath);
        SftpUtils.downloadV1(hostName, userName, password, localFilePath,  remoteFilePath);
        System.out.println("exit............");
    }
}
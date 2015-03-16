package nbi.implementCores;

import nbi.behaviors.*;
import nbi.dynamic.DynamicClassFactory;
import nbi.protocols.*;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public class JudgeBehaviors {
	/**
	 * Method judgementBehavior.
	 * @param singleBehavior Behavior
	 * @param sFtp boolean
	 * @return ProcessBehavior 
	 * */
	public static ProcessBehavior judgementBehavior(Behavior singleBehavior,boolean sFtp){
		ProcessBehavior result =null;
		String customLogicClassName  =singleBehavior.getCustomLogicClassName();
		if (customLogicClassName!=null && customLogicClassName.trim().length()>0 ){
			if(sFtp){
				result = (DynamicSFtpBehavior) DynamicClassFactory.makeClass(DynamicSFtpBehavior.class,customLogicClassName);
			}else{
				result = (DynamicFtpBehavior) DynamicClassFactory.makeClass(DynamicFtpBehavior.class,customLogicClassName);
			}
		}else 
		if(sFtp){			
			if(!singleBehavior.isPutOrGet()){
				//upload
				if(singleBehavior.isBulkTransport()){
					//BulkTransport					
					
					if(singleBehavior.isCompressPut()){
						result =new SFtpCompressBulkUploadBehavior();
					}else{
						result =new SFtpBulkUploadBehavior();
					}
				}else{
					if(singleBehavior.isCompressPut()){
						result =new SFtpCompressSingleUploadBehavior();
					}else{
						result =new SFtpSingleUploadBehavior();
					}
				}
			}else{
				//download
				if(singleBehavior.isBulkTransport()){
					//BulkTransport
					result =new SFtpBulkDownloadBehavior();
				}else{
					result =new SFtpSingleDownloadBehavior();
				}
				
			}
		}else{
			if(!singleBehavior.isPutOrGet()){
				//upload
				if(singleBehavior.isBulkTransport()){
					//BulkTransport					
					
					if(singleBehavior.isCompressPut()){
						result =new FtpCompressBulkUploadBehavior();
					}else{
						result =new FtpBulkUploadBehavior();
					}
				}else{
					if(singleBehavior.isCompressPut()){
						result =new FtpCompressSingleUploadBehavior();
					}else{
						result =new FtpSingleUploadBehavior();
					}
				}
				
				
			}else{
				//download
				
				if(singleBehavior.isBulkTransport()){
					//BulkTransport
					result =new FtpBulkDownloadBehavior();
				}else{
					result =new FtpSingleDownloadBehavior();
				} 
				
			}
		}
		result.setFilePatternString(singleBehavior.getFilePatternString());
		result.setLocalFolderName(singleBehavior.getLocalFolderName());
		result.setRemoteFolderName(singleBehavior.getRemoteFolderName());
		return result;
	}
}

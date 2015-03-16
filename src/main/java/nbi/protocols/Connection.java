package nbi.protocols;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author robert.lee
 * @version $Revision: 1.0 $
 */
public interface Connection {
	/**
	 * Method processLogic.
	 * @param singleBehavior Behavior	
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 **/
	void processLogic(Behavior singleBehavior) throws FileNotFoundException,IOException;
	void connect();
}

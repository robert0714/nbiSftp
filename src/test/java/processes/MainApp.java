package processes;

import nbi.main.ConnectionProcess;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] argsaa={"serversSchema.xml","/home/uim/Desktop/iRMNBI"}; // Lab
		String[] argsaa={"serversBulkT02.xml","/home/uim/Desktop/iRMNBI"};
		ConnectionProcess.main(argsaa);
	}

}

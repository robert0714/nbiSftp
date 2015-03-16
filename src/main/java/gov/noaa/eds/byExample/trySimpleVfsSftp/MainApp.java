package gov.noaa.eds.byExample.trySimpleVfsSftp;

import java.util.Vector;

import javax.swing.ProgressMonitor;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.FileNameParser;
import org.apache.commons.vfs2.provider.sftp.SftpClientFactory;
import org.apache.commons.vfs2.provider.sftp.SftpFileNameParser;
 

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainApp aMainApp = new MainApp();

		aMainApp.testMethod01();

	}

	public void testMethod01() {

		String ip = "192.168.56.102";
		String username = "irmftp";
		String password = "irmftp123";
		FileNameParser aSftpFileNameParser = SftpFileNameParser.getInstance();
		FileSystemOptions opts = new FileSystemOptions();

		try {
			UserAuthenticator auth = new StaticUserAuthenticator(null, username, password);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		} catch (FileSystemException ex) {
			throw new RuntimeException("setUserAuthenticator failed", ex);
		}
		Session session = null;
		try {
			session = SftpClientFactory.createConnection(ip, 22, username.toCharArray(), password.toCharArray(), opts);
			//
			if (!session.isConnected()) {
				session.connect();
			}

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp command = (ChannelSftp) channel;

			command.cd("/");
			command.cd("/var/me_cx_config/config");
			command.lcd("D:/tmp");
			Vector a = command.ls("*");
			System.out.println(a.size());
			// command.get
			
			SftpProgressMonitor monitor=new MyProgressMonitor();
			int mode=ChannelSftp.OVERWRITE;
			command.get("*","d:/tmp", monitor, mode);
		   
			
			
			
			
			System.out.println("isConnected: " + session.isConnected());
			;
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (FileSystemException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
		}

	}

	public static class MyProgressMonitor implements SftpProgressMonitor {
		ProgressMonitor monitor;
		long count = 0;
		long max = 0;

		public void init(int op, String src, String dest, long max) {
			this.max = max;
			monitor = new ProgressMonitor(null, ((op == SftpProgressMonitor.PUT) ? "put" : "get") + ": " + src, "", 0, (int) max);
			count = 0;
			percent = -1;
			monitor.setProgress((int) this.count);
			monitor.setMillisToDecideToPopup(1000);
		}

		private long percent = -1;

		public boolean count(long count) {
			this.count += count;

			if (percent >= this.count * 100 / max) {
				return true;
			}
			percent = this.count * 100 / max;

			monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
			monitor.setProgress((int) this.count);

			return !(monitor.isCanceled());
		}

		public void end() {
			monitor.close();
		}
	}
}

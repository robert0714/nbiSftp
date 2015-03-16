package nbi.dynamic;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 */
public final class JavaC {
	String classpath;
	String outputdir;
	String sourcepath;
	String bootclasspath;
	String extdirs;
	String encoding;
	String target;

	/**
	 * Constructor for JavaC.
	 * @param classpath String
	 * @param outputdir String
	 */
	public JavaC(String classpath, String outputdir) {
		this.classpath = classpath;
		this.outputdir = outputdir;
	}

	/**
	 * Compile the given source files.
	 * 
	 * @param srcFiles
	
	 * @return null if success; or compilation errors */
	public String compile(String srcFiles[]) {
		StringWriter err = new StringWriter();
		PrintWriter errPrinter = new PrintWriter(err);

		String args[] = buildJavacArgs(srcFiles);
		int resultCode=0;
		try {
			resultCode = com.sun.tools.javac.Main.compile(args, errPrinter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		errPrinter.close();
		String result =  (resultCode == 0) ? null : err.toString();
		return result;
	}

	/**
	 * Method compile.
	 * @param srcFiles File[]
	 * @return String
	 */
	public String compile(File srcFiles[]) {
		String paths[] = new String[srcFiles.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = srcFiles[i].getAbsolutePath();
		}
		return compile(paths);
	}
	/**
	 * Method buildJavacArgs.
	 * @param srcFiles String[]
	 * @return String[]
	 */
	private String[] buildJavacArgs(String srcFiles[]) {
		List<String> args = new ArrayList<String>();
		if (classpath != null) {
			args.add("-classpath");
			args.add(classpath);
		}
		if (outputdir != null) {
			args.add("-d");
			args.add(outputdir);
		}
		if (sourcepath != null) {
			args.add("-sourcepath");
			args.add(sourcepath);
		}
		if (bootclasspath != null) {
			args.add("-bootclasspath");
			args.add(bootclasspath);
		}
		if (extdirs != null) {
			args.add("-extdirs");
			args.add(extdirs);
		}
		if (encoding != null) {
			args.add("-encoding");
			args.add(encoding);
		}
		if (target != null) {
			args.add("-target");
			args.add(target);
		}

		for (int i = 0; i < srcFiles.length; i++) {
			args.add(srcFiles[i]);
		}

		return args.toArray(new String[args.size()]);
	}
	/**
	 * Method getBootclasspath.
	 * @return String
	 */
	public String getBootclasspath() {
		return bootclasspath;
	}
	/**
	 * Method setBootclasspath.
	 * @param bootclasspath String
	 */
	public void setBootclasspath(String bootclasspath) {
		this.bootclasspath = bootclasspath;
	}
	/**
	 * Method getClasspath.
	 * @return String
	 */
	public String getClasspath() {
		return classpath;
	}
	/**
	 * Method setClasspath.
	 * @param classpath String
	 */
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	/**
	 * Method getEncoding.
	 * @return String
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * Method setEncoding.
	 * @param encoding String
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * Method getExtdirs.
	 * @return String
	 */
	public String getExtdirs() {
		return extdirs;
	}
	/**
	 * Method setExtdirs.
	 * @param extdirs String
	 */
	public void setExtdirs(String extdirs) {
		this.extdirs = extdirs;
	}
	/**
	 * Method getOutputdir.
	 * @return String
	 */
	public String getOutputdir() {
		return outputdir;
	}
	/**
	 * Method setOutputdir.
	 * @param outputdir String
	 */
	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}
	/**
	 * Method getSourcepath.
	 * @return String
	 */
	public String getSourcepath() {
		return sourcepath;
	}
	/**
	 * Method setSourcepath.
	 * @param sourcepath String
	 */
	public void setSourcepath(String sourcepath) {
		this.sourcepath = sourcepath;
	}
	/**
	 * Method getTarget.
	 * @return String
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * Method setTarget.
	 * @param target String
	 */
	public void setTarget(String target) {
		this.target = target;
	}
}

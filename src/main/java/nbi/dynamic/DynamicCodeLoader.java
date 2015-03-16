package nbi.dynamic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 */
public final class DynamicCodeLoader {

	private String compileClasspath;
	private ClassLoader parentClassLoader;
	private List<SourceDir> sourceDirs = new ArrayList<SourceDir>();
	private Map<String, LoadedClass> loadedClasses = new HashMap<String, LoadedClass>();

	public DynamicCodeLoader() {
		this(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Constructor for DynamicCodeLoader.
	 * @param parentClassLoader ClassLoader
	 */
	public DynamicCodeLoader(ClassLoader parentClassLoader) {
		this(extractClasspath(parentClassLoader), parentClassLoader);
	}

	/**
	 * @param compileClasspath  used to compile dynamic classes
	 * @param parentClassLoader the parent of the class loader that loads all the dynamic  classes
	 */
	public DynamicCodeLoader(String compileClasspath, ClassLoader parentClassLoader) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
	}
	
	/**
	 * Add a directory that contains the source of dynamic java code.
	 * 
	 * @param srcDir	
	 * @return true if the add is successful */
	public boolean addSourceDir(File srcDir) {
		try {
			srcDir = srcDir.getCanonicalFile();
			// info("Adding src directory [" + srcDir.getAbsolutePath() + "].");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		synchronized (sourceDirs) {
			// check existence
			for (int i = 0; i < sourceDirs.size(); i++) {
				SourceDir src = sourceDirs.get(i);
				if (src.srcDir.equals(srcDir)) {
					return false;
				}
			}
			// add new
			SourceDir src = new SourceDir(srcDir);
			sourceDirs.add(src);
			// info("Add source dir " + srcDir);
		}
		return true;
	}

	/**
	 * Returns the up-to-date dynamic class by name.
	 * 
	 * @param className	
	 * @return Class
	 * @throws ClassNotFoundException	   if source file not found or compilation error
	 *  */
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = loadedClasses.get(className);
		}
		// first access of a class
		if (loadedClass == null) { 
			String resource =  className.replace('.', '/') + ".java";
			SourceDir src = locateResource(resource);
			if (src == null) {
				throw new ClassNotFoundException("DynaCode class not found " + className);
			}
			synchronized (this) {
				// compile and load class
				loadedClass = new LoadedClass(className, src);
				synchronized (loadedClasses) {
					loadedClasses.put(className, loadedClass);
				}
			}
			return loadedClass.clazz;
		}
		// subsequent access
		if (loadedClass.isChanged()) {
			// unload and load again
			unload(loadedClass.srcDir);
			return loadClass(className);
		}
		return loadedClass.clazz;
	}

	/**
	 * Method locateResource.
	 * @param resource String
	 * @return SourceDir
	 */
	private SourceDir locateResource(String resource) {
		for (int i = 0; i < sourceDirs.size(); i++) {
			SourceDir src = sourceDirs.get(i);
			File srcDirpath = src.srcDir;
			
			File tmpFile = new File(srcDirpath, resource);
			
			if (tmpFile.exists()) {
				return src;
			}
		}
		return null;
	}

	/**
	 * Method unload.
	 * @param src SourceDir
	 */
	private void unload(SourceDir src) {
		// clear loaded classes
		synchronized (loadedClasses) {
			for (Iterator<LoadedClass> iter = loadedClasses.values().iterator(); iter.hasNext();) {
				LoadedClass loadedClass =   iter.next();
				if (loadedClass.srcDir == src) {
					iter.remove();
				}
			}
		}
		// create new class loader
		src.recreateClassLoader();
	}

	/**
	 * Get a resource from added source directories.
	 * 
	 * @param resource
	
	 * @return the resource URL, or null if resource not found */
	public URL getResource(String resource) {
		try {
			SourceDir src = locateResource(resource);
			return src == null ? null : new File(src.srcDir, resource).toURL();
		} catch (MalformedURLException e) {
			// should not happen
			return null;
		}
	}

	/**
	 * Get a resource stream from added source directories.
	 * 
	 * @param resource
	
	 * @return the resource stream, or null if resource not found */
	public InputStream getResourceAsStream(String resource) {
		try {
			SourceDir src = locateResource(resource);
			return src == null ? null : new FileInputStream(new File(src.srcDir, resource));
		} catch (FileNotFoundException e) {
			// should not happen
			return null;
		}
	}

	/**
	 * Create a proxy instance that implements the specified access interface
	 * and delegates incoming invocations to the specified dynamic
	 * implementation. The dynamic implementation may change at run-time, and
	 * the proxy will always delegates to the up-to-date implementation.
	 * 
	 * @param interfaceClass
	 *            the access interface
	 * @param implClassName
	 *            the backend dynamic implementation
	
	
	 * @return Object
	 * @throws RuntimeException
	 *             if an instance cannot be created, because of class not found
	 *             for example */
	public Object newProxyInstance(Class<?> interfaceClass, String implClassName) throws RuntimeException {
		final MyInvocationHandler handler = new MyInvocationHandler(implClassName);
		final ClassLoader loader = interfaceClass.getClassLoader();
		Object result = Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
		return result;
	}

	/**
	 */
	private class SourceDir {
		File srcDir;
		File binDir;
		JavaC javac;
		URLClassLoader classLoader;
		/**
		 * Constructor for SourceDir.
		 * @param srcDir File
		 */
		SourceDir(File srcDir) {
			this.srcDir = srcDir;

			String subdir = srcDir.getAbsolutePath().replace(':', '_').replace('/', '_').replace('\\', '_');
			this.binDir = new File(System.getProperty("java.io.tmpdir"), "dynacode/" + subdir);
			this.binDir.mkdirs();
			// info("Dynamic class output to ["+this.binDir.getAbsolutePath()+"].");

			// prepare compiler
			this.javac = new JavaC(compileClasspath, binDir.getAbsolutePath());

			// class loader
			recreateClassLoader();
		}

		void recreateClassLoader() {
			try {			
				classLoader = new URLClassLoader(new URL[] { binDir.toURL() }, parentClassLoader);	
			} catch (MalformedURLException e) {
				// should not happen
				e.printStackTrace();
			}
		}
	}

	/**
	 */
	private static class LoadedClass {
		String className;
		SourceDir srcDir;
		File srcFile;
		File binFile;
		Class<?> clazz;
		long lastModified;
		/**
		 * Constructor for LoadedClass.
		 * @param className String
		 * @param src SourceDir
		 */
		LoadedClass(String className, SourceDir src) {
			this.className = className;
			this.srcDir = src;

			String path = className.replace('.', '/');
			this.srcFile = new File(src.srcDir, path + ".java");
			this.binFile = new File(src.binDir, path + ".class");

			compileAndLoadClass();
		}

		/**
		 * Method isChanged.
		 * @return boolean
		 */
		boolean isChanged() {
			return srcFile.lastModified() != lastModified;
		}

		void compileAndLoadClass() {
			if (clazz != null) {
				return; // class already loaded
			}

			// compile, if required
			String error = null;
			if (binFile.lastModified() < srcFile.lastModified()) {
				JavaC javaccc = srcDir.javac;
				error = javaccc.compile(new File[] { srcFile });
			}
			if (error != null) {
				throw new RuntimeException("Failed to compile " + srcFile.getAbsolutePath() + ". Error: " + error);
			}

			try {
				// load class
				clazz = srcDir.classLoader.loadClass(className);

				// load class success, remember timestamp
				lastModified = srcFile.lastModified();

			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Failed to load DynaCode class " + "src=[" + srcFile.getAbsolutePath() + "], class[" + className + "].");
			}

			// info("Init " + clazz);
		}
	}

	/**
	 */
	private class MyInvocationHandler implements InvocationHandler {
		String backendClassName;
		Object backend;
		/**
		 * Constructor for MyInvocationHandler.
		 * @param className String
		 */
		MyInvocationHandler(String className) {
			backendClassName = className;
			try {
				Class<?> clz = loadClass(backendClassName);
				backend = newDynaCodeInstance(clz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Method invoke.
		 * @param proxy Object
		 * @param method Method
		 * @param args Object[]
		 * @return Object
		 * @throws Throwable
		 * @see java.lang.reflect.InvocationHandler#invoke(Object, Method, Object[])
		 */
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// check if class has been updated
			Class<?> clz = loadClass(backendClassName);
			if (backend.getClass() != clz) {
				backend = newDynaCodeInstance(clz);
			}
			try {
				// invoke on backend
				return method.invoke(backend, args);

			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
		/**
		 * Method newDynaCodeInstance.
		 * @param clz Class
		 * @return Object
		 */
		private Object newDynaCodeInstance(Class<?> clz) {
			try {
				return clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to new instance of DynaCode class " + clz.getName(), e);
			}
		}
	}

	/**
	 * Extracts a classpath string from a given class loader. Recognizes only
	 * URLClassLoader.
	 * @param cl ClassLoader
	 * @return String
	 */
    private static String extractClasspath(ClassLoader cl) {
	StringBuffer buf = new StringBuffer();
	String anotherCp = null;
	while (cl != null) {
	    info("Checking classpath for classload [" + cl.getClass().getName() + "].");
	    
//	    if (cl instanceof weblogic.utils.classloaders.GenericClassLoader) {		
//		info("It is a Weblogic GenericClassLoader, taking its classpath should be enough.");
//		anotherCp = ((weblogic.utils.classloaders.GenericClassLoader) cl).getClassPath();
//		break;
//	    } else 
		if (cl instanceof URLClassLoader) {
		URL urls[] = ((URLClassLoader) cl).getURLs();
		for (int i = 0; i < urls.length; i++) {
		    if (buf.length() > 0) {
			buf.append(File.pathSeparatorChar);
		    }

		    try {
			String decodedURL = URLDecoder.decode(urls[i].getFile().toString(), "UTF-8");
			buf.append(decodedURL);
		    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			buf.append(urls[i].getFile().toString());
		    }
		}
	    }
	    cl = cl.getParent();
	}
	if (anotherCp != null) {
	    return anotherCp;
	} else {
	    return buf.toString();
	}
    }

	/**
	 * Log a message.
	 * @param msg String
	 */
	private static void info(String msg) {
		System.out.println("[DynaCode] " + msg);
	}
}

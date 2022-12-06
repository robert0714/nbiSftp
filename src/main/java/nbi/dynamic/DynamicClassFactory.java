package nbi.dynamic;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.LoggerFactory;import org.slf4j.Logger;

/**
 * As a wrapper to call the DynamicCodeLoader to load the class on the fly. <br>
 * It will read options from a properties file so that the dynamic code loading
 * will be turned off and use tradition "new" operation to create the class.
 * 
 */
public final class DynamicClassFactory {

	// TODO: should try dependence injection instead of properties file
	private static String config = "dynamic";
	private static boolean isDynamic = true;
	private static String srcDir;
	final static Logger  log = LoggerFactory.getLogger(DynamicClassFactory.class);
	static {
		try {
			srcDir = (System.getProperty("metasolv.home") + "/src").replace(
					"\\", "/");
			ResourceBundle rb = ResourceBundle.getBundle(config);
			try {
				String isDynamicStr = rb.getString("isDynamic");
				if ("true".equalsIgnoreCase(isDynamicStr)) {
					// info("Set dynamic loading to true.");
					isDynamic = true;
				} else {
					// info("Set dynamic loading to false.");
					isDynamic = false;
				}
				srcDir = rb.getString("src.dir");
			} catch (MissingResourceException ex) {
				info("A config is missing.");
			}
		} catch (MissingResourceException ex) {
			info("Unable to find the [" + config + "].properties.");
		}
		// showConfig();
	}

	/**
	 * Method makeClass.
	 * @param interfaceClass Class
	 * @param implClassName String
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public static Object makeClass(Class interfaceClass, String implClassName) {
		if (isDynamic) {
			//info("Loading class[" + implClassName + "] dynamically.");
			DynamicCodeLoader dLoader = new DynamicCodeLoader();
			dLoader.addSourceDir(new File(srcDir));
			Object result =  dLoader.newProxyInstance(interfaceClass, implClassName);			
			return result;
		} else {
			Class clazz;
			try {
				// Note: class.forName() may still slower than new(), if it is
				// absolutely required
				// (performance), change it to use new() with
				// if (implClassName.equals("xxx")) then return new xxx but and
				// at the end to use
				// class.forName for those "unknown" class, just in case.
				//
				clazz = Class.forName(implClassName, true, Thread.currentThread().getContextClassLoader());
				return clazz.newInstance();
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException(
						"Problem in creating the class (via newInstnace)", ex);
			} catch (InstantiationException ex) {
				throw new RuntimeException(
						"Problem in creating the class (via newInstnace)", ex);
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(
						"Problem in creating the class (via newInstnace)", ex);
			}
		}
	}

	private static void showConfig() {
		info("isDynamic: " + isDynamic);
		info("srcDir: [" + srcDir + "].");
	}

	/**
	 * Method info.
	 * @param message String
	 */
	private static void info(String message) {
		log.info("[DynamicClassFactory] " + message);
	}
}

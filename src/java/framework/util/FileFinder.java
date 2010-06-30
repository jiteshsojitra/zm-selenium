//helper class for finding files
package framework.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class FileFinder {
	private static String filter = "";

	public static File[] listFilesAsArray(File directory, String fltr, boolean recurse) {
		Collection<File> files = listFiles(directory, fltr, recurse);
		File[] arr = new File[files.size()];
		return files.toArray(arr);
	}

	public static Collection<File> listFiles(File directory, String fltr, boolean recurse) {
		filter = fltr;
		Vector<File> files = new Vector<File>();

		File[] entries = directory.listFiles();

		for (File entry : entries) {
			if (filter == null || filenameFilter.accept(directory, entry.getName())) {
				files.add(entry);
			}

			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, fltr, recurse));
			}
		}
		return files;
	}

	public static File findFileInCurrentDir(String dir, String name) throws Exception {
		File file = new File(dir);
		String[] filenames = file.list();
		String filename = "";
		File myfile = new File("");

		if (null != filenames) {
			for (int i = 0; i < filenames.length; i++) {
				filename = filenames[i];
				if (filename.endsWith(name)) {
					myfile = new File(dir + File.separator + filename);
					break;
				} else {
					myfile = new File(dir + File.separator + filename);
					if (myfile.isDirectory()) {
						myfile = findFileInCurrentDir(myfile.getCanonicalPath(), name);
						if (myfile.getName().endsWith(name) && myfile.exists()) {
							break;
						}
					}
				}
			}
		}
		if (!myfile.getName().contains(name) || !myfile.exists()) {
			myfile = new File("");
		}
		return myfile;
	}

	public static FilenameFilter filenameFilter = new FilenameFilter() {
		public boolean accept(File file, String fname) {
			return fname.contains(filter);
		}
	};
	// String[] fnl = file.list(filenameFilter);

	// FileFilter fileFilter = new FileFilter() {
	// public boolean accept(File file) {
	// return file.isDirectory();
	// }
	// };
	// File[] fl = file.listFiles(fileFilter);;

	private static class CurClassGetter extends SecurityManager {
		private Class<?> getCurrentClass() {
			return getClassContext()[1];
		}
	}
	
	//for unit test need to change access to public
	private static void main(String[] args) {
		ZimbraSeleniumLogger.setmLog(new CurClassGetter().getCurrentClass());
		
		File dir = new File("."), f = new File(".");
		String fname = "config.properties";

		try {
			dir = new File(dir.getCanonicalPath());
		} catch (Exception ex) {
			ZimbraSeleniumLogger.mLog.error("Exception: " + ex);
		}
		Collection<File> files = listFiles(dir, fname, true);
		Iterator<File> it = files.iterator();
		while (it.hasNext()) {
			f = it.next();
			System.out.println(f.getPath());
		}

		File[] farr = listFilesAsArray(dir, fname, true);
		if (farr != null && farr.length > 0){
			for(File fn : farr)
			System.out.println(fn.getPath());
		}
		
		try {
			f = findFileInCurrentDir(dir.getCanonicalPath(), fname);
		} catch (Exception ex) {
			ZimbraSeleniumLogger.mLog.error("Exception :" + ex);
		} 
		System.out.println(f.getPath());
	}
}
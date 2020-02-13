package net.mcorp.thirdeye.manifest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class JavaClassLoader extends ClassLoader {

	public final File classes_folder;
	
	public JavaClassLoader(File classes_folder) {
		this.classes_folder = classes_folder;
	}
	
	@Override
	public Class<?> findClass(String name) {
		
		try {
			File class_file = new File(classes_folder.getAbsolutePath()+"/");
			if(class_file == null || class_file.exists() == false)
				throw new FileNotFoundException("Could not find class "+name+" @ "+classes_folder.getAbsolutePath());
			URLClassLoader url_classloader = new URLClassLoader(new URL[] { class_file.toURI().toURL() });
			
			Class<?> temp =  url_classloader.loadClass(name);
			url_classloader.close();
			return temp;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		//byte[] bt = loadClassData(name);
		//return defineClass(name, bt, 0, bt.length);
	}
	  
	private byte[] loadClassData(String className) {
		try {
			File class_file = new File(classes_folder.getAbsolutePath()+"/"+className.replace(".", "/")+".class");
			if(class_file == null || class_file.exists() == false)
				throw new FileNotFoundException("Could not find class "+className+" @ "+classes_folder.getAbsolutePath());
			return Files.readAllBytes(class_file.toPath());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

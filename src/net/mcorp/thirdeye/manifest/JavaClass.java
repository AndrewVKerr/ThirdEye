package net.mcorp.thirdeye.manifest;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.mcorp.thirdeye.ThirdEye;
import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.devices.Device;

public final class JavaClass {
	
	private Device instance;
	public Device instance() { return this.instance; };
	
	private final ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>();
	public AccessRight[] getAccessRights() { return this.accessRights.toArray(new AccessRight[] {}); };
	
	private Node rootNode;
	public Node rootNode() { return this.rootNode; };
	
	private JavaClass() {};
	
	public static JavaClass newInstance(Node javaclass_node) {
		JavaClass javaclass = new JavaClass();
		javaclass.rootNode = javaclass_node;
		
		NodeList children_nodes = javaclass_node.getChildNodes();
		for(int i = 0; i < children_nodes.getLength(); i++) {
			Node child_node = children_nodes.item(i);
			
			if(child_node.getNodeType() == Node.ELEMENT_NODE) {//Node is an element.
				
				if(child_node.getNodeName().equalsIgnoreCase("accessrights")) {//Node is a container of access rights.
					NamedNodeMap accessrights = child_node.getAttributes();
					for(int j = 0; j < accessrights.getLength(); j++) {
						Node accessright_node = accessrights.item(j);
						
						if(accessright_node.getNodeType() == Node.ATTRIBUTE_NODE) {
							AccessRight accessright = AccessRight.parse(accessright_node.getNodeName());
							if(accessright == null) {
								System.err.println("Unknown AccessRight name: "+accessright_node.getNodeName());
								continue;
							}
							
							boolean should_enable = Boolean.parseBoolean(accessright_node.getNodeValue());
							if(should_enable) {
								javaclass.accessRights.add(accessright);
							}else {
								Debugger.warn().println("AccessRight "+accessright.name()+" is disabled.");
							}
						}
						
					}
				}else
				if(child_node.getNodeName().equalsIgnoreCase("classpath")) {
					String classpath = child_node.getTextContent();
					Node location_node = child_node.getAttributes().getNamedItem("jar_location");
					if(location_node == null) {
						new FileNotFoundException("No jar_location attribute added to Classpath.").printStackTrace();
						continue;
					}
					
					String location = location_node.getNodeValue();
					System.out.println(location);
					
					if(javaclass.instance == null) {
						System.out.println("Attempting to load device class");
						try {
							
							File file_classlocation = new File(location);
							System.out.println(file_classlocation);
							
							//JavaClassLoader jcl = new JavaClassLoader(file_classlocation);
							
							URLClassLoader url_classloader = new URLClassLoader(new URL[] { file_classlocation.toURI().toURL() });
							
							Class<?> loaded_class =  url_classloader.loadClass(classpath);
							url_classloader.close();
							
							System.out.println("Class: "+loaded_class);
							if(loaded_class == null) {
								Debugger.warn().println("Class could not load! ["+classpath+"]");
								continue;
							}
							System.out.println("Load success");
							if(Device.class.isAssignableFrom(loaded_class)) {
								System.out.println("Creating device");
								
								Device device = (Device) loaded_class.getConstructor(JavaClass.class).newInstance(javaclass);
								device.start();
								javaclass.instance = device;
							}
						} catch (Exception e) {
							System.out.println(e);
							e.printStackTrace();
						} catch (ClassFormatError e) {
							e.printStackTrace();
						} catch (IllegalAccessError iae) {
							iae.printStackTrace();
						}
					}else {
						Debugger.warn().println("Duplicate Classpath tag: "+classpath);
					}
				}
				
			}
		}
		
		return javaclass;
	}
	
}

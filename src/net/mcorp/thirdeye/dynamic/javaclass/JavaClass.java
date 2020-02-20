package net.mcorp.thirdeye.dynamic.javaclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.dynamic.AccessRight;
import net.mcorp.thirdeye.dynamic.Manifest;
import net.mcorp.thirdeye.dynamic.devices.Device;


/**
 * <h1>JavaClass</h1>
 * <hr>
 * <p>
 * 	This class is used by the {@linkplain Manifest} class. It is used to generate and configure new {@linkplain Device} objects from the Manifest.
 * </p>
 * @author Andrew Kerr
 */
public final class JavaClass {
	
	/**
	 * The device object created when {@linkplain #newInstance(Node)} is called.
	 */
	private Device instance;
	
	/**
	 * This method returns the {@linkplain Device} created when {@linkplain #newInstance(Node)} is called.
	 * @return The value stored at {@linkplain #instance}.
	 */
	public Device instance() { return this.instance; };
	
	/**
	 * An {@linkplain ArrayList} of {@linkplain AccessRight}'s. Defines what system rights the device has.
	 */
	private final ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>();
	
	/**
	 * The rights associated with this JavaClass and subsequently the Device stored at {@linkplain #instance}.
	 * @return The array version of {@linkplain #accessRights} using the method {@linkplain ArrayList#toArray(Object[])}. 
	 */
	public AccessRight[] getAccessRights() { return this.accessRights.toArray(new AccessRight[] {}); };
	
	/**
	 * The node that contains data for this JavaClass.
	 * @implNote This object contains no reference back tThis method is kept private as this class should not be generated outside of this class.o the actual document used to generate this object.
	 * It is just a clone of the Node('s) found in the actual document.
	 */
	private Node rootNode;
	
	/**
	 * The node that contains the data used to generate this JavaClass.
	 * @return The {@linkplain Node} stored in the private {@linkplain #rootNode} variable.
	 * @implNote The object returned contains no reference back to the actual document used to generate this object.
	 * It is just a clone of the Node('s) found in the actual document.
	 */
	public Node rootNode() { return this.rootNode; };
	
	private File workspace;
	
	public final File getWorkspace() { return workspace; };
	
	private String uid;
	
	public final String uid() { return uid; };
	
	/**
	 * The constructor for JavaClass. Must be set to private.
	 */
	private JavaClass() {};
	
	private static Node copy(Node old_node) {
		DocumentBuilderFactory doc_builder_factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder doc_builder = doc_builder_factory.newDocumentBuilder();
			Document doc = doc_builder.newDocument();
			Node new_node = doc.importNode(old_node, true);
			doc.adoptNode(new_node);
			doc.appendChild(new_node);
			return new_node;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param javaclass_node
	 * @return
	 */
	public static JavaClass newInstance(Node javaclass_node) {
		JavaClass javaclass = new JavaClass();
		javaclass.rootNode = copy(javaclass_node);
		
		Node uid;
		if((uid = javaclass_node.getAttributes().getNamedItem("uid")) != null) {
			javaclass.uid = uid.getNodeValue();
		}
		
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
					
					if(javaclass.instance == null) {
						System.out.println("Attempting to load device class");
						try {
							
							File file_classlocation = new File(location);
							
							//JavaClassLoader jcl = new JavaClassLoader(file_classlocation);
							
							URLClassLoader url_classloader = new URLClassLoader(new URL[] { file_classlocation.toURI().toURL() });
							
							Class<?> loaded_class =  url_classloader.loadClass(classpath);
							url_classloader.close();
							
							if(loaded_class == null) {
								Debugger.warn().println("Class could not load! ["+classpath+"]");
								continue;
							}
							if(Device.class.isAssignableFrom(loaded_class)) {
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
				}else
				if(child_node.getNodeName().equalsIgnoreCase("workspace")) {
					javaclass.workspace = new File(child_node.getTextContent());
				}
				
			}
		}
		
		if(javaclass.workspace == null) {
			javaclass.workspace = new File(Manifest.instance.WORKSPACE+"/"+javaclass.uid);
		}
		
		return javaclass;
	}
	
}

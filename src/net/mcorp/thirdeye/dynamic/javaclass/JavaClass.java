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
	 * @implNote This object contains no reference back to the actual document used to generate this object.
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
	
	/**
	 * {@linkplain File} - The file object for the directory used by this {@linkplain JavaClass} and subsequent {@linkplain Device}.
	 * @implNote This variable should act as a final variable once set to a non-null value it should remain as the same non-null value.
	 * This value may be generated using the {@linkplain Manifest#WORKSPACE} variable and the {@linkplain #name} variable
	 * if a name tag is not defined in the manifest file.
	 */
	private File workspace;
	
	/**
	 * The file object for the directory used by this {@linkplain JavaClass} and subsequent {@linkplain Device}.
	 * @return The {@linkplain File} stored in the private {@linkplain #rootNode} variable.
	 * @implNote This is the only directory (sub-directory's included) that the {@linkplain #instance} can interact with except the folder "/tmp".
	 */
	public final File getWorkspace() { return workspace; };
	
	/**
	 * The name of the {@linkplain Device} object. Retrieved from the manifest directly or if one is not provided then generated.
	 * @implNote This variable should act as a final variable once set to a non-null value it should remain as the same non-null value.
	 */
	private String name;

	/**
	 * The name of the {@linkplain Device} object, defined by the manifest or if not defined then is generated.
	 * @return {@linkplain String} - The name of the {@linkplain Device} object.
	 * @implNote The name is used to navigate to the {@linkplain Device} via the {@linkplain DataServer} or used
	 * by the {@linkplain #workspace} variable if one was not defined.
	 */
	public final String name() { return name; };
	
	/**
	 * {@linkplain Boolean} - If this is set to True then the {@linkplain Device#start()} method was not called and
	 * should not be called as this device has not been started for a reason. Otherwise False.
	 */
	private boolean disabled = false;
	
	/**
	 * The current disabled status for this {@linkplain JavaClass}. Set to True if the device has not been started 
	 * due to the manifest disabling it.
	 * @return {@linkplain Boolean} - The value stored in {@linkplain #disabled}.
	 * @implNote If this method returns false then this device is possibly not ready for execution. This should be checked
	 * before a manual startup for the device, if the value returned is {@linkplain Boolean#TRUE} then this device should
	 * not be started.
	 */
	public final boolean disabled() { return disabled; };
	
	/**
	 * The constructor for JavaClass. Must be set to private.
	 */
	private JavaClass() {};
	
	/**
	 * Copy's the provided {@linkplain Node} and all children Nodes onto a new Document before returning the new root node.
	 * @param old_node - {@linkplain Node} - The node to copy.
	 * @return {@linkplain Node} - A deep copy of the provided node and all children nodes.
	 */
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
	 * When a {@linkplain JavaClass} has not had a name tag assigned to it then the prefix "Device-" concatenated with this variable is assigned to the {@linkplain JavaClass#name}.
	 */
	private static int id_count = 0;
	
	/**
	 * Creates a new {@linkplain JavaClass} object using the provided {@linkplain Node}.
	 * @param node - {@linkplain Node} - The javaclass node. Should have the tag name of "JavaClass".
	 * @return {@linkplain JavaClass} - A JavaClass object generated using the provided {@linkplain Node}.
	 */
	public static JavaClass newInstance(Node node) {
		JavaClass javaclass = new JavaClass();
		javaclass.rootNode = copy(node);
		
		NodeList children_nodes = node.getChildNodes();
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
				}else
				if(child_node.getNodeName().equalsIgnoreCase("name")) {
					javaclass.name = child_node.getTextContent();
				}else
				if(child_node.getNodeName().equalsIgnoreCase("disabled")) {
					javaclass.disabled = true;
				}
				
			}
		}
		
		if(javaclass.name == null) {
			javaclass.name = "Device-"+id_count;
			id_count++;
		}
		
		if(javaclass.workspace == null) {
			javaclass.workspace = new File(Manifest.instance().WORKSPACE+"/"+javaclass.name);
		}
		
		if(javaclass.instance != null && javaclass.disabled == false)
			javaclass.instance.start();
		
		return javaclass;
	}
	
}

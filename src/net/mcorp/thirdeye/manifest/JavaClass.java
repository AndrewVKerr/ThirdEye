package net.mcorp.thirdeye.manifest;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.mcorp.thirdeye.devices.Device;

public final class JavaClass {
	
	private Device instance;
	public Device instance() { return this.instance; };
	
	private final ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>();
	public AccessRight[] getAccessRights() { return this.accessRights.toArray(new AccessRight[] {}); };
	
	private JavaClass() {};
	
	public static JavaClass newInstance(Node javaclass_node) {
		JavaClass javaclass = new JavaClass();
		
		NodeList children_nodes = javaclass_node.getChildNodes();
		for(int i = 0; i < children_nodes.getLength(); i++) {
			Node child_node = children_nodes.item(i);
			
			if(child_node.getNodeType() == Node.ELEMENT_NODE) {//Node is an element.
				
				if(child_node.getNodeName().equalsIgnoreCase("accessrights")) {//Node is a container of access rights.
					NodeList accessrights_nodes = child_node.getChildNodes();
					for(int j = 0; j < accessrights_nodes.getLength(); j++) {
						Node accessright_node = accessrights_nodes.item(j);
						
						if(accessright_node.getNodeType() == Node.ELEMENT_NODE) {
							AccessRight accessright = AccessRight.valueOf(accessright_node.getNodeName());
							if(accessright == null)
								continue;
							
							javaclass.accessRights.add(accessright);
						}
						
					}
				}else
				if(child_node.getNodeName().equalsIgnoreCase("classpath")) {
					System.out.println(child_node.getTextContent());
				}
				
			}
		}
		
		return javaclass;
	}
	
}

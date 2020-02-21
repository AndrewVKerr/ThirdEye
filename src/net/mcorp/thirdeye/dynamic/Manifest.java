package net.mcorp.thirdeye.dynamic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.mcorp.thirdeye.ThirdEye;
import net.mcorp.thirdeye.debugger.Debugger;
import net.mcorp.thirdeye.dynamic.javaclass.JavaClass;

/**
 * <h1>Manifest</h1>
 * <hr>
 * <p>
 * 	This class is responsible for maintaining a list of {@linkplain JavaClass}es. 
 * </p>
 * @author Andrew Kerr
 *
 */
public final class Manifest {
	
	private static Manifest instance = new Manifest();
	
	/**
	 * The current instance of the {@linkplain Manifest} object. If one does not exist then one will be created.
	 * @return {@linkplain Manifest} - The current and only instance of this class.
	 */
	public static final Manifest instance() {
		if(instance == null)
			instance = new Manifest();
		return instance;
	}
	
	/**
	 * An {@linkplain ArrayList} of {@linkplain JavaClass}es.
	 */
	private final ArrayList<JavaClass> classes = new ArrayList<JavaClass>();
	
	/**
	 * Returns the array stored inside {@linkplain #classes}.
	 * @return {@linkplain JavaClass}[] - An array of {@linkplain JavaClass} objects.
	 */
	public final JavaClass[] classes() {
		return classes.toArray(new JavaClass[] {});
	}
	
	/**
	 * The workspace directory for this manifest object, used as a basis for the {@linkplain JavaClass#getWorkspace()} method unless override inside manifest.xml file.
	 */
	public final File WORKSPACE;
	
	/**
	 * The constructor for the {@linkplain Manifest} class.
	 */
	private Manifest() {
		WORKSPACE = new File(ThirdEye.directory().getAbsolutePath()+"/Workspace");
	}
	
	public void read(File file) throws IOException {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    //factory.setValidating(true);
		    factory.setIgnoringElementContentWhitespace(true);
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse(file);
		    
		    NodeList nodelist_javaclass = doc.getElementsByTagName("JavaClass");
		    for(int i = 0; i < nodelist_javaclass.getLength(); i++) {
		    	Node javaclass_node = nodelist_javaclass.item(i);
		    	try {
			    	JavaClass javaclass = JavaClass.newInstance(javaclass_node);
			    	this.classes.add(javaclass);
		    	}catch(Exception e) { e.printStackTrace(); }//TODO: Add separate error handling.
		    }
		    
		    NodeList autoShutdown = doc.getElementsByTagName("AutoReboot");
		    if(autoShutdown.getLength() == 1) {
		    	if(autoShutdown.item(0).getTextContent().equalsIgnoreCase("true")) {
		    		/*Thread restartThread = Threader.instance.createThread(new Runnable() {

		    			@Override
		    			public void run() {
		    				/*try {
		    					Process process = Runtime.getRuntime().exec("shutdown -r +5");//TODO: UNLOCK RESET LIMITER
		    					process.waitFor();
		    				} catch (IOException e) {
		    					e.printStackTrace();
		    				} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				System.out.println("Scheduled reboot in 5 minutes.");
		    			}
		    			
		    		});
		    		
		    		Runtime.getRuntime().addShutdownHook(restartThread);*/
		    		Debugger.warn().println("AutoReboot hook is currently not enabled.");
		    	}
		    }
		    
		}catch(Exception e) {
			throw new IOException(e);
		}
	}
	
}

package net.mcorp.thirdeye.manifest;

import java.util.ArrayList;

public final class Manifest {

	public static final Manifest instance = new Manifest();
	
	public ArrayList<JavaClass> classes = new ArrayList<JavaClass>();
	
	private Manifest() {};
	
}

module ThirdEyeTemp {
	requires java.desktop;
	requires transitive java.xml;
	requires pi4j.core;
	
	exports net.mcorp.thirdeye;
	exports net.mcorp.thirdeye.debugger;
	exports net.mcorp.thirdeye.dynamic;
	exports net.mcorp.thirdeye.dynamic.devices;
	exports net.mcorp.thirdeye.dynamic.devices.prefabs;
	exports net.mcorp.thirdeye.dynamic.javaclass;
	exports net.mcorp.thirdeye.systems;
	exports net.mcorp.thirdeye.systems.callbacks;
}
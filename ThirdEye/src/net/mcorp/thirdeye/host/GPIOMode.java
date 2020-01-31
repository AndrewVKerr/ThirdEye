package net.mcorp.thirdeye.host;

public enum GPIOMode {
	
	Physical,
	Virtual;

	public static GPIOMode parse(String value) {
		try {
			return GPIOMode.valueOf(value);
		}catch(Exception e) {
			return Virtual;
		}
	}
	
}

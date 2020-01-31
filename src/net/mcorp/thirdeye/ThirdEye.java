package net.mcorp.thirdeye;

import net.mcorp.thirdeye.host.DeviceEnvironment;
import net.mcorp.thirdeye.host.GPIOMode;
import net.mcorp.thirdeye.host.ThirdEyeHost;

/**
 * <h1>ThirdEye</h1>
 * <hr>
 * <p>
 * 	ThirdEye is a personal project of mine, it is designed to be a simple home monitoring system. It can easily replace any existing home security alarms as it
 * 	uses several different plug and play components that can be loaded dynamically during runtime.
 * </p>
 * @author Andrew Kerr
 */
public final class ThirdEye{
	
	private String devicesPath = "./devices";
	public String devicesPath() { return this.devicesPath; };
	
	private static ThirdEyeMode instance;
	public static final ThirdEyeMode instance() { return instance; };
	
	private ThirdEye() {}
	
	public static void main(String[] args) {	
		
		String help_screen = "\n";
		help_screen += "+===========[ ThirdEye | Help ]===========+\n";
		help_screen += "| This program is designed to be a tool   |\n";
		help_screen += "| used to create a simple yet effective   |\n";
		help_screen += "| home monitoring system. It is designed  |\n";
		help_screen += "| to run on a raspberry pi and can be     |\n";
		help_screen += "| integrated into your home network.      |\n";
		help_screen += "+=========[ ThirdEye | Switches ]=========+\n\n";
		help_screen += "'--help'\t\t\t\t| Displays this screen.\n";
		help_screen += "'--host'\t\t\t\t| Starts this device as the brains of the network.\n";
		help_screen += "\t'--start-gpio'\t| Sets the gpio mode of the gpio for this device.\n";
		help_screen += "\t'--devices:<String>'\t\t| Uses the provided filepath string as the devices folder. DEFAULT'S to './devices/*'\n";
		help_screen += "\t\n";
		
		//Custom arguments for application.
		if(args.length > 0) {
			try {
				String prefix = "--";
				String parameter;
				for(String arg : args) {
					
					//Split arg into parameter (param) and the value.
					String[] split = arg.split(":");
					String switch_ = split[0];
					String value = null;
					if(split.length == 2)
						value = split[1];
					
					parameter = "help";
					if(switch_.startsWith(prefix+parameter)) {
						System.out.println(help_screen);
						System.exit(0);
					}
					
					parameter = "start-gpio";
					if(switch_.startsWith(prefix+parameter)) {
						DeviceEnvironment.getEnvironment().gpioMode(GPIOMode.Physical);
					}
					
					parameter = "-host";
					if(switch_.startsWith(prefix+parameter)) {
						if(value == null)
							throw new RuntimeException("Invalid switch/value pair, please supply a value to the switch ["+switch_+"], seperated by ':' (colon).");
						//thirdEye.is_host = Boolean.parseBoolean(value);
					}
					
					parameter = "devices";
					if(switch_.startsWith(prefix+parameter)) {
						if(value == null)
							throw new RuntimeException("Invalid switch/value pair, please supply a value to the switch ["+switch_+"], seperated by ':' (colon).");
						//thirdEye.devicesPath = value;
					}
				}
			}catch(Exception e) {
				System.out.println("===[ ThirdEye Error ]===\n\tAn unexpected error occured!\n\tError Description:\n"+e.getLocalizedMessage());
			}
		}
		
		try {
			//thirdEye.run();
			instance = new ThirdEyeHost();
			instance.run();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}

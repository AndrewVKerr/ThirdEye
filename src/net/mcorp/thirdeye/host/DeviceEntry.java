package net.mcorp.thirdeye.host;

import static java.lang.annotation.ElementType.CONSTRUCTOR;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

@Target(CONSTRUCTOR)
@Documented
public @interface DeviceEntry {}

package org.harper.otms.common.mail;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class SharedVelocityEngine {

	private static VelocityEngine engine = new VelocityEngine();

	static {
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		engine.init();
	}

	public static VelocityEngine get() {
		return engine;
	}
}

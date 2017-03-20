package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefixes;

/*
 *  This class maps the NormalUser class in the ontology
 */

@Component
public class NormalUser extends Person {

	private static NormalUser normalUserInstance;

	public NormalUser() {
		super("NormalUser", "http://iot-platform#NormalUser", Prefixes.IOT_PLATFORM);
		init();
	}

	/*
	 * This constructor is used to perform overloading constructor technique and
	 * the parameter String nothing will be passed always with null
	 * 
	 * I have done this overloaded constructor to instantiate the static
	 * normalUserInstance to avoid java.lang.StackOverflowError exception that
	 * Occur when calling init() to add properties to normalUserInstance
	 * 
	 */
	public NormalUser(String nothing) {
		super("NormalUser", "http://iot-platform#NormalUser", Prefixes.IOT_PLATFORM);
	}

	public synchronized static NormalUser getNormalUserInstance() {
		if (normalUserInstance == null) {
			normalUserInstance = new NormalUser(null);
			initNormalUserStaticInstance(normalUserInstance);
		}

		return normalUserInstance;
	}

	public static void initNormalUserStaticInstance(Class normalUserInstance) {
		normalUserInstance.getProperties().put("usesApplication", new ObjectProperty("usesApplication",
				Prefixes.IOT_PLATFORM, Application.getApplicationInstance(), false, false));

		normalUserInstance.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "usesApplication",
				"usesApplication");

		normalUserInstance.getSuperClassesList().add(Person.getPersonInstance());
	}

	private void init() {
		super.getProperties().put("usesApplication", new ObjectProperty("usesApplication", Prefixes.IOT_PLATFORM,
				Application.getApplicationInstance(), false, false));

		super.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "usesApplication", "usesApplication");

		super.getSuperClassesList().add(Person.getPersonInstance());
	}

	public static void main(String[] args) {
		NormalUser normalUser = new NormalUser();
		System.out.println(normalUser.getProperties().size());
		System.out.println(NormalUser.getNormalUserInstance().getProperties().size());
	}
}

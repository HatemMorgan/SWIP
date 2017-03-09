package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefixes;
import com.iotplatform.ontology.XSDDataTypes;

/*
 * This class maps the application class in the ontology
 */

@Component
public class Application extends Class {

	private static Application applicationInstance;

	public Application() {
		super("Application", "http://iot-platform#Application", Prefixes.IOT_PLATFORM);
		init();
	}

	/*
	 * String nothing parameter is added for overloading constructor technique
	 * because I need to initialize an instance without having properties and it
	 * will be always passed by null
	 */
	public Application(String nothing) {
		super("Application", "http://iot-platform#Application", Prefixes.IOT_PLATFORM);
	}

	public synchronized static Application getApplicationInstance() {

		if (applicationInstance == null) {
			applicationInstance = new Application(null);

		}
		return applicationInstance;
	}

	private void init() {

		/*
		 * Application Describption
		 */
		super.getProperties().put("description",
				new DataTypeProperty("description", Prefixes.IOT_PLATFORM, XSDDataTypes.string_typed, false, false));

		/*
		 * Application name
		 */
		super.getProperties().put("name",
				new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed, false, false));

		/*
		 * relation between an application and an organization the funds it . It
		 * is one to many relation because an application can be funded by more
		 * than one organization
		 */
		super.getProperties().put("fundedBy",
				new ObjectProperty("fundedBy", Prefixes.FOAF, Organization.getOrganizationInstance(), true, false));

		/*
		 * It describes the relation that an IOT application uses a system
		 * (smart campus). It is a one to many relationship because an
		 * application can use more than one system
		 */
		super.getProperties().put("usesSystem",
				new ObjectProperty("usesSystem", Prefixes.IOT_PLATFORM, IOTSystem.getIOTSystemInstance(), true, false));

		super.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "description", "description");
		super.getHtblPropUriName().put(Prefixes.FOAF.getUri() + "name", "name");
		super.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "usesSystem", "usesSystem");
		super.getHtblPropUriName().put(Prefixes.FOAF.getUri() + "fundedBy", "fundedBy");
	}

}

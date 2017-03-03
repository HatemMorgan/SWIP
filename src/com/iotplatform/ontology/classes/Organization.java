package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.Prefixes;
import com.iotplatform.ontology.XSDDataTypes;

/*
 * Organization class maps the organization class in the ontology. 
 */

@Component
public class Organization extends Agent {

	private static Organization organizationInstance;

	public Organization() {
		super("Organization", "http://xmlns.com/foaf/0.1/Organization", Prefixes.FOAF);

		super.getProperties().put("name", new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed,false,true));
		super.getProperties().put("description",
				new DataTypeProperty("description", Prefixes.IOT_PLATFORM, XSDDataTypes.string_typed,false,false));

		super.getHtblPropUriName().put(Prefixes.FOAF.getUri() + "name", "name");
		super.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "description", "description");

		super.getSuperClassesList().add(Agent.getAgentInstance());

	}

	/*
	 * this constructor is used only to construct an instance of class
	 * organization that will be used as the class type of an object so it does
	 * not need to has the associated properties of class organization . the
	 * nothing parameter that it takes will be passed as null because it is only
	 * used to allow overloading constructor technique
	 */
	public Organization(String nothing) {
		super("Organization", "http://xmlns.com/foaf/0.1/Organization", Prefixes.FOAF);
	}

	public synchronized static Organization getOrganizationInstance() {

		if (organizationInstance == null) {
			organizationInstance = new Organization(null);

		}
		return organizationInstance;
	}

}
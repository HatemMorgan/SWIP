package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
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
		super("Organization", "http://xmlns.com/foaf/0.1/Organization", Prefixes.FOAF,
				new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed, false, true));

		init();
	}

	/*
	 * This constructor is used to perform overloading constructor technique and
	 * the parameter String nothing will be passed always with null
	 * 
	 * I have done this overloaded constructor to instantiate the static
	 * organizationInstance to avoid java.lang.StackOverflowError exception that
	 * Occur when calling init() to add properties to organizationInstance
	 * 
	 */
	public Organization(String nothing) {
		super("Organization", "http://xmlns.com/foaf/0.1/Organization", Prefixes.FOAF,
				new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed, false, true));
	}

	public synchronized static Organization getOrganizationInstance() {

		if (organizationInstance == null) {
			organizationInstance = new Organization(null);
			initOrganizationStaticInstance(organizationInstance);
		}
		return organizationInstance;
	}

	public static void initOrganizationStaticInstance(Class organizationInstance) {
		organizationInstance.getProperties().put("name",
				new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed, false, true));
		organizationInstance.getProperties().put("description",
				new DataTypeProperty("description", Prefixes.IOT_PLATFORM, XSDDataTypes.string_typed, false, false));

		organizationInstance.getHtblPropUriName().put(Prefixes.FOAF.getUri() + "name", "name");
		organizationInstance.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "description", "description");

		organizationInstance.getSuperClassesList().add(Agent.getAgentInstance());

	}

	private void init() {
		super.getProperties().put("name",
				new DataTypeProperty("name", Prefixes.FOAF, XSDDataTypes.string_typed, false, true));
		super.getProperties().put("description",
				new DataTypeProperty("description", Prefixes.IOT_PLATFORM, XSDDataTypes.string_typed, false, false));

		super.getHtblPropUriName().put(Prefixes.FOAF.getUri() + "name", "name");
		super.getHtblPropUriName().put(Prefixes.IOT_PLATFORM.getUri() + "description", "description");

		super.getSuperClassesList().add(Agent.getAgentInstance());
	}

	
	public static void main(String[] args) {
		Organization organization = new Organization();
		System.out.println(organization.getProperties().size());
		System.out.println(Organization.getOrganizationInstance().getProperties().size());
	}
}

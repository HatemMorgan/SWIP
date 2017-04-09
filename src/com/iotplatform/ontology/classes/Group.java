package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefix;
import com.iotplatform.ontology.XSDDatatype;

/*
 *  This class maps the group class in the ontology 
 *  A group can contains another group or persons or organizations (any agent)
 */

@Component
public class Group extends Agent {

	private static Group groupInstance;
	private Class groupSubjectClassInstance;

	public Group() {
		super("Group", "http://xmlns.com/foaf/0.1/Group", Prefix.FOAF, "name", false);
		init();
	}

	/*
	 * This constructor is used to perform overloading constructor technique and
	 * the parameter String nothing will be passed always with null
	 * 
	 * I have done this overloaded constructor to instantiate the static
	 * groupInstance to avoid java.lang.StackOverflowError exception that Occur
	 * when calling init() to add properties to groupInstance
	 * 
	 */
	public Group(String nothing) {
		super("Group", "http://xmlns.com/foaf/0.1/Group", Prefix.FOAF, "name", null, false);
	}

	private Class getGroupSubjectClassInstance() {
		if (groupSubjectClassInstance == null)
			groupSubjectClassInstance = new Class("Group", "http://xmlns.com/foaf/0.1/Group", Prefix.FOAF, "name",
					false);

		return groupSubjectClassInstance;
	}

	public synchronized static Group getGroupInstance() {
		if (groupInstance == null) {
			groupInstance = new Group(null);
			initGroupStaticInstance(groupInstance);
			initAgentStaticInstanc(groupInstance);
		}

		return groupInstance;
	}

	public static void initGroupStaticInstance(Group groupInstance) {
		groupInstance.getProperties().put("name", new DataTypeProperty(groupInstance.getGroupSubjectClassInstance(),
				"name", Prefix.FOAF, XSDDatatype.string_typed, false, true));
		groupInstance.getProperties().put("description",
				new DataTypeProperty(groupInstance.getGroupSubjectClassInstance(), "description", Prefix.IOT_PLATFORM,
						XSDDatatype.string_typed, false, false));
		groupInstance.getProperties().put("member", new ObjectProperty(groupInstance.getGroupSubjectClassInstance(),
				"member", Prefix.FOAF, Agent.getAgentInstance(), true, false));

		groupInstance.getHtblPropUriName().put(Prefix.FOAF.getUri() + "name", "name");
		groupInstance.getHtblPropUriName().put(Prefix.IOT_PLATFORM.getUri() + "description", "description");
		groupInstance.getHtblPropUriName().put(Prefix.FOAF.getUri() + "member", "member");

		groupInstance.getSuperClassesList().add(Agent.getAgentInstance());
	}

	private void init() {
		super.getProperties().put("name", new DataTypeProperty(getGroupSubjectClassInstance(), "name", Prefix.FOAF,
				XSDDatatype.string_typed, false, true));
		super.getProperties().put("description", new DataTypeProperty(getGroupSubjectClassInstance(), "description",
				Prefix.IOT_PLATFORM, XSDDatatype.string_typed, false, false));
		super.getProperties().put("member", new ObjectProperty(getGroupSubjectClassInstance(), "member", Prefix.FOAF,
				Agent.getAgentInstance(), true, false));

		super.getHtblPropUriName().put(Prefix.FOAF.getUri() + "name", "name");
		super.getHtblPropUriName().put(Prefix.IOT_PLATFORM.getUri() + "description", "description");
		super.getHtblPropUriName().put(Prefix.FOAF.getUri() + "member", "member");

		super.getSuperClassesList().add(Agent.getAgentInstance());
	}

	public static void main(String[] args) {
		Group group = new Group();
		System.out.println(group.getProperties().size());
		System.out.println(Group.getGroupInstance().getProperties().size());
	}
}

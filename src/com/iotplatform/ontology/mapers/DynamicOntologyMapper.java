package com.iotplatform.ontology.mapers;

import java.util.Hashtable;

import com.iotplatform.ontology.Class;

public class DynamicOntologyMapper {

	private static Hashtable<String, Hashtable<String, Class>> htblappDynamicOntologyClasses;
	private static Hashtable<String, Hashtable<String, Class>> htblappDynamicOntologyClassesUri;

	private static Hashtable<String, DynamicOntologyStateEnum> htblApplicationOntologyState;

	public static Hashtable<String, Hashtable<String, Class>> getHtblappDynamicOntologyClasses() {

		if (htblappDynamicOntologyClasses == null)
			htblappDynamicOntologyClasses = new Hashtable<>();

		return htblappDynamicOntologyClasses;
	}

	public static Hashtable<String, Hashtable<String, Class>> getHtblappDynamicOntologyClassesUri() {

		if (htblappDynamicOntologyClassesUri == null)
			htblappDynamicOntologyClassesUri = new Hashtable<>();

		return htblappDynamicOntologyClassesUri;
	}

	public static Hashtable<String, DynamicOntologyStateEnum> getHtblApplicationOntologyState() {

		if (htblApplicationOntologyState == null)
			htblApplicationOntologyState = new Hashtable<>();

		return htblApplicationOntologyState;
	}

}

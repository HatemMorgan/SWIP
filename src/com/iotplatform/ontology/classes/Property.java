package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefixes;

/*
 *  This Class maps ssn:Property class in the ontology
 *  
 *  An observable Quality of an Event or Object.  That is, not a quality of an abstract entity as is 
 *  also allowed by DUL's Quality, but rather an aspect of an entity that is intrinsic to and cannot exist 
 *  without the entity and is observable by a sensor.
 */

@Component
public class Property extends Class {

	private static Property propertyInstance;

	public Property(String name, String uri, Prefixes prefix) {
		super(name, uri, prefix);
		init();
	}

	public Property() {
		super("Property", "http://purl.oclc.org/NET/ssnx/ssn#Property", Prefixes.SSN);
		init();
	}

	public synchronized static Property getPropertyInstance() {
		if (propertyInstance == null)
			propertyInstance = new Property();

		return propertyInstance;
	}

	private void init() {

		/*
		 * relation between a Property and its value of type Amount
		 */
		super.getProperties().put("hasValue",
				new ObjectProperty("hasValue", Prefixes.SSN, Amount.getAmountInstance(), false, false));

		super.getHtblPropUriName().put(Prefixes.SSN.getUri() + "hasValue", "hasValue");
	}
}

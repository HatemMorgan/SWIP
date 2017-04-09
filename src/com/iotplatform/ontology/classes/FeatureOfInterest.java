package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefix;
import com.iotplatform.ontology.XSDDatatype;

/*
 *  This Class maps the ssn:FeatureOfInterest class in the ontology
 *  
 *  A feature is an abstraction of real world phenomena (thing, person, event, etc) that are the target of sensing .
 *  
 * A relation between an observation and the entity whose quality was observed. 
 * For example, in an observation of the weight of a person, the feature of interest is the person and the 
 * quality is weight. A soil is a feature of interest has property soil tempreture and qualty is tempreture .
 *
 *Features of interest can be events or objects but not qualities ex:  accuracy is not a property of a
 * temperature but the property of a sensor or an observation procedure
 */

@Component
public class FeatureOfInterest extends Class {

	private static FeatureOfInterest featureOfInterestInstance;
	private Class featureOfInterestSubjectClassInstance;

	public FeatureOfInterest() {
		super("FeatureOfInterest", "http://purl.oclc.org/NET/ssnx/ssn#FeatureOfInterest", Prefix.SSN, null, false);
		init();
	}

	private Class getFeatureOfInterestSubjectClassInstance() {
		if (featureOfInterestSubjectClassInstance == null)
			featureOfInterestSubjectClassInstance = new Class("FeatureOfInterest",
					"http://purl.oclc.org/NET/ssnx/ssn#FeatureOfInterest", Prefix.SSN, null, false);

		return featureOfInterestSubjectClassInstance;
	}

	public synchronized static FeatureOfInterest getFeatureOfInterestInstance() {
		if (featureOfInterestInstance == null)
			featureOfInterestInstance = new FeatureOfInterest();

		return featureOfInterestInstance;
	}

	private void init() {
		this.getProperties().put("id", new DataTypeProperty(getFeatureOfInterestSubjectClassInstance(), "id",
				Prefix.IOT_LITE, XSDDatatype.string_typed, false, false));

		this.getHtblPropUriName().put(Prefix.IOT_LITE.getUri() + "id", "id");

		/*
		 * A relation between a FeatureOfInterest and a Property of that
		 * feature. A FeatureOfInterest can have more than one property.
		 */
		this.getProperties().put("hasProperty", new ObjectProperty(getFeatureOfInterestSubjectClassInstance(),
				"hasProperty", Prefix.SSN, Property.getPropertyInstance(), false, false));

		this.getHtblPropUriName().put(Prefix.SSN.getUri() + "hasProperty", "hasProperty");
	}

}

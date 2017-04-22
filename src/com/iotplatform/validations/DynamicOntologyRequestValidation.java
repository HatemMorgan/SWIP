package com.iotplatform.validations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import com.iotplatform.exceptions.InvalidDynamicOntologyException;
import com.iotplatform.ontology.Prefix;

/*
 * DynamicOntologyRequestValidation class is used to validate and parse dynamic ontology request body 
 * to be used to after that to generate SPARQL-in-SQL query to add new concepts as turtles to
 *  mainOntology of a specific application domain 
 *  
 * It checks that the request is valid by checking that the request body format is acceptable and
 * checks that the new concept does not violate any constraints:
 * 
 * 1- to insert a new class :
 * 
 * 	 insert new ontology class : name(required), uri(required), prefix(required) , 
 *	 uniqueIdentifier(not required) , listOfProperties(not required), superClassList(not required), 
 *	 typeClassList(not required), RestrictionList (not required) 
 * 
 * 2- to insert a new property:
 * 
 * 	 name(required), prefix(required), multipleValues(required), unique(required), 
 * 	 subjectClass(not required), propertyType(required), range(not required) 
 * 
 */

@Component
public class DynamicOntologyRequestValidation {

	private static Hashtable<String, Prefix> htblPrefixes;

	/*
	 * validateDynamicOntologyClassRequest validate requestBody of dynamic
	 * ontology class insertion
	 */
	private static LinkedHashMap<String, LinkedHashMap<String, Object>> validateDynamicOntologyClassRequest(
			LinkedHashMap<String, Object> htblRequestBody) {

		/*
		 * validationResult contains :
		 * 
		 * key is the classUri and its value is LinkedHashMap<String, Object>
		 * contains:
		 * 
		 * 1- key = prefixedClassName and value = prefixedClassName(eg.
		 * iot-platform:Developer)
		 * 
		 * 2- key = subClass and value = a list of subClassesURIs
		 * 
		 * 3- key = restriction and value = a linkedHashMap contains restriction
		 * (eg. {owl:onProperty = foaf:name ; owl:allValuesFrom = xsd:string .})
		 * 
		 * 4- key = classURI (a nested subclass) and value = linkedHashMap
		 * contains the same as the above 2 points because it is a new class to
		 * be inserted and but it is a subClass of the first one
		 */
		LinkedHashMap<String, LinkedHashMap<String, Object>> validationResult = new LinkedHashMap<>();

		return validationResult;
	}

	private static void validateNewClassMap(LinkedHashMap<String, LinkedHashMap<String, Object>> validationResult,
			LinkedHashMap<String, Object> newClassMap, boolean isSuperClass, String subClassPrefixedName) {

		/*
		 * check that the required fields name and prefixAlias are exist
		 */
		if (!newClassMap.containsKey("name")) {
			throw new InvalidDynamicOntologyException("Invalid Dynamic Ontology class insertion request. "
					+ "To insert a new ontology class, you must provide required field name which has "
					+ "value the new class name.");
		}

		if (!newClassMap.containsKey("prefixAlias")) {
			throw new InvalidDynamicOntologyException("Invalid Dynamic Ontology class insertion request. "
					+ "To insert a new ontology class, you must provide required field prefixAlias "
					+ "which has value the new class prefixAlias eg: foaf or iot-platform .");
		}

		/*
		 * get values name and prefixAlias keys
		 */
		String newClassName = newClassMap.get("name").toString();
		Prefix newClassPrefix = getPrefix(newClassMap.get("prefixAlias").toString());

		/*
		 * check that the newClassPrefix is a valid prefix (not equal null)
		 */
		if (newClassPrefix == null) {
			throw new InvalidDynamicOntologyException("Invalid Dynamic Ontology class insertion request. "
					+ "InValid prefixAlias with value = " + newClassMap.get("prefixAlias").toString() + "."
					+ " The prefixAlias must have a value from " + htblPrefixes.keys().toString());
		}

		/*
		 * add newClass prefixedClassName to validationResult
		 */
		String newClassPrefixedName = newClassPrefix.getPrefix() + newClassName;
		validationResult.put(newClassPrefixedName, new LinkedHashMap<String, Object>());
		validationResult.get(newClassPrefixedName).put("prefixedClassName", newClassPrefix.getPrefix() + newClassName);

		/*
		 * if isSuperClass = true it means that the passed class is a superClass
		 * of a class with subClassPrefixedName
		 */
		if (isSuperClass) {
			/*
			 * create a new keyField with name subClassList and its value is a
			 * list of subClassesPrefixedNames
			 */
			validationResult.get(newClassPrefixedName).put("subClassList", new ArrayList<String>());

			/*
			 * add subClass with subClassPrefixedName in the subClassList
			 */
			((ArrayList<String>) validationResult.get(newClassPrefixedName).get("subClassList"))
					.add(newClassPrefixedName);

		}

		/*
		 * Iterate over the htblRequestBody to validate and parse it
		 */
		Iterator<String> htblRequestBodyIter = newClassMap.keySet().iterator();

		/*
		 * flag is used to check that key is valid to throw an exception if it
		 * is not valid
		 */
		boolean flag;
		while (htblRequestBodyIter.hasNext()) {
			String key = htblRequestBodyIter.next();
			flag = false;
			/*
			 * skip name and prefixAlias keys because I used them above
			 */
			if (key.equals("name") || key.equals("prefixAlias"))
				continue;

			/*
			 * check if the requestBody has uniqueIdentifierPropertyName key
			 * field which tells that the new class has uniqueIdentifier so the
			 * system will treat the value passed of this
			 * uniqueIdentifierProperty as the subjectIdnetifier of any instance
			 * of this class
			 */
			if (key.equals("uniqueIdentifierPropertyName")) {

				/*
				 * get uniqueIdentifierPropertyName
				 */
				String uniqueIdentifierPropertyName = newClassMap.get(key).toString();

				/*
				 * add uniqueIdentiferProperty prefixedName to
				 * htblUniqueIdentifier
				 */
				validationResult.get(newClassPrefixedName).put("uniqueIdentiferProperty", uniqueIdentifierPropertyName);

				flag = true;
			}

			if (key.equals("superClassList") && newClassMap.get(key) instanceof java.util.ArrayList) {

				validationResult.get(newClassPrefixedName).put("superClassList", new ArrayList<String>());
				validateSuperClassListKeyField(newClassPrefixedName, validationResult,
						(ArrayList<Object>) newClassMap.get(key));
				flag = true;
			}

			if (!flag) {
				throw new InvalidDynamicOntologyException("Invalid Dynamic Ontology class insertion request. "
						+ "Invalid field: " + key + " Check the documentation to learn the available "
						+ "fields to insert a new ontology class");
			}

		}
	}

	/*
	 * validateSuperClassListKeyField is used to parse and validate
	 * superClassKeyField value
	 */
	private static void validateSuperClassListKeyField(String currentClassPrefixedName,
			LinkedHashMap<String, LinkedHashMap<String, Object>> validationResult, ArrayList<Object> superClassesList) {

		for (Object superClass : superClassesList) {

			/*
			 * check that the superClass is a String. Therefore it will be the
			 * prefixedName of an existing class eg. foaf:Person
			 */
			if (superClass instanceof String) {

				String superClassPrefixedName = superClass.toString();
				/*
				 * add superClassName to superClassList of currentClassURI in
				 * validationResult
				 */
				((ArrayList<String>) validationResult.get(currentClassPrefixedName).get("superClassList"))
						.add(superClassPrefixedName);

				/*
				 * add superClass's prefixedName to validationResult and add a
				 * subClassList and add the currentClassPrefixedName in
				 * subClassList
				 */
				validationResult.put(superClassPrefixedName, new LinkedHashMap<String, Object>());
				validationResult.get(superClassPrefixedName).put("subClassList", new ArrayList<String>());
				((ArrayList<String>) validationResult.get(superClassPrefixedName).get("subClassList"))
						.add(currentClassPrefixedName);
			} else {

				/*
				 * check if the superClass is a new nestedClass
				 */
				if (superClass instanceof java.util.LinkedHashMap<?, ?>) {
					validateNewClassMap(validationResult, (LinkedHashMap<String, Object>) superClass, true,
							currentClassPrefixedName);
				} else {

					/*
					 * invalid format
					 */
					throw new InvalidDynamicOntologyException("Invalid Dynamic Ontology class insertion request. "
							+ "Invalid field: superClassList, the value of superClassList field must be a list of"
							+ "either a 1- String (prefixedClassName of an existing class). "
							+ "2- An object (A nested new superClass).");
				}
			}
		}
	}

	/*
	 * getPrefix is used to get Prefix enum that maps prefixAlias and it will
	 * return null if the prefixAlias is not valid
	 */
	private static Prefix getPrefix(String prefixAlias) {
		if (htblPrefixes == null) {
			htblPrefixes = new Hashtable<>();

			for (Prefix prefix : Prefix.values()) {
				htblPrefixes.put(prefix.getPrefixName(), prefix);
			}

		}

		if (htblPrefixes.containsKey(prefixAlias)) {
			return htblPrefixes.get(prefixAlias);
		} else {
			return null;
		}
	}

}

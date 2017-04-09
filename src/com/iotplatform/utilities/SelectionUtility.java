package com.iotplatform.utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iotplatform.exceptions.DatabaseException;
import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefix;
import com.iotplatform.ontology.Property;
import com.iotplatform.ontology.XSDDatatype;
import com.iotplatform.ontology.mapers.OntologyMapper;

@Component
public class SelectionUtility {

	private DynamicPropertiesUtility dynamicPropertiesUtility;

	@Autowired
	public SelectionUtility(DynamicPropertiesUtility dynamicPropertiesUtility) {
		this.dynamicPropertiesUtility = dynamicPropertiesUtility;
	}

	/*
	 * constructQueryResult method used to return results without any prefixed
	 * ontology URIs
	 */

	private Object[] constructSinglePropertyValuePair(String applicationName, String propertyURI, Object value,
			Class subjectClass) {
		Object[] res = new Object[2];

		String propertyName = subjectClass.getHtblPropUriName().get(propertyURI);

		if (propertyName == null) {

			/*
			 * update subject class properties list by loading the dynamic
			 * properties from database
			 */

			dynamicPropertiesUtility.getDynamicProperties(applicationName, subjectClass);
			propertyName = subjectClass.getHtblPropUriName().get(propertyURI);

		}
		System.out.println(propertyName+ "   "+ propertyURI);
		Property property = subjectClass.getProperties().get(propertyName);

		if (property instanceof ObjectProperty) {
			value = value.toString().substring(Prefix.IOT_PLATFORM.getUri().length(), value.toString().length());
		} else {
			/*
			 * datatype property
			 */
			value = typeCastValueToItsDataType((DataTypeProperty) property, value);
		}

		res[0] = propertyName;
		res[1] = value;

		return res;
	}

	private static Object typeCastValueToItsDataType(DataTypeProperty dataTypeProperty, Object value) {
		if (XSDDatatype.boolean_type.equals(dataTypeProperty.getDataType())) {
			return Boolean.parseBoolean(value.toString());
		}

		if (XSDDatatype.decimal_typed.equals(dataTypeProperty.getDataType())) {
			return Double.parseDouble(value.toString());
		}

		if (XSDDatatype.float_typed.equals(dataTypeProperty.getDataType())) {
			return Float.parseFloat(value.toString());
		}

		if (XSDDatatype.integer_typed.equals(dataTypeProperty.getDataType())) {
			return Integer.parseInt(value.toString());
		}

		if (XSDDatatype.string_typed.equals(dataTypeProperty.getDataType())) {
			return value.toString();
		}

		if (XSDDatatype.dateTime_typed.equals(dataTypeProperty.getDataType())) {
			return Date.parse(value.toString());
		}

		if (XSDDatatype.double_typed.equals(dataTypeProperty.getDataType())) {
			return Double.parseDouble(value.toString());
		}
		return null;
	}
	/*
	 * constractResponeJsonObject method is responsible take two inputs
	 * 
	 * 1-The ResultSet returned from querying the application model and take the
	 * 2-subjectClass to check if the property returned has multiple values or
	 * not. If it has multiple value it should be returned as an arrayList
	 */

	public List<Hashtable<String, Object>> constractResponeJsonObjectForListSelection(String applicationName,
			ResultSet results, Class subjectClass) throws SQLException {

		List<Hashtable<String, Object>> responseJson = new ArrayList<>();

		Hashtable<Object, Hashtable<String, Object>> temp = new Hashtable<>();

		Hashtable<String, Property> subjectClassProperties = subjectClass.getProperties();

		while (results.next()) {

			Object subject = results.getObject(1);

			/*
			 * create a new hashtable to hold subject's property and value
			 */

			if (temp.size() == 0) {
				Hashtable<String, Object> htblSubjectPropVal = new Hashtable<>();
				temp.put(subject, htblSubjectPropVal);
				responseJson.add(htblSubjectPropVal);
			}

			// skip rdf:type property

			if (results.getString(2).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
				continue;
			}

			Object[] preparedPropVal = constructSinglePropertyValuePair(applicationName, results.getString(2),
					results.getString(3), subjectClass);

			String propertyName = preparedPropVal[0].toString();
			Object value = preparedPropVal[1];

			/*
			 * check if the property passed is a multiValues Property it is
			 * after calling constructSinglePropertyValuePair method because
			 * constructSinglePropertyValuePair checks if the property was
			 * cached or need to be cached
			 */

			Property property = subjectClassProperties.get(propertyName);

			if (property.isMulitpleValues()) {

				/*
				 * check if the property was added before to use the previous
				 * created value array
				 */

				if (temp.containsKey(subject)) {

					if (temp.get(subject).containsKey(propertyName)) {

						((ArrayList) temp.get(subject).get(propertyName)).add(value);
					} else {

						/*
						 * property was not added before so create a new
						 * arraylist of objects to hold values and add it to
						 * multipleValuePropNameValueArr hashtable
						 */

						ArrayList<Object> valueList = new ArrayList<>();
						valueList.add(value);
						temp.get(subject).put(propertyName, valueList);
					}
				} else {
					Hashtable<String, Object> htblAdminPropVal = new Hashtable<>();
					temp.put(subject, htblAdminPropVal);

					if (temp.get(subject).containsKey(propertyName)) {

						((ArrayList) temp.get(subject).get(propertyName)).add(value);
					} else {

						/*
						 * property was not added before so create a new
						 * arraylist of objects to hold values and add it to
						 * multipleValuePropNameValueArr hashtable
						 */

						ArrayList<Object> valueList = new ArrayList<>();
						valueList.add(value);
						temp.get(subject).put(propertyName, valueList);
					}
					responseJson.add(htblAdminPropVal);
				}

			} else {

				/*
				 * as long as the current subject equal to subject got from the
				 * results then add the property and value to the hashtable . If
				 * they are not the same this means that this is a new subject
				 * so we have to construct a new hashtable to hold its data
				 */

				if (temp.containsKey(subject)) {
					temp.get(subject).put(propertyName, value);
				} else {

					Hashtable<String, Object> htblAdminPropVal = new Hashtable<>();
					temp.put(subject, htblAdminPropVal);
					temp.get(subject).put(propertyName, value);
					responseJson.add(htblAdminPropVal);

				}
			}
		}
		return responseJson;
	}

	public static List<Hashtable<String, Object>> constructQueryResult(String applicationName, ResultSet results,
			String requestClassName, Hashtable<String, QueryVariable> htblSubjectVariables) {

		List<Hashtable<String, Object>> consturctedQueryResult = new ArrayList<>();

		/*
		 * htblIndividualSubjectIndex has subjectUri as key and index of
		 * subjectUri in consturctedQueryResult
		 */
		LinkedHashMap<String, Integer> htblIndividualSubjectIndex = new LinkedHashMap<>();

		/*
		 * htblIndividualIndex is used to hold uniqueIdentifier of an individual
		 * (key) and index of individual in list (value)
		 * 
		 * htblIndividualIndex is used only when there is an objectProperty that
		 * has multiple values so the value of this property has to be
		 * List<Hashtable<String, Object>> each row in the list represent an
		 * individual so htblIndividualIndex is used to get index of individual
		 */
		LinkedHashMap<String, Integer> htblIndividualIndex = new LinkedHashMap<>();

		/*
		 * 
		 */
		List<Hashtable<String, Object>> helperList = new ArrayList<>();

		try {
			ResultSetMetaData rsmd = results.getMetaData();

			int columnsNumber = rsmd.getColumnCount();
			while (results.next()) {

				/*
				 * SUBJECT0 must be always the first column
				 */
				String mainSubjectUri = results.getString("SUBJECT0");

				/*
				 * check if the subjectUri exist before or not in
				 * htblIndividualSubjectIndex
				 */
				if (!htblIndividualSubjectIndex.containsKey(mainSubjectUri)) {

					/*
					 * construct new data structures instances to hold data of
					 * the new subjectUri
					 */
					Hashtable<String, Object> htblSubjectVariablehtblpropVal = new Hashtable<>();

					Hashtable<String, Object> htblIndividualPropertyValue = new Hashtable<>();

					htblSubjectVariablehtblpropVal.put("subject0", htblIndividualPropertyValue);

					helperList.add(htblSubjectVariablehtblpropVal);

					/*
					 * store index of mainSubjectUri
					 * htblSubjectVariablehtblpropVal in helperList
					 */
					int index = helperList.size() - 1;
					htblIndividualSubjectIndex.put(mainSubjectUri, index);

					consturctedQueryResult.add(htblIndividualPropertyValue);

				}

				for (int i = 2; i <= columnsNumber; i++) {

					String columnName = rsmd.getColumnName(i).toLowerCase();

					if (columnName.contains("subject")) {
						int index = htblIndividualSubjectIndex.get(mainSubjectUri);

						/*
						 * get queryVariable of the subjectVariable to know its
						 * propertyName
						 */
						QueryVariable queryVariable = htblSubjectVariables.get(columnName);
						String propertyName = queryVariable.getPropertyName();

						/*
						 * get parrentSubjectVariable of the objectVariable
						 * (columnName)
						 */
						String parrentSubjectVariable = queryVariable.getSubjectVariableName();

						/*
						 * get propertyMapping of propertyName that connects
						 * subjectVariable to objectVariable (columnName)
						 */
						String classUri = queryVariable.getSubjectClassUri();
						Class propertyClass = OntologyMapper.getHtblMainOntologyClassesUriMappers().get(classUri);
						Property property = propertyClass.getProperties().get(propertyName);

						Hashtable<String, Object> htblSubjectVariablehtblpropVal = helperList.get(index);

						String individualUniqueIdentifier = results.getString(columnName);

						/*
						 * check if the subject variable exist
						 * 
						 * if it exist so I will skip this iteration because I
						 * don't have to create a new Hashtable<String, Object>
						 * instance to hold it data
						 */
						if (htblSubjectVariablehtblpropVal.containsKey(columnName)) {

							/*
							 * create new Hashtable<String, Object> instance to
							 * hold new subjectVariable data
							 */
							Hashtable<String, Object> htblPropValue = new Hashtable<>();

							/*
							 * Make sure that individualUniqueIdentifier was not
							 * added before
							 */
							if (!htblIndividualIndex.containsKey(individualUniqueIdentifier)) {

								/*
								 * get subjectVariablePropValue from
								 * htblSubjectVariablehtblpropVal
								 */
								Object parentSubjectVariablePropValue = htblSubjectVariablehtblpropVal
										.get(parrentSubjectVariable);

								/*
								 * get target htblParentFieldObject that holds
								 * the propValues of parentSubjectVariable
								 */
								Hashtable<String, Object> htblParentPropValue = getHtblPropValue(
										parentSubjectVariablePropValue);

								/*
								 * check if the property has multipleValue in
								 * order to add the new individual to list of
								 * individuals
								 */
								if (property.isMulitpleValues()) {

									/*
									 * get valueList of the property from
									 * parentVariable htblParentPropValue
									 */
									ArrayList<Hashtable<String, Object>> valueList = (ArrayList<Hashtable<String, Object>>) htblParentPropValue
											.get(propertyName);

									/*
									 * adding new individual to value List
									 * 
									 * this will also be added to list of
									 * individuals of subjectVariable stored in
									 * columnName because they both reference
									 * (pointers) the same list so adding it in
									 * one of them reflect the other
									 */
									valueList.add(htblPropValue);

									/*
									 * add index (which will be zero because it
									 * is the first time to see this
									 * subjectVariable) of the individual to
									 * htblIndividualIndex
									 */
									htblIndividualIndex.put(individualUniqueIdentifier, null);

								} else {

									/*
									 * add objectPropertyName and add reference
									 * to htblPropValue(Hashtable<String,Object)
									 * that holds the subjectVariabelIndividual
									 * data
									 */
									htblParentPropValue.put(propertyName, htblPropValue);

									/*
									 * get subjectVariablePropValue of the
									 * subjectVariable (stored in columnName)
									 * from htblSubjectVariablehtblpropVal
									 */
									Object subjectVariablePropValue = htblSubjectVariablehtblpropVal.get(columnName);

									/*
									 * if subjectVariablePropValue is an
									 * instance of hashtable<String,Object> so I
									 * will reinitialize a new instance to hold
									 * new individual data
									 */
									if (subjectVariablePropValue instanceof java.util.Hashtable<?, ?>) {
										htblSubjectVariablehtblpropVal.put(columnName, htblPropValue);

									}

									/*
									 * add index (which will be zero because it
									 * is the first time to see this
									 * subjectVariable) of the individual to
									 * htblIndividualIndex
									 */
									htblIndividualIndex.put(individualUniqueIdentifier, null);
								}

							} else {

								/*
								 * individualUniqueIdentifier was added before
								 * so skip this iteration because we do not need
								 * to override that past values because they are
								 * the same
								 */
								continue;
							}

						} else {

							/*
							 * create new Hashtable<String, Object> instance to
							 * hold new subjectVariable data
							 */
							Hashtable<String, Object> htblPropValue = new Hashtable<>();

							/*
							 * get subjectVariablePropValue from
							 * htblSubjectVariablehtblpropVal
							 */
							Object parentSubjectVariablePropValue = htblSubjectVariablehtblpropVal
									.get(parrentSubjectVariable);

							/*
							 * get target htblfieldObject that holds the
							 * propValues of parentSubjectVariable
							 */
							Hashtable<String, Object> htblParentFieldObject = getHtblPropValue(
									parentSubjectVariablePropValue);

							/*
							 * check if the property has multipleValue in order
							 * to create a new Arraylist to hold values
							 */
							if (property.isMulitpleValues()) {
								ArrayList<Hashtable<String, Object>> valueList = new ArrayList<>();
								valueList.add(htblPropValue);

								htblParentFieldObject.put(propertyName, valueList);

								/*
								 * add new subject variable and its valueList to
								 * htblSubjectVariablehtblpropVal
								 */
								htblSubjectVariablehtblpropVal.put(columnName, valueList);

								/*
								 * add index (which will be zero because it is
								 * the first time to see this subjectVariable)
								 * of the individual to htblIndividualIndex
								 */
								htblIndividualIndex.put(individualUniqueIdentifier, 0);

							} else {
								htblParentFieldObject.put(propertyName, htblPropValue);

								/*
								 * add new subject variable and its
								 * htblPropValue to
								 * htblSubjectVariablehtblpropVal
								 */
								htblSubjectVariablehtblpropVal.put(columnName, htblPropValue);

								/*
								 * add index (which will be zero because it is
								 * the first time to see this subjectVariable)
								 * of the individual to htblIndividualIndex
								 */
								htblIndividualIndex.put(individualUniqueIdentifier, 0);
							}

						}

					} else {
						/*
						 * it is a variable not a subject so get value of the
						 * variable
						 */
						Object propValue = results.getObject(i);

						/*
						 * get queryVariable of the subjectVariable to know its
						 * propertyName
						 */
						QueryVariable queryVariable = htblSubjectVariables.get(columnName);
						String propertyName = queryVariable.getPropertyName();

						/*
						 * get propertyMapping of propertyName that connects
						 * subjectVariable to objectVariable (columnName)
						 */
						String classUri = queryVariable.getSubjectClassUri();
						Class propertyClass = OntologyMapper.getHtblMainOntologyClassesUriMappers().get(classUri);
						Property property = propertyClass.getProperties().get(propertyName);

						/*
						 * construct value datatype of propValue if the property
						 * is a datatype property
						 */
						if (property instanceof DataTypeProperty) {
							propValue = typeCastValueToItsDataType((DataTypeProperty) property, propValue);
						}

						/*
						 * get subjectVariable of the objectVariable
						 * (columnName)
						 */
						String parentSubjectVariable = queryVariable.getSubjectVariableName();

						/*
						 * get parentSubjectVariablePropValue of the
						 * subjectVariable
						 */
						int index = htblIndividualSubjectIndex.get(mainSubjectUri);
						Object parentSubjectVariablePropValue = helperList.get(index).get(parentSubjectVariable);
						/*
						 * get target htblParentFieldObject that holds the
						 * propValues of subjectVariable
						 */
						Hashtable<String, Object> htblParentFieldObject;

						if (parentSubjectVariablePropValue instanceof java.util.Hashtable<?, ?>) {

							/*
							 * parentSubjectVariable has one single
							 * Hashtable<String, Object> that holds its prop
							 * values
							 */
							htblParentFieldObject = (Hashtable<String, Object>) parentSubjectVariablePropValue;

						} else {

							/*
							 * subjectVariable has list of Hashtable<String,
							 * Object> that holds all individuals' prop values
							 */
							List<Hashtable<String, Object>> htblfieldObjectList = (List<Hashtable<String, Object>>) parentSubjectVariablePropValue;

							/*
							 * get the index of the last individual because I am
							 * iterating on values so I am adding values in
							 * order s
							 */
							int lastIndividualIndex = htblfieldObjectList.size() - 1;
							htblParentFieldObject = htblfieldObjectList.get(lastIndividualIndex);
						}

						/*
						 * check if the subjectVariablePropVal contains the
						 * propertyName
						 */
						if (htblParentFieldObject.containsKey(propertyName) && property.isMulitpleValues()) {
							Object value = htblParentFieldObject.get(propertyName);

							/*
							 * if the value exist then it must be an arrayList
							 * of values so I have to add literalValue
							 */
							ArrayList<Object> valueList = (ArrayList<Object>) value;
							if (!valueList.contains(propValue)) {
								valueList.add(propValue);
							}
						} else {

							/*
							 * check if the property has multipleValue in order
							 * to create a new Arraylist to hold values
							 */
							if (property.isMulitpleValues()) {
								ArrayList<Object> valueList = new ArrayList<>();
								valueList.add(propValue);

								htblParentFieldObject.put(propertyName, valueList);

							} else {
								htblParentFieldObject.put(propertyName, propValue);

							}

						}

					}

				}
			}

		} catch (

		SQLException e) {
			throw new DatabaseException(e.getMessage(), requestClassName);
		}

		System.out.println("--->" + helperList);

		return consturctedQueryResult;
	}

	private static Hashtable<String, Object> getHtblPropValue(Object subjectVariablePropValueObject) {
		/*
		 * get target htblfieldObject that holds the propValues of
		 * subjectVariable
		 */
		Hashtable<String, Object> htblPropValue;
		if (subjectVariablePropValueObject instanceof java.util.Hashtable<?, ?>) {
			/*
			 * subjectVariable has one single Hashtable<String, Object> that
			 * holds its prop values
			 */
			htblPropValue = (Hashtable<String, Object>) subjectVariablePropValueObject;

		} else {
			/*
			 * subjectVariable has list of Hashtable<String, Object> that holds
			 * all individuals' prop values
			 */
			List<Hashtable<String, Object>> htblIndividualshtblPropValueList = (List<Hashtable<String, Object>>) subjectVariablePropValueObject;

			/*
			 * get the index of the last individual because I am iterating on
			 * values so I am adding values in order s
			 */
			int lastIndividualIndex = htblIndividualshtblPropValueList.size() - 1;
			htblPropValue = htblIndividualshtblPropValueList.get(lastIndividualIndex);
		}

		return htblPropValue;
	}

}
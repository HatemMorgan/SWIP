package com.iotplatform.ontology.classes;

import java.util.Hashtable;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.ObjectProperty;
import com.iotplatform.ontology.Prefixes;
import com.iotplatform.ontology.XSDDataTypes;

/*
 * This Class maps iot-lite:Coverage Class in the ontology
 * 
 * The coverage of an IoT device (i.e. a temperature sensor inside a room has a coverage of that room).
 */

@Component
public class Coverage extends Class {

	private static Coverage coverageInstance;
	private Hashtable<String, Class> coverageTypesList;

	public Coverage() {
		super("Coverage", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Coverage", Prefixes.IOT_LITE,
				new DataTypeProperty("id", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, true));
		coverageTypesList = new Hashtable<>();
		init();
	}

	/*
	 * This constructor is used to perform overloading constructor technique and
	 * the parameter String nothing will be passed always with null
	 * 
	 * I have done this overloaded constructor to instantiate the static
	 * systemInstance to avoid java.lang.StackOverflowError exception that Occur
	 * when calling init() to add properties to systemInstance
	 * 
	 */
	public Coverage(String nothing) {
		super("Coverage", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Coverage", Prefixes.IOT_LITE,
				new DataTypeProperty("id", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, true));
		coverageTypesList = new Hashtable<>();
	}

	public synchronized static Coverage getCoverageInstance() {
		if (coverageInstance == null) {
			coverageInstance = new Coverage(null);
			initCoverageStaticInstance(coverageInstance);
		}

		return coverageInstance;
	}

	private void init() {

		/*
		 * Add iot-lite:Circle Class to coverageTypesList
		 * 
		 * I put uniqueIdentefier to null because I defiened in coverage class
		 * which is the superClass
		 */
		Class circle = new Class("Circle", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Circle", Prefixes.IOT_LITE,
				null);
		circle.getProperties().put("radius",
				new DataTypeProperty("radius", Prefixes.IOT_LITE, XSDDataTypes.double_typed, false, false));
		circle.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "radius", "radius");

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Circle is also an
		 * instance of class Coverage
		 */
		circle.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageTypesList.put("Circle", circle);

		/*
		 * Add iot-lite:Rectange Class to coverageTypesList
		 */
		Class rectangle = new Class("Rectangle", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Rectangle",
				Prefixes.IOT_LITE, null);

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Rectangle is also an
		 * instance of class Coverage
		 */
		rectangle.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageTypesList.put("Rectangle", rectangle);

		/*
		 * Add iot-lite:Polygon Class to coverageTypesList
		 */
		Class polygon = new Class("Polygon", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Polygon", Prefixes.IOT_LITE,
				null);

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Polygon is also an
		 * instance of class Coverage
		 */
		polygon.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageTypesList.put("Polygon", polygon);

		/*
		 * Relation between coverage and its physical location described by
		 * point class
		 */
		super.getProperties().put("location",
				new ObjectProperty("location", Prefixes.GEO, Point.getPointInstacne(), false, false));

		super.getProperties().put("id",
				new DataTypeProperty("id", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, true));

		super.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "id", "id");
		super.getHtblPropUriName().put(Prefixes.GEO.getUri() + "location", "location");
	}

	private static void initCoverageStaticInstance(Coverage coverageInstance) {

		/*
		 * Add iot-lite:Circle Class to coverageTypesList
		 * 
		 * I put uniqueIdentefier to null because I defiened in coverage class
		 * which is the superClass
		 */
		Class circle = new Class("Circle", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Circle", Prefixes.IOT_LITE,
				null);
		circle.getProperties().put("radius",
				new DataTypeProperty("radius", Prefixes.IOT_LITE, XSDDataTypes.double_typed, false, false));
		circle.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "radius", "radius");

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Circle is also an
		 * instance of class Coverage
		 */
		circle.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageInstance.getCoverageTypesList().put("Circle", circle);

		/*
		 * Add iot-lite:Rectange Class to coverageTypesList
		 */
		Class rectangle = new Class("Rectangle", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Rectangle",
				Prefixes.IOT_LITE, null);

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Rectangle is also an
		 * instance of class Coverage
		 */
		rectangle.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageInstance.getCoverageTypesList().put("Rectangle", rectangle);

		/*
		 * Add iot-lite:Polygon Class to coverageTypesList
		 */
		Class polygon = new Class("Polygon", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Polygon", Prefixes.IOT_LITE,
				null);

		/*
		 * adding coverage class to superClassesList to tell the dao to add
		 * triple that expresses that an instance of class Polygon is also an
		 * instance of class Coverage
		 */
		polygon.getSuperClassesList().add(Coverage.getCoverageInstance());
		coverageInstance.getCoverageTypesList().put("Polygon", polygon);

		/*
		 * Relation between coverage and its physical location described by
		 * point class
		 */
		coverageInstance.getProperties().put("location",
				new ObjectProperty("location", Prefixes.GEO, Point.getPointInstacne(), false, false));

		coverageInstance.getProperties().put("id",
				new DataTypeProperty("id", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, true));

		coverageInstance.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "id", "id");
		coverageInstance.getHtblPropUriName().put(Prefixes.GEO.getUri() + "location", "location");

	}

	public Hashtable<String, Class> getCoverageTypesList() {
		return coverageTypesList;
	}

}

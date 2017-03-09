package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.DataTypeProperty;
import com.iotplatform.ontology.Prefixes;
import com.iotplatform.ontology.XSDDataTypes;

/*
 * This Class maps iot-lite:Service Class in the ontology
 * 
 * Service provided by an IoT Device
 * 
 */

@Component
public class Service extends Class {

	private static Service serviceInstance;

	public Service() {
		super("Service", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Service", Prefixes.IOT_LITE);
		init();
	}

	/*
	 * String nothing parameter is added for overloading constructor technique
	 * because I need to initialize an instance without having properties and it
	 * will be always passed by null
	 */
	public Service(String nothing) {
		super("Service", "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Service", Prefixes.IOT_LITE);
	}

	public synchronized static Service getServiceInstance() {
		if (serviceInstance == null)
			serviceInstance = new Service(null);

		return serviceInstance;
	}

	private void init() {

		/*
		 * Endpoint of a service (i.e. URL that provides a RESTful interface
		 * (website fo example to give more information about the service ) to
		 * access a service) ,
		 */
		super.getProperties().put("endpoint",
				new DataTypeProperty("endpoint", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, false));

		/*
		 * Description of the service.
		 */
		super.getProperties().put("interfaceDescription", new DataTypeProperty("interfaceDescription",
				Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, false));

		/*
		 * Defines the type of interface of the service endpoint.
		 */
		super.getProperties().put("interfaceType",
				new DataTypeProperty("interfaceType", Prefixes.IOT_LITE, XSDDataTypes.string_typed, false, false));

		super.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "endpoint", "endpoint");
		super.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "interfaceDescription", "interfaceDescription");
		super.getHtblPropUriName().put(Prefixes.IOT_LITE.getUri() + "interfaceType", "interfaceType");

	}
}

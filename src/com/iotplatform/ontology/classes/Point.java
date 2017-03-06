package com.iotplatform.ontology.classes;

import org.springframework.stereotype.Component;

import com.iotplatform.ontology.Class;
import com.iotplatform.ontology.Prefixes;

/*
 * This Class maps the geo:Point class in the ontology
 * 
 * Uniquely identified by lat/long/alt. i.e.
 *
 *spaciallyIntersects(P1, P2) :- lat(P1, LAT), long(P1, LONG), alt(P1, ALT),
 *lat(P2, LAT), long(P2, LONG), alt(P2, ALT).
 *
 *sameThing(P1, P2) :- type(P1, Point), type(P2, Point), spaciallyIntersects(P1, P2).
  
 */

@Component
public class Point extends Class {

	private static Point pointInstance;

	public Point() {
		super("Point", "http://www.w3.org/2003/01/geo/wgs84_pos#Point", Prefixes.GEO);
		init();
	}

	/*
	 * String nothing parameter is added for overloading constructor technique
	 * because I need to initialize an instance without having properties and it
	 * will be always passed by null
	 */
	public Point(String nothing) {
		super("Point", "http://www.w3.org/2003/01/geo/wgs84_pos#Point", Prefixes.GEO);

	}

	public synchronized static Point getPointInstacne() {
		if (pointInstance == null)
			pointInstance = new Point(null);

		return pointInstance;
	}

	private void init() {

	}

}
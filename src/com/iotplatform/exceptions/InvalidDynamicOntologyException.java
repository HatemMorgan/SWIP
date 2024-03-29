package com.iotplatform.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDynamicOntologyException extends ErrorObjException {

	public InvalidDynamicOntologyException(String exceptionMessage) {
		super(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), exceptionMessage, "Ontology");
	}

}

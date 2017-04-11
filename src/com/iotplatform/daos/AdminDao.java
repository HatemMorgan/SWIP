package com.iotplatform.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iotplatform.exceptions.DatabaseException;
import com.iotplatform.ontology.mapers.OntologyMapper;
import com.iotplatform.queries.SelectionQuery;
import com.iotplatform.query.results.SelectionQueryResults;

import oracle.spatial.rdf.client.jena.Oracle;

@Repository("adminDao")
public class AdminDao {

	private Oracle oracle;
	private SelectionQueryResults selectionUtility;

	@Autowired
	public AdminDao(Oracle oracle, SelectionQueryResults selectionUtility) {
		this.oracle = oracle;
		this.selectionUtility = selectionUtility;
	}

	/*
	 * getAdmins method returns all the admins in the passed application model
	 */

	public List<Hashtable<String, Object>> getAdmins(String applicationModelName) {

		String applicationName = applicationModelName.replaceAll(" ", "").toUpperCase().substring(0,
				applicationModelName.length() - 6);

		String queryString = SelectionQuery.constructSelectAllQueryNoFilters(
				OntologyMapper.getHtblMainOntologyClassesMappers().get("admin"), applicationModelName);
		List<Hashtable<String, Object>> adminsList = new ArrayList<>();

		try {
			ResultSet results = oracle.executeQuery(queryString, 0, 1);

			/*
			 * call constractResponeJsonObjectForListSelection method in
			 * selectionUtility class to construct the response json
			 */

			adminsList = selectionUtility.constractResponeJsonObjectForListSelection(applicationName, results,
					OntologyMapper.getHtblMainOntologyClassesMappers().get("admin"));

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage(), "Admin");
		}

		return adminsList;
	}

}

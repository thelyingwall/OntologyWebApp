package com.ontology.web.rest.services;

import org.springframework.stereotype.Service;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class OntologyService {

    private final Model model;

    private final String prefixRDF = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    private final String prefixOWL = "PREFIX owl:  <http://www.w3.org/2002/07/owl#>";
    private String baseUri;

    public OntologyService(Model model) {
        this.model = model;
    }

    public List<String> getClasses() {
        String queryStr = prefixRDF + prefixOWL + """
        SELECT ?class
        WHERE {
            ?class rdf:type owl:Class .
        }
        ORDER BY ?class
        """;

        Query query = QueryFactory.create(queryStr);

        List<String> classNames = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                String clsUri = sol.getResource("class").getURI();
                String localName = clsUri.substring(clsUri.lastIndexOf('#') + 1);
                classNames.add(localName);

                if (baseUri == null) {
                    baseUri = clsUri.substring(0, clsUri.indexOf('#'));
                }
            }
        }
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        collator.setStrength(Collator.PRIMARY);
        Collections.sort(classNames, collator);

        return classNames;
    }

    public List<String> getInstancesOfClass(String selectedClass) {
        List<String> instances = new ArrayList<>();
        String classUri = baseUri + "#" + selectedClass;

        String queryStr = prefixRDF + """
            SELECT ?instance
            WHERE {
                ?instance rdf:type <%s> .
            }
            ORDER BY ?instance
            """.formatted(classUri);

        Query query = QueryFactory.create(queryStr);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                String instanceUri = sol.getResource("instance").getURI();
                String localName = instanceUri.substring(instanceUri.lastIndexOf('#') + 1);
                instances.add(localName);
            }
        }
        if (instances.isEmpty())
            instances.add("Brak wyników dla klasy: " + selectedClass);

        return instances;
    }
}

package com.ontology.web.rest.controllers;

import com.ontology.web.rest.services.OntologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ontology")
public class OntologyResource {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyResource.class);
    private final OntologyService ontologyService;

    public OntologyResource(OntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    @GetMapping("/classlist")
    public List<String> getClassList() {
        return ontologyService.getClasses();
    }

    @GetMapping("/instances/{className}")
    public List<String> getInstancesByClass(@PathVariable("className") String className) {
        return ontologyService.getInstancesOfClass(className);
    }
}

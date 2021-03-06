# JOPA Examples

This repository contains some usage examples of the [JOPA framework](https://github.com/kbss-cvut/jopa).

### Examples

1. **Example01** - JOPA with a Sesame in-memory storage, working with object model generated by OWL2Java.
2. **Example02** - JOPA using OWLAPI-accessed files as storage.
3. **Example03** - JOPA with a native Sesame storage utilizing its contexts to store and access data in different RDF named graphs.
4. **Example04** - Full-blown Java web application with Spring, REST services, ReactJS-based UI and JOPA with Sesame native storage. 
Declarative transactions included.
5. **Example05** - JOPA utilizing [OWL2Query](https://kbss.felk.cvut.cz/web/portal/owl2query) - a SPARQL-DL query engine for OWLAPI.
6. **eswc2016** - JOPA demo for the ESWC 2016 conference. Running version can be found at 
[http://onto.fel.cvut.cz/eswc2016/](http://onto.fel.cvut.cz/eswc2016/).
7. **Example06** - JOPA showcasing support for mapped superclasses and plain identifier object property values.
8. **JOPA JSON-LD** - JOPA with [JB4JSON-LD](https://github.com/kbss-cvut/jb4jsonld) for Jackson and Spring - 
serialization/deserialization of POJOs into/from JSON-LD. Also showcases declarative transaction support for JOPA and Spring.
9. **Example07** - JOPA demonstrating support for single inheritance and plural data properties.

Each example has its own README file with a more detailed description. Note that JOPA requires Java 8.

#### Additional Example

An example of a more complex application using JOPA as its persistence provider can be found at 
[https://github.com/kbss-cvut/reporting-tool](https://github.com/kbss-cvut/reporting-tool).

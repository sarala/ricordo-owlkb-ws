package ricordo.owlkb.rest.service;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 14/03/12
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public interface QueryConstructorService {
    public OWLClassExpression runManchesterQuery(String manchesterQuery);

    public void addAxioms(Set<OWLAxiom> axiomSet);

    public void deleteAxioms(Set<OWLAxiom> owlAxiomSet);
}

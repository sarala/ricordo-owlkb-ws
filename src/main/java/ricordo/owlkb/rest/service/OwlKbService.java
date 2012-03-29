package ricordo.owlkb.rest.service;

import ricordo.owlkb.rest.bean.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 13/03/12
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */
public interface OwlKbService {
    public ArrayList<Term> getSubTerms(String query);
    public ArrayList<Term> getEquivalentTerms(String query);
    public ArrayList<Term> getTerms(String query);
    public ArrayList<Term> addTerm(String query);
    public ArrayList<Term> deleteTerm(String query);
}

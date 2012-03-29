package ricordo.owlkb.rest.service;

import ricordo.owlkb.rest.bean.Term;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 13/03/12
 * Time: 21:39
 */
public interface OwlKbService {
    public ArrayList<Term> getSubTerms(String query);
    public ArrayList<Term> getEquivalentTerms(String query);
    public ArrayList<Term> getTerms(String query);
    public ArrayList<Term> addTerm(String query);
    public ArrayList<Term> deleteTerm(String query);
}

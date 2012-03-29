package ricordo.owlkb.rest.service;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.*;
import org.semanticweb.owlapi.owllink.builtin.requests.*;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.owllink.retraction.RetractRequest;
import org.semanticweb.owlapi.owllink.server.OWLlinkServer;
import org.semanticweb.owlapi.owllink.server.serverfactory.PelletServerFactory;
import org.semanticweb.owlapi.reasoner.Node;
import ricordo.owlkb.rest.bean.Term;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 02/03/12
 * Time: 14:22
 */
public class OwlKbServiceImpl implements OwlKbService {
    private IRI docIRI;
    private IRI kbIRI;
    private String kbNs;
    private OWLlinkReasoner reasoner =null;
    private OWLOntologyManager owlOntologyManager = null;
    private OWLlinkServer server = null;

    private QueryConstructorService queryConstructorService;

    public OwlKbServiceImpl(String serverUrl, String serverPort, String kbNs, IRI docIRI, OWLOntologyManager owlOntologyManager,QueryConstructorService queryConstructorService){
        this.queryConstructorService = queryConstructorService;
        this.kbNs = kbNs;
        this.owlOntologyManager = owlOntologyManager;
        this.docIRI = docIRI;
        kbIRI = org.semanticweb.owlapi.model.IRI.create(kbNs);
        startPelletServer(serverPort);
        setUpReasoner(serverUrl, serverPort);
        createKB();
    }

    /**
     * start pellet server
     * @param serverPort
     */
    private void startPelletServer(String serverPort){
        PelletServerFactory pellet = new PelletServerFactory();
        server = pellet.createServer(Integer.parseInt(serverPort));
        server.run();
    }

    /**
     * setting up reasoner
     * @param serverUrl
     * @param serverPort
     */
    private void setUpReasoner(String serverUrl, String serverPort){
        OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
        try{
            OWLlinkReasonerConfiguration reasonerConfiguration = new OWLlinkReasonerConfiguration(new URL(serverUrl+":"+serverPort));
            reasoner = factory.createNonBufferingReasoner(owlOntologyManager.createOntology(), reasonerConfiguration);
        }catch (OWLlinkReasonerIOException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

/*    public void stopPelletServer(){
        if(server!=null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    */

    /**
     *  Creating the knowledgebase and loading the ontologies from the owl document
     */
    private void createKB(){
        try {
            CreateKB createKBRequest = new CreateKB(kbIRI);
            KB kbResponse = reasoner.answer(createKBRequest);
            reasoner.answer(new LoadOntologies(kbResponse.getKB(),docIRI));
        } catch (OWLlinkErrorResponseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls to the knowledge base
     * @param request
     * @return response
     */
    private Response executeReasoner(Request request){
        Response response = null;
        try{
            response = reasoner.answer(request);
        }catch(OWLlinkReasonerIOException e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Queries for all subclasses
     * @param query following Manchester Query Syntax
     * @return list of terms
     */
    public ArrayList<Term> getSubTerms(String query) {
        OWLClassExpression exp = queryConstructorService.runManchesterQuery(query);
        return getSubTerms(exp);
    }

    /**
     * Queries for all subclasses
     * @param exp Owl class expression constructed from the manchester query
     * @return list of terms
     */
    private ArrayList<Term> getSubTerms(OWLClassExpression exp) {
        ArrayList<Term> idList = new ArrayList<Term>();
        if(exp!=null){
            GetSubClasses getSubClasses = new GetSubClasses(kbIRI, exp);
            SetOfClassSynsets synsets = (SetOfClassSynsets)executeReasoner(getSubClasses);
            for (Object synset : synsets) {
                Node<OWLClass> owlClassNode = (Node<OWLClass>) synset;
                idList.add(new Term(owlClassNode.getEntities().iterator().next().toStringID()));
            }
        }
        return idList;
    }


    /**
     * Queries for all equivalent classes
     * @param query following Manchester Query Syntax
     * @return list of terms
     */
    public ArrayList<Term> getEquivalentTerms(String query){
        OWLClassExpression exp = queryConstructorService.runManchesterQuery(query);
        return getEquivalentTerms(exp);
    }

    /**
     * Queries for all equivalent
     * @param exp Owl class expression constructed from the manchester query
     * @return list of terms
     */
    private ArrayList<Term> getEquivalentTerms(OWLClassExpression exp){
        ArrayList<Term> idList = new ArrayList<Term>();
        if(exp!=null){
            GetEquivalentClasses getEquivalentClasses = new GetEquivalentClasses(kbIRI,exp);
            SetOfClasses eqclasses = (SetOfClasses)executeReasoner(getEquivalentClasses);
            for (Object eqclass : eqclasses) {
                OWLClass eqClass = (OWLClass) eqclass;
                idList.add(new Term(eqClass.toStringID()));
            }
        }
        return idList;
    }

    /**
     * Queries for all subclasses and equivalent classes
     * @param query following Manchester Query Syntax
     * @return list of terms
     */
    public ArrayList<Term> getTerms(String query) {
        ArrayList<Term> idList = new ArrayList<Term>();
        OWLClassExpression exp = queryConstructorService.runManchesterQuery(query);
        idList.addAll(getEquivalentTerms(exp));
        idList.addAll(getSubTerms(exp));
        return idList;
    }

    /**
     * Add a new term into the knowledge base using Manchester Query Syntax if the term does not exist
     * @param query following Manchester Query Syntax
     * @return
     */
    public ArrayList<Term> addTerm(String query) {
        OWLClassExpression exp = queryConstructorService.runManchesterQuery(query);
        ArrayList<Term> idList = getEquivalentTerms(exp);
        if(idList.isEmpty()){
            String ricordoid = String.valueOf(System.currentTimeMillis());
            OWLClass newowlclass = owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(kbNs+"#RICORDO_"+ricordoid));

            OWLAxiom axiom = owlOntologyManager.getOWLDataFactory().getOWLEquivalentClassesAxiom(newowlclass, exp);
            Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
            axiomSet.add(axiom);
            Tell tellRequest = new Tell(kbIRI,axiomSet);
            OK okResponse = (OK)executeReasoner(tellRequest);//reasoner.answer(tellRequest);

            //add to owlfile
            queryConstructorService.addAxioms(axiomSet);

            idList.add(new Term(newowlclass.toStringID()));
        }
        
        return idList;
    }

    /**
     * Delete axioms from the knowledge base using Manchester Query Syntax
     * @param query following Manchester Query Syntax
     * @return
     */

    public ArrayList<Term> deleteTerm(String query) {
        OWLClassExpression exp = queryConstructorService.runManchesterQuery(query);
        ArrayList<Term> idList = getEquivalentTerms(exp);

        if(!exp.isAnonymous()){
            OWLClass owlClass = exp.asOWLClass();

            Set<OWLClassAxiom> owlClassAxiomSet = owlOntologyManager.getOntology(kbIRI).getAxioms(owlClass);
            Set<OWLAxiom> owlAxiomSet = new HashSet<OWLAxiom>(owlClassAxiomSet.size());

            for (OWLClassAxiom anOwlClassAxiomSet : owlClassAxiomSet) {
                owlAxiomSet.add(anOwlClassAxiomSet);
            }
            RetractRequest retractRequest = new RetractRequest(kbIRI, owlAxiomSet);
            OK okResponse = (OK)executeReasoner(retractRequest);

            queryConstructorService.deleteAxioms(owlAxiomSet);
            idList.clear();
        }
        return idList;
    }
}

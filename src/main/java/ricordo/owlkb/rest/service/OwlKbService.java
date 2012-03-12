package ricordo.owlkb.rest.service;


import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.*;
import org.semanticweb.owlapi.owllink.builtin.requests.*;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.owllink.server.OWLlinkServer;
import org.semanticweb.owlapi.owllink.server.serverfactory.PelletServerFactory;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import ricordo.owlkb.rest.bean.Term;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 02/03/12
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class OwlKbService implements InitializingBean, DisposableBean {
    private IRI docIRI;
    private String kbNs;
    private String serverPort;
    private String serverUrl;

    private OWLlinkReasoner reasoner =null;
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLOntology queryOntology =null;
    private OWLlinkServer server = null;

    public OwlKbService(){
    }

    private void setUpReasoner(){
        OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
        try{
            OWLlinkReasonerConfiguration reasonerConfiguration = new OWLlinkReasonerConfiguration(new URL(serverUrl+":"+serverPort));
            reasoner = factory.createNonBufferingReasoner(manager.createOntology(), reasonerConfiguration);
        }catch (OWLlinkReasonerIOException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    private void setUpManQuery(){
        try {
            queryOntology = manager.loadOntology(docIRI);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }    
    }

    public void startPelletServer(){
        PelletServerFactory pellet = new PelletServerFactory();
        server = pellet.createServer(Integer.parseInt(serverPort));
        server.run();
    }

    public void stopPelletServer(){
        if(server!=null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createKB(){
        try {
            CreateKB createKBRequest = new CreateKB(org.semanticweb.owlapi.model.IRI.create(kbNs));
            KB kbResponse = reasoner.answer(createKBRequest);
            reasoner.answer(new LoadOntologies(kbResponse.getKB(),docIRI));
        } catch (OWLlinkErrorResponseException e) {
            e.printStackTrace();
        }
    }

    private Response executeReasoner(Request request){
        Response response = null;
        try{
            response = reasoner.answer(request);
        }catch(OWLlinkReasonerIOException e){
            e.printStackTrace();
        }
        return response;
    }

    private OWLClassExpression runManchesterQuery(String manchesterQuery){
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(manager.getOWLDataFactory(), manchesterQuery);
        parser.setDefaultOntology(queryOntology);
        Set<OWLOntology> importsClosure = queryOntology.getImportsClosure();
        BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, importsClosure, new SimpleShortFormProvider());
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);

        OWLClassExpression classExp=null;
        try {
            classExp = parser.parseClassExpression();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        return classExp;
    }

    public ArrayList<Term> getSubTerms(String query) {
        OWLClassExpression exp = runManchesterQuery(query);
        ArrayList<Term> idList = new ArrayList<Term>();
        if(exp!=null){
            GetSubClasses getSubClasses = new GetSubClasses(org.semanticweb.owlapi.model.IRI.create(kbNs), exp);
            SetOfClassSynsets synsets = (SetOfClassSynsets)executeReasoner(getSubClasses);
            for (Object synset : synsets) {
                Node<OWLClass> owlClassNode = (Node<OWLClass>) synset;
                idList.add(new Term(owlClassNode.getEntities().iterator().next().toStringID()));
            }
        }
        return idList;
    }


    public ArrayList<Term> getEquivalentTerms(String query){
        OWLClassExpression exp = runManchesterQuery(query);
        ArrayList<Term> idList = new ArrayList<Term>();
        if(exp!=null){
            GetEquivalentClasses getEquivalentClasses = new GetEquivalentClasses(org.semanticweb.owlapi.model.IRI.create(kbNs),exp);
            SetOfClasses eqclasses = (SetOfClasses)executeReasoner(getEquivalentClasses);
            for (Object eqclass : eqclasses) {
                OWLClass eqClass = (OWLClass) eqclass;
                idList.add(new Term(eqClass.toStringID()));
            }
        }
        return idList;
    }

    public ArrayList<Term> getTerms(String query) {
        ArrayList<Term> idList = new ArrayList<Term>();
        idList.addAll(getEquivalentTerms(query));
        idList.addAll(getSubTerms(query));
        return idList;
    }

    public ArrayList<Term> addTerm(String query) {
        ArrayList<Term> idList = getEquivalentTerms(query);
        if(idList.isEmpty()){
            String ricordoid = String.valueOf(System.currentTimeMillis());
            OWLClassExpression exp = runManchesterQuery(query);
            OWLClass newowlclass = manager.getOWLDataFactory().getOWLClass(IRI.create("http://www.ricordo.eu/ricordo.owl#RICORDO_"+ricordoid));
            OWLAxiom axiom = manager.getOWLDataFactory().getOWLEquivalentClassesAxiom(newowlclass, exp);
            Set<OWLAxiom> axiomSet = new HashSet<OWLAxiom>();
            axiomSet.add(axiom);
            Tell tellRequest = new Tell(docIRI,axiomSet);
            OK okResponse = (OK)executeReasoner(tellRequest);//reasoner.answer(tellRequest);

            //add to owlfile
            manager.addAxioms(queryOntology, axiomSet);
         //   writeToOWLFile();

            idList.add(new Term(newowlclass.toStringID()));
        }
        
        return idList;
    }

    private void writeToOWLFile(){
        try {
            manager.saveOntology(queryOntology, docIRI);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setDocIRI(String docIRI) {
        this.docIRI = org.semanticweb.owlapi.model.IRI.create(new File(docIRI));
    }

    public void setKbNs(String kbNs) {
        this.kbNs = kbNs;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void destroy() throws Exception {
        stopPelletServer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startPelletServer();
        setUpReasoner();
        setUpManQuery();
        createKB();
    }
}

package ricordo.owlkb.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import ricordo.owlkb.rest.bean.Term;
import ricordo.owlkb.rest.bean.TermList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 08/03/12
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration(locations={"client-context.xml"})
public class TermClient {

    private static String URL_STRING = "http://localhost:8080/service/";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGetTermsInJson() throws Exception {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"terms/{query}",
                HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class,
                "RICORDO_2 and part-of some RICORDO_21.json");
        assertEquals("{\"terms\":{\"count\":4,\"terms\":[{\"id\":\"http://www.ricordo.eu/ricordo.owl#RICORDO_25\"},{\"id\":\"http://www.ricordo.eu/ricordo.owl#RICORDO_23\"},{\"id\":\"http://www.ricordo.eu/ricordo.owl#RICORDO_24\"},{\"id\":\"http://www.w3.org/2002/07/owl#Nothing\"}]}}",response.getBody());

    }

    @Test
    public void testGetTermsInXML() throws Exception {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"terms/{query}",
                HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class,
                "RICORDO_2 and part-of some RICORDO_21.xml");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><terms><count>4</count><term><id>http://www.ricordo.eu/ricordo.owl#RICORDO_25</id></term><term><id>http://www.ricordo.eu/ricordo.owl#RICORDO_23</id></term><term><id>http://www.ricordo.eu/ricordo.owl#RICORDO_24</id></term><term><id>http://www.w3.org/2002/07/owl#Nothing</id></term></terms>",response.getBody());

    }

    @Test
    public void testGetTermsInAsTermObjects()throws Exception {
        ResponseEntity<TermList> response = restTemplate.exchange( URL_STRING+"terms/{query}",
                HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), TermList.class,
                "RICORDO_2 and part-of some RICORDO_21");

        TermList termList = response.getBody();
        assertEquals(4,termList.getCount());
    }

    @Test
    public void testEquivalentTerms() {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"eqterms/{query}",
                HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class,
                "RICORDO_2 and part-of some RICORDO_21");

        assertEquals("{\"terms\":{\"count\":1,\"terms\":[{\"id\":\"http://www.ricordo.eu/ricordo.owl#RICORDO_25\"}]}}",response.getBody());
    }

    @Test
    public void testPostQuery() {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"addterm/{query}",
                HttpMethod.POST, new HttpEntity<String>(new HttpHeaders()), String.class,
                "RICORDO_2 and part-of some RICORDO_21");

        assertEquals("{\"terms\":{\"count\":1,\"terms\":[{\"id\":\"http://www.ricordo.eu/ricordo.owl#RICORDO_25\"}]}}",response.getBody());
    }

    @Test
    public void testDeleteTerm() {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"deleteterm/{query}",
                HttpMethod.DELETE, new HttpEntity<String>(new HttpHeaders()), String.class,
                "RICORDO_1332909646667");

        assertEquals("{\"terms\":{\"count\":0,\"terms\":[]}}",response.getBody());
    }

}

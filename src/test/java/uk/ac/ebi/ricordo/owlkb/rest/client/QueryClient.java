package uk.ac.ebi.ricordo.owlkb.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 01/07/12
 * Time: 20:53
 */
@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration(locations={"client-context.xml"})
public class QueryClient {

    private static String URL_STRING = "http://localhost:8080/service/";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGetQueriesInJson() throws Exception {
        ResponseEntity response = restTemplate.exchange( URL_STRING+"queries",
                HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
        assertEquals("{\"queries\":{\"count\":7,\"queries\":[\"term\",\"relation some term\",\"relation some term and relation some term\",\"relation some term or relation some term\",\"term and relation some term\",\"term and relation some ( relation some term )\",\"\"]}}",response.getBody());

    }
}

package ricordo.owlkb.rest.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 08/03/12
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class TermClient {

    private static String URL_STRING = "http://localhost:8080/";

    public static void main(String[] args) {
        RestTemplate restTemplate = getTemplate();

        getTerms(restTemplate);
        postQuery(restTemplate);
    }

    public static void getTerms(RestTemplate rest) {
        HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

        ResponseEntity response = rest.exchange( URL_STRING+
                "service/terms/{query}",
                HttpMethod.GET, entity, String.class, "RICORDO_2 and part-of some RICORDO_21");

        System.out.println(response.getBody());
    }

    private static void postQuery(RestTemplate restTemplate) {
        HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

        ResponseEntity response = restTemplate.exchange( URL_STRING+
                "service/addterm/{query}",
                HttpMethod.POST, entity, String.class, "RICORDO_2 and part-of some RICORDO_21");

        System.out.println(response.getBody());
    }

    private static RestTemplate getTemplate() {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(
                "src/main/java/ricordo/owlkb/rest/client/client-context.xml");
        RestTemplate template = (RestTemplate) ctx.getBean("restTemplateClient");
        return template;
    }

    private static HttpEntity<String> prepareGet(MediaType type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }
}

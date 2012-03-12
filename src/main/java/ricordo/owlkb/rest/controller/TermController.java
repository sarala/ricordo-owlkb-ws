package ricordo.owlkb.rest.controller;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ricordo.owlkb.rest.bean.Term;
import ricordo.owlkb.rest.bean.TermList;
import ricordo.owlkb.rest.service.OwlKbService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 05/03/12
 * Time: 13:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TermController {
    private OwlKbService owlKbService;
    private Jaxb2Marshaller jaxb2Mashaller;

    public void setOwlKbService(OwlKbService service) {
        this.owlKbService = service;
    }

    public void setJaxb2Mashaller(Jaxb2Marshaller jaxb2Mashaller) {
        this.jaxb2Mashaller = jaxb2Mashaller;
    }

    private static final String XML_VIEW_NAME = "terms";

 /*   @RequestMapping(method= RequestMethod.GET, value="/employee/{id}")
    public ModelAndView getEmployee(@PathVariable String id) {
        Term e = employeeDS.get(Long.parseLong(id));
        return new ModelAndView(XML_VIEW_NAME, "object", e);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/employee/{id}")
    public ModelAndView updateEmployee(@RequestBody String body) {
        Source source = new StreamSource(new StringReader(body));
        Employee e = (Employee) jaxb2Mashaller.unmarshal(source);
        employeeDS.update(e);
        return new ModelAndView(XML_VIEW_NAME, "object", e);
    }

    @RequestMapping(method=RequestMethod.POST, value="/employee")
    public ModelAndView addEmployee(@RequestBody String body) {
        Source source = new StreamSource(new StringReader(body));
        Employee e = (Employee) jaxb2Mashaller.unmarshal(source);
        employeeDS.add(e);
        return new ModelAndView(XML_VIEW_NAME, "object", e);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/employee/{id}")
    public ModelAndView removeEmployee(@PathVariable String id) {
        employeeDS.remove(Long.parseLong(id));
        List<Employee> employees = employeeDS.getAll();
        EmployeeList list = new EmployeeList(employees);
        return new ModelAndView(XML_VIEW_NAME, "employees", list);
    }
*/
    @RequestMapping(method=RequestMethod.GET, value="/terms/{query}")
    public ModelAndView getTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/subterms/{query}")
    public ModelAndView getSubTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getSubTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/eqterms/{query}")
    public ModelAndView getEqTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getEquivalentTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.POST, value="/addterm/{query}")
    public ModelAndView addTerm(@PathVariable String query) {
        System.out.println("addTerm method: "+query);
        List<Term> terms = owlKbService.addTerm(query);
        TermList list = new TermList(terms);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

}

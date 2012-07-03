
/*
 * Copyright 2012 EMBL-EBI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.ricordo.owlkb.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.ricordo.owlkb.bean.Term;
import uk.ac.ebi.ricordo.owlkb.bean.TermList;
import uk.ac.ebi.ricordo.owlkb.service.OwlKbService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 05/03/12
 * Time: 13:07
 */

@Controller
public class TermController {
    private OwlKbService owlKbService;

    private static final String XML_VIEW_NAME = "terms";

    @RequestMapping(method=RequestMethod.GET, value="/terms/{query}")
    public ModelAndView getTerms(@PathVariable String query) {
        List<Term> termList = owlKbService.getTerms(query);
        TermList list = new TermList(termList);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/subterms/{query}")
    public ModelAndView getSubTerms(@PathVariable String query) {
        List<Term> termList = owlKbService.getSubTerms(query);
        TermList list = new TermList(termList);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/eqterms/{query}")
    public ModelAndView getEqTerms(@PathVariable String query) {
        List<Term> termList = owlKbService.getEquivalentTerms(query);
        TermList list = new TermList(termList);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.POST, value="/addterm/{query}")
    public ModelAndView addTerm(@PathVariable String query) {
        List<Term> terms = owlKbService.addTerm(query);
        TermList list = new TermList(terms);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/deleteterm/{query}")
    public ModelAndView removeEmployee(@PathVariable String query) {
        List<Term> terms = owlKbService.deleteTerm(query);
        TermList list = new TermList(terms);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    public void setOwlKbService(OwlKbService service) {
        this.owlKbService = service;
    }
}

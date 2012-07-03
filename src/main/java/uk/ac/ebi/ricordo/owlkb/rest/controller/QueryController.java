
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.ricordo.owlkb.bean.Query;
import uk.ac.ebi.ricordo.owlkb.bean.QueryList;
import uk.ac.ebi.ricordo.owlkb.service.QueryTemplateService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 28/06/12
 * Time: 17:22
 */
@Controller
public class QueryController {

    private QueryTemplateService queryTemplateService;

    private static final String XML_VIEW_NAME = "queries";

    @RequestMapping(method= RequestMethod.GET, value="/queries")
    public ModelAndView getQueryTemplates() {
        List<Query> queryList = queryTemplateService.getQueryTemplateList();
        QueryList list = new QueryList(queryList);
        return new ModelAndView(XML_VIEW_NAME, "queries", list);
    }

    public void setQueryTemplateService(QueryTemplateService queryTemplateService) {
        this.queryTemplateService = queryTemplateService;
    }
}

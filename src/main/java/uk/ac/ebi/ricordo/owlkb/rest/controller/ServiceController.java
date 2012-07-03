
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
import uk.ac.ebi.ricordo.owlkb.service.OwlKbService;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 28/06/12
 * Time: 17:22
 */
@Controller
public class ServiceController {

    private OwlKbService owlKbService;

    private static final String XML_VIEW_NAME = "servicestate";

    @RequestMapping(method= RequestMethod.GET, value="/startService")
    public ModelAndView startService() {
        owlKbService.startService();
        String successMessage="Service started";
        return new ModelAndView(XML_VIEW_NAME, "message", successMessage);
    }

    @RequestMapping(method= RequestMethod.GET, value="/stopService")
    public ModelAndView stopService() {
        owlKbService.stopService();
        String successMessage="Service stopped";
        return new ModelAndView(XML_VIEW_NAME, "message", successMessage);
    }

    public void setOwlKbService(OwlKbService owlKbService) {
        this.owlKbService = owlKbService;
    }
}

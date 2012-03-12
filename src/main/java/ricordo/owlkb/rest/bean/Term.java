package ricordo.owlkb.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 02/03/12
 * Time: 04:34
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class Term {
    private String id;

    public Term() {}

    public Term(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

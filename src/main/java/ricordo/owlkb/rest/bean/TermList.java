package ricordo.owlkb.rest.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sarala
 * Date: 02/03/12
 * Time: 04:36
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name="terms")
public class TermList {

    private int count;
    private List<Term> terms;

    public TermList() {}

    public TermList(List<Term> terms) {
        this.terms = terms;
        this.count = terms.size();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @XmlElement(name="term")
    public List<Term> getTerms() {
        return terms;
    }
    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

}

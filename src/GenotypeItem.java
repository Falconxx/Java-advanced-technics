import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlRootElement(name="genotypeitem")
public class GenotypeItem {

    private int genotype;
    private String cl;

    @XmlElement
    public void setGenotype(int genotype)
    {
        this.genotype = genotype;
    }

    public int getGenotype()
    {
        return genotype;
    }

    @XmlElement
    public void setCl(String cl)
    {
        this.cl = cl;
    }

    public String getCl()
    {
        return cl;
    }

    @Override
    public String toString() {
        return genotype + " " + cl;
    }
}

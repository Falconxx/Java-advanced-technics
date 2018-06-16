import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "genotypeitems")
public class GenotypeItems {

    private List<GenotypeItem> genotypeItems;

    public List<GenotypeItem> getGenotypeItems() {
        return genotypeItems;
    }

    @XmlElement(name = "genotypeitem")
    public void setGenotypeItems(List<GenotypeItem> genotypeItems) {
        this.genotypeItems = genotypeItems;
    }

    @Override
    public String toString() {
        return genotypeItems.toString();
    }

}
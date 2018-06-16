package application;

import static java.util.Arrays.asList;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
@XmlAccessorType(FIELD)
public class Data {

	private List<Przedmiot> przedmioty = new ArrayList<>(asList(new Przedmiot("dlugopis"),
																new Przedmiot("0 dlugopisow"),
																new Przedmiot("ogorek"),
																new Przedmiot("marchewka")));

	public List<Przedmiot> getPrzedmiots() {
		return przedmioty;
	}

	public void setPrzedmiots(List<Przedmiot> przedmioty) {
		this.przedmioty = przedmioty;
	}
}

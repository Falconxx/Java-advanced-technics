package application;

import static application.Subjects.ZAPOTRZEBOWANIE;
import static application.Subjects.ILOSC;
import static application.Subjects.CENA;
import static application.Subjects.ROZMIAR;
import static application.Subjects.PRIORYTET;

import java.util.HashMap;
import java.util.List;

public class SummingRow extends Przedmiot{

	public SummingRow(){
		super("Average");
	}
	
	public SummingRow(List<Przedmiot> przedmioty){
		super("Average");
		
		this.setGrades(new HashMap<String, Float>());
		
		this.getGrades().put(CENA.toString(), countAverageForSubject(CENA, przedmioty));
		this.getGrades().put(ZAPOTRZEBOWANIE.toString(), countAverageForSubject(ZAPOTRZEBOWANIE, przedmioty));
		this.getGrades().put(ILOSC.toString(), countAverageForSubject(ILOSC, przedmioty));
		this.getGrades().put(ROZMIAR.toString(), countAverageForSubject(ROZMIAR, przedmioty));
		this.getGrades().put(PRIORYTET.toString(), countAverageForSubject(PRIORYTET, przedmioty));
	}
	
	private float countAverageForSubject(Subjects subject, List<Przedmiot> przedmioty){
		
		return (float) przedmioty.stream()
								.mapToDouble(student -> student.getGrade(subject.toString()))
								.average()
								.getAsDouble();
	}
}

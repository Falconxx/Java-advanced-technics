package application;

import static application.Subjects.Need;
import static application.Subjects.count;
import static application.Subjects.price;
import static application.Subjects.size;
import static application.Subjects.image;

import java.util.HashMap;
import java.util.List;

public class SummingRow extends Przedmiot{

	public SummingRow(){
		super("Average");
	}
	
	public SummingRow(List<Przedmiot> przedmioty){
		super("Average");
		
		this.setGrades(new HashMap<String, Float>());

		this.getGrades().put(price.toString(), countAverageForSubject(price, przedmioty));
		this.getGrades().put(Need.toString(), countAverageForSubject(Need, przedmioty));
		this.getGrades().put(count.toString(), countAverageForSubject(count, przedmioty));
		this.getGrades().put(size.toString(), countAverageForSubject(size, przedmioty));
		this.getGrades().put(image.toString(), countAverageForSubject(image, przedmioty));
	}
	
	private float countAverageForSubject(Subjects subject, List<Przedmiot> przedmioty){
		
		return (float) przedmioty.stream()
								.mapToDouble(student -> student.getGrade(subject.toString()))
								.average()
								.getAsDouble();
	}
}

package application;

import static java.lang.Float.NaN;
import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Przedmiot {

	private String name;
	
	private Map<String, Float> grades = new HashMap<>();

	public Przedmiot(){}
	
	public Przedmiot(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Float> getGrades() {
		return grades;
	}
	public void setGrades(Map<String, Float> grades) {
		this.grades = grades;
	}
	
	public String getGradeAsString(String key){
		
		return getGrade(key).toString();
	}
	
	public Float getGrade(String key){
		
		return ofNullable(grades.get(key))
				.orElse(NaN);
	}
}

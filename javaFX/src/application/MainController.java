package application;

import static application.Subjects.Need;
import static application.Subjects.count;
import static application.Subjects.price;
import static application.Subjects.size;
import static application.Subjects.image;
import static java.lang.Float.parseFloat;
import java.lang.*;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

import java.io.File;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class MainController {

	private Data data = new Data();
	private SummingRow sumRow = new SummingRow();
	ResourceBundle bundle = ResourceBundle.getBundle("application.polish");

	@FXML
	private TableView<Przedmiot> przedmiotyTable;

	@FXML
	private Button button, newPrzedmiotButton;

	@FXML
	private TextField newPrzedmiotIndex;

	@FXML
	private TableColumn<Przedmiot, String> nameColumn, cenaColumn,  iloscColumn, iloscPercent, zapotrzebowanieColumn,
			rozmiarColumn,priorytetColumn;

	private ObservableList<Przedmiot> przedmioty = observableArrayList(data.getPrzedmiots());

	@FXML
	protected void initialize() {

		przedmioty.add(sumRow);
		przedmiotyTable.setItems(przedmioty);
		przedmiotyTable.setEditable(true);

		przedmiotyTable.setRowFactory(tv -> {
			PseudoClass lastLinePC = PseudoClass.getPseudoClass("last-line");
			TableRow<Przedmiot> row = new TableRow<>();
			row.indexProperty().addListener((obs, oldIndex, newIndex) -> {
				row.pseudoClassStateChanged(lastLinePC, newIndex.intValue() == przedmioty.size() - 1);
			});
			
			return row;
		});

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		configureColumn(cenaColumn, price);

		configureColumn(iloscColumn, count);

		configureColumn(zapotrzebowanieColumn, Need);

		configureColumn(rozmiarColumn, size);

		configureColumn(priorytetColumn, image);

		przedmiotyTable.refresh();
	}

	private void configurePercentColumn(TableColumn<Przedmiot, String> column, Subjects subject) {

		column.setCellFactory(forTableColumn());
		column.setCellValueFactory(percentColumnValueFactory(subject));
	}

	private Callback<CellDataFeatures<Przedmiot, String>, ObservableValue<String>> percentColumnValueFactory(
			Subjects subject) {

		return (CellDataFeatures<Przedmiot, String> param) -> countPrzedmiotPercentage(subject, param);
	}

	private ObservableValue<String> countPrzedmiotPercentage(Subjects subject, CellDataFeatures<Przedmiot, String> param) {

		float thisGrade = param.getValue().getGrade(subject.toString());
		System.out.println(iloscColumn.getText());
		int theSameOrLess = countPrzedmiotsWithLessOrTheSameGrade(subject, thisGrade);

		return new ReadOnlyStringWrapper(String.format("%.2f %%", 100.0 * theSameOrLess / data.getPrzedmiots().size()));
	}

	private int countPrzedmiotsWithLessOrTheSameGrade(Subjects subject, float thisPrzedmiotGrade) {

		return data.getPrzedmiots().stream().filter(student -> student.getGrade(subject.toString()) <= thisPrzedmiotGrade)
				.collect(toList()).size();
	}

	private void configureColumn(TableColumn<Przedmiot, String> column, Subjects subject) {

		column.setCellValueFactory(
				cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGradeAsString(subject.toString())));
		column.setCellFactory(forTableColumn());
	}

	@FXML
	protected void editGrade(CellEditEvent<Przedmiot, String> event) {

		int index = event.getTablePosition().getRow();
		String column = ((TableColumn) event.getSource()).getText();

		data.getPrzedmiots().get(index).getGrades().put(column, parseFloat(event.getNewValue()));

		przedmioty.remove(sumRow);
		sumRow = new SummingRow(data.getPrzedmiots());
		przedmioty.add(sumRow);

		przedmiotyTable.refresh();
	}

	@FXML
	protected void addNewPrzedmiot(ActionEvent event) {

		if ("".equals(newPrzedmiotIndex.getText())) {
			return;
		}

		Przedmiot newPrzedmiot = new Przedmiot(newPrzedmiotIndex.getText());

		data.getPrzedmiots().add(newPrzedmiot);

		przedmioty.remove(sumRow);
		przedmioty.add(newPrzedmiot);
		sumRow = new SummingRow(data.getPrzedmiots());
		przedmioty.add(sumRow);
	}
	@FXML
	protected void removePrzedmiot(ActionEvent event) {

		ObservableList<Przedmiot> przedmiotzaznaczony,wszystkieprzedmioty;
		wszystkieprzedmioty = przedmiotyTable.getItems();
		przedmiotzaznaczony= przedmiotyTable.getSelectionModel().getSelectedItems();
		przedmiotzaznaczony.forEach(wszystkieprzedmioty::remove);
		
		
	}
	@FXML
	protected void serializeData(ActionEvent event) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(data, new File("data.xml"));
	}

	@FXML
	protected void deserializeData(ActionEvent event) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		data = (Data) jaxbUnmarshaller.unmarshal(new File("data.xml"));

		przedmioty = observableArrayList(data.getPrzedmiots());

		przedmiotyTable.setItems(przedmioty);

		przedmiotyTable.refresh();
	}
}

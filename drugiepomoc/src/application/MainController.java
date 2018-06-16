package application;

import static application.Subjects.CENA;
import static application.Subjects.ILOSC;
import static application.Subjects.PRIORYTET;
import static application.Subjects.ROZMIAR;
import static application.Subjects.ZAPOTRZEBOWANIE;
import static java.lang.Float.parseFloat;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;
import application.MainView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.Locale;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
public class MainController {
	private ResourceBundle bundle;
	private Locale locale;
	private Data data = new Data();
	private SummingRow sumRow = new SummingRow();
	private AnchorPane rootLayout;
	int i=0;

	@FXML
	private TableView<Przedmiot> przedmiotyTable;

	@FXML
	private TableView<CustomImage> tableview = new TableView<CustomImage>();

	
	private void initialize(URL url, ResourceBundle rb){
		//TODO
	}
	@FXML
	private Button button, EN,ADD,DELETE,ZAPIS,ODCZYT,OBRAZ;
	


	@FXML
	private TextField newPrzedmiotIndex;

	@FXML
	private TableColumn<Przedmiot, String> nameColumn, cenaColumn,  iloscColumn, iloscPercent, zapotrzebowanieColumn,
			rozmiarColumn,priorytetColumn;

	private ObservableList<Przedmiot> przedmioty = observableArrayList(data.getPrzedmiots());
//	public ObservableList<CustomImage> imgList = FXCollections.observableArrayList();
//    CustomImage item_1 = new CustomImage(new ImageView(new Image("ogorek.png")));
//    CustomImage item_2 = new CustomImage(new ImageView(new Image("dlugopis.png")));
//    
//    imgList.addAll(item_1, item_2);



	@FXML
	protected void initialize() {

		przedmioty.add(sumRow);
		przedmiotyTable.setItems(przedmioty);
		przedmiotyTable.setEditable(true);
		przedmiotyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		przedmiotyTable.setRowFactory(tv -> {
			PseudoClass lastLinePC = PseudoClass.getPseudoClass("last-line");
			TableRow<Przedmiot> row = new TableRow<>();
			row.indexProperty().addListener((obs, oldIndex, newIndex) -> {
				row.pseudoClassStateChanged(lastLinePC, newIndex.intValue() == przedmioty.size() - 1);
			});
			
			return row;
		});

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		configureColumn(cenaColumn, CENA);

		configureColumn(iloscColumn, ILOSC);

		configureColumn(zapotrzebowanieColumn, ZAPOTRZEBOWANIE);

		configureColumn(rozmiarColumn, ROZMIAR);

		configureColumn(priorytetColumn, PRIORYTET);

		przedmiotyTable.refresh();
	}
    TableColumn albumArt = new TableColumn("Album Art");

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
		//System.out.println(iloscColumn.getText());
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
	protected void getImage(CellEditEvent<Przedmiot, String> event)
	{
		Przedmiot newPrzedmiot = new Przedmiot(newPrzedmiotIndex.getText());

		int index = event.getTablePosition().getRow();
		String column = ((TableColumn) event.getSource()).getText();
		System.out.println("hehehe");

	}
	
	@FXML
	protected void editGrade(CellEditEvent<Przedmiot, String> event) {

		int index = event.getTablePosition().getRow();
		String column = ((TableColumn) event.getSource()).getText();
		przedmiotyTable.getSelectionModel().setCellSelectionEnabled(true);
//		System.out.println(event.getTablePosition().getColumn());
//		System.out.println(event.getTablePosition().getRow());
//		System.out.println(event.getNewValue());

		data.getPrzedmiots().get(index).getGrades().put(column, parseFloat(event.getNewValue()));
		przedmioty.remove(sumRow);
		sumRow = new SummingRow(data.getPrzedmiots());
		przedmioty.add(sumRow);
		przedmiotyTable.refresh();
	}
	
//	@FXML
//	protected void editTekst(CellEditEvent<Przedmiot, String> event) {
//
//		int index = event.getTablePosition().getRow();
//		String column = ((TableColumn) event.getSource()).getText();
//		przedmiotyTable.getSelectionModel().setCellSelectionEnabled(true);
////		System.out.println(event.getTablePosition().getColumn());
////		System.out.println(event.getTablePosition().getRow());
////		System.out.println(event.getNewValue());
//
//
//		data.getPrzedmiots().get(index).getGrades().put(column, parseFloat(event.getNewValue()));
//		data.getPrzedmiots().add(e)
//		przedmioty.remove(sumRow);
//		sumRow = new SummingRow(data.getPrzedmiots());
//		przedmioty.add(sumRow);
//
//		przedmiotyTable.refresh();
//	}
	@FXML
	private void PL(ActionEvent event){
		loadlang("PL");
	}
	
	@FXML
	private void EN(ActionEvent event){
		loadlang("EN");
	}
	
	private void loadlang(String lang){
		
		MessageFormat messageForm = new MessageFormat("");
		locale = new Locale(lang);
		
		bundle = ResourceBundle.getBundle("application.lang",locale);
		double[] dlugopisy ={0,1,2};
		String[] fileStrings = { bundle.getString("0dlugopisow"),bundle.getString("1dlugopis"),bundle.getString("wieledlugopisow")};
		ChoiceFormat choiceform = new ChoiceFormat(dlugopisy,fileStrings);
		String pattern = bundle.getString("pattern");
		messageForm.setLocale(locale);
		messageForm.applyPattern(pattern);
		Object[] messageArguments = {null,"dlugopis",null};
		

		ADD.setText(bundle.getString("ADD"));
		DELETE.setText(bundle.getString("DELETE"));
		ZAPIS.setText(bundle.getString("ZAPIS"));
		ODCZYT.setText(bundle.getString("ODCZYT"));
		OBRAZ.setText(bundle.getString("OBRAZ"));
		nameColumn.setText(bundle.getString("nameColumn"));
		cenaColumn.setText(bundle.getString("cenaColumn"));
		iloscColumn.setText(bundle.getString("iloscColumn"));
		zapotrzebowanieColumn.setText(bundle.getString("zapotrzebowanieColumn"));
		rozmiarColumn.setText(bundle.getString("rozmiarColumn"));
		priorytetColumn.setText(bundle.getString("priorytetColumn"));
		
		
	//	String pattern= bundle.getString("pattern");
		//System.out.println(pattern);
		
		for(int i=0;i<przedmiotyTable.getItems().size();i++)
		{
			System.out.println(przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName());
			if(przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName().contains("nie")||przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName().contains("0")||(przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName().contains("no")))
				przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).setName(bundle.getString("0dlugopisow"));
			przedmiotyTable.refresh();

		}
			for(int i=0;i<przedmiotyTable.getItems().size();i++)
			{
				if(przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName().contains("1"))
				{
					messageArguments[0] = new Integer(1);
					messageArguments[2] = new Integer(1);
					przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).setName(messageForm.format(messageArguments));
				przedmiotyTable.refresh();
				}
				}
				for(int i=0;i<przedmiotyTable.getItems().size();i++)
				{
					if(przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).getName().contains("2,3,4,5,6,7,8,9"))
						przedmiotyTable.getSelectionModel().getTableView().getItems().get(i).setName(bundle.getString("wieledlugopisow"));
					przedmiotyTable.refresh();

				}
				}

	@FXML
	protected void addNewPrzedmiot(ActionEvent event) {

		if ("".equals(newPrzedmiotIndex.getText())) {
			return;
		}
		//System.out.println(przedmiotyTable.getColumns().size());
		//System.out.println(przedmiotyTable.getItems().size());
		Przedmiot newPrzedmiot = new Przedmiot(newPrzedmiotIndex.getText());
		data.getPrzedmiots().add(newPrzedmiot);
		//System.out.println(newPrzedmiotIndex.getText());
		//przedmiotyTable.setite

		przedmioty.remove(sumRow);
		przedmioty.add(newPrzedmiot);
		sumRow = new SummingRow(data.getPrzedmiots());
		przedmioty.add(sumRow);
	}
	
	
	@FXML
	protected void removePrzedmiot(ActionEvent event) {
		CellEditEvent<Przedmiot, String> event1;
		ObservableList<Przedmiot> przedmiotzaznaczony,wszystkieprzedmioty;
		wszystkieprzedmioty = przedmiotyTable.getItems();
		przedmiotzaznaczony= przedmiotyTable.getSelectionModel().getSelectedItems();
		System.out.println(przedmiotyTable.getSelectionModel().getSelectedItem().getName());
		przedmiotzaznaczony.forEach(wszystkieprzedmioty::remove);
		
		
	}
//	 Label secondLabel = new Label("obraz");
	 Stage stage;
	 
//	 @Override
//	 public void start(Stage stage) {
//		 this.stage = stage;
//		 this.stage.setTitle("Titelek");
//	}
	@FXML
	protected void viewImage(ActionEvent event) {
		 GridPane pane = new GridPane();
		 Image image = new Image("ogorek.jpg");
		 Image image2 = new Image("cos.jpg");
		 Image image3 = new Image("marchewka.jpg");

		 double w=500;
		 double s=500;
		 ImageView iv = new ImageView();
		 System.out.println(przedmiotyTable.getSelectionModel().getSelectedItem().getName());
		 if(przedmiotyTable.getSelectionModel().getSelectedItem().getName().equals("dlugopis"))
		 {
			 System.out.println("3");

			s= image2.getWidth();
			w=image2.getHeight();
			 iv.setImage(image2);
			pane.getChildren().add(iv);
		 }
		 if(przedmiotyTable.getSelectionModel().getSelectedItem().getName().equals("ogorek"))
		 {			 System.out.println("2");

				s= image.getWidth();
				w=image.getHeight();
			 iv.setImage(image);
			pane.getChildren().add(iv);
		 }
		 if(przedmiotyTable.getSelectionModel().getSelectedItem().getName().equals("marchewka"))
		 {
			 System.out.println("1");

				s= image3.getWidth();
				w=image3.getHeight();
			 iv.setImage(image3);
			pane.getChildren().add(iv);
		 }
		 
//		 pane.getChildren().add(secondLabel);
		
		 Scene scene = new Scene(pane,s,w);
			FXMLLoader loader = new FXMLLoader();

		 loader.setLocation(MainView.class.getResource("MainView.fxml"));
			try {
				rootLayout = (AnchorPane) loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         Stage stage = new Stage();
         stage.setTitle("My New Stage Title");
         stage.setScene(new Scene(rootLayout, s, w));
         stage.show();

		//System.out.println(przedmiotyTable.getSelectionModel().getSelectedItem().getName());
		
		stage.setScene(scene);
		stage.setTitle("obrazek");
		stage.show();
		
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

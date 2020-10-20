package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Tyontekijat extends Application {


	Tietokanta sql;
	int sisaan = -1;
	int tyontekijanid;
	int asukkaanid;
	String asukas;
	String kirjaaja;
	Label info;
	String tunnus;
	boolean naytalistaa = false;
	List<KirjauksetHenkilo> asukkaatlista;
	TableView kt2;
	String pvm2;
	TextArea kt1;


	@Override
	public void start(Stage ikkuna) throws SQLException{

		sql = new Tietokanta();
		sql.yhteydenAvaus();


		BorderPane kehys = new BorderPane();
		Scene scene = new Scene(kehys,1600,800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		kehys.getStyleClass().add("tausta");
		kehys.getStyleClass().add("fontti");
		ikkuna.setTitle("kirjautuminen hoitokodin tietokantaan");
		kehys.setPadding(new Insets(5, 10, 5, 10));
		ikkuna.setScene(scene);

		GridPane kirjautuminen = new GridPane();
		kehys.setCenter(kirjautuminen);
		kirjautuminen.setAlignment(Pos.CENTER);
		kirjautuminen.setVgap(10);
		kirjautuminen.setHgap(10);
		kirjautuminen.setPadding(new Insets(60, 10, 0, 10));
		kirjautuminen.setPrefSize(500, 500);

		Label l1 = new Label("käyttäjätunnus: ");
		TextField t1 = new TextField();
		Label l2 = new Label("salasana: ");
		PasswordField p1 = new PasswordField();
		CheckBox c1 = new CheckBox("näytä/piilota salasana");
		Button b1 = new Button("kirjaudu");
		Button b2 = new Button("salasana unohtunut");
		Label l3 = new Label(" ");

		c1.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				String salasana = p1.getText();
				if(newValue == true) {
					l3.setText(salasana);
				}if (newValue == false ){
					l3.setText(" ");
				}
			}
		});

		kirjautuminen.add(l1, 0, 0);
		kirjautuminen.add(t1, 1, 0);
		kirjautuminen.add(l2, 0, 1);
		kirjautuminen.add(p1, 1, 1);
		kirjautuminen.add(c1,0, 2);
		kirjautuminen.add(l3, 1, 2);
		kirjautuminen.add(b1, 0, 3);
		kirjautuminen.add(b2, 1, 3);




		BorderPane sisalla = new BorderPane();

		VBox asukkaat = new VBox();
		sisalla.setLeft(asukkaat);
		asukkaat.setSpacing(30);
		asukkaat.setPadding(new Insets(20, 10, 0, 10));

		Label val1 = new Label("kirjaaminen");
		val1.setFont(new Font("Text Font", 35));
		Label val3 = new Label("ASUKKAAT");
		Button vb1 = new Button("sulje ohjelma");


		ListView vlv1 = new ListView(); 
		ResultSet rs = sql.haeAsukkaat();
		ObservableList<String> lista = FXCollections.observableArrayList();
		while (rs.next()) {
			lista.add(rs.getString("nimi"));
		}
		vlv1.setItems(lista);
		vlv1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		asukkaat.getChildren().addAll(val1,val3,vlv1,vb1);


		asukkaatlista = new LinkedList<KirjauksetHenkilo>();
		kt2 = new TableView<KirjauksetHenkilo>();
		ObservableList<KirjauksetHenkilo> lista2 = FXCollections.observableArrayList();

		TableColumn <KirjauksetHenkilo, String> ktc1 = new TableColumn("kirjaaja");
		ktc1.setPrefWidth(150);
		ktc1.setCellValueFactory(new PropertyValueFactory<KirjauksetHenkilo, String>("kirjaaja"));
		TableColumn <KirjauksetHenkilo, String> ktc2 = new TableColumn("asukas");
		ktc2.setCellValueFactory(new PropertyValueFactory<KirjauksetHenkilo, String>("asukas"));
		ktc2.setPrefWidth(150);
		TableColumn <KirjauksetHenkilo, String> ktc3 = new TableColumn("pvm");
		ktc3.setCellValueFactory(new PropertyValueFactory<KirjauksetHenkilo, String>("pvm"));
		ktc3.setPrefWidth(100);
		TableColumn <KirjauksetHenkilo, String> ktc4 = new TableColumn("kello");
		ktc4.setCellValueFactory(new PropertyValueFactory<KirjauksetHenkilo, String>("kello"));
		ktc4.setPrefWidth(100);
		TableColumn <KirjauksetHenkilo, String> ktc5 = new TableColumn("kirjaus");
		ktc5.setPrefWidth(650);
		ktc5.setCellValueFactory(new PropertyValueFactory<KirjauksetHenkilo, String>("kirjaus"));
		kt2.getColumns().addAll(ktc1, ktc2, ktc3, ktc4, ktc5);
		kt2.setEditable(false);
		kt2.setMouseTransparent(true);
		kt2.setFocusTraversable(false);

		try {
			ResultSet rs2 = sql.haeKirjauksetA(asukas);
			while (rs2.next()) {
				lista2.add(new KirjauksetHenkilo(rs2.getString("kirjaaja"),rs2.getString("asukas"),rs2.getString("pvm"),rs2.getString("kello"),rs2.getString("kirjaus")));
			}
		}catch (Exception e) {
		};


		GridPane kirjaus = new GridPane();
		kirjaus.setAlignment(Pos.CENTER);
		kirjaus.setVgap(10);
		kirjaus.setHgap(10);
		kirjautuminen.setPadding(new Insets(60, 10, 0, 10));
		sisalla.setCenter(kirjaus);
		info = new Label("ASUKAS ei ole valittu");
		info.getStyleClass().add("fontti2");
		Button kb1 = new Button("asukkaan tiedot");

		DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime aika = LocalTime.now();
		String kello = aika.format(df);
		Label kl2 = new Label("PÄIVÄMÄÄRÄ");
		DatePicker kd1 = new DatePicker();
		kd1.setValue(LocalDate.now());
		kd1.setShowWeekNumbers(true);
		java.sql.Date date = java.sql.Date.valueOf(kd1.getValue());
		String pvm = date.toString();

		Label kl3 = new Label("KIRJAUS");
		kt1 = new TextArea();
		Button kb2 = new Button("TALLENNA");
		Label kl4 = new Label("KIRJATUT");
		DatePicker kd2 = new DatePicker();
		kd2.setValue(LocalDate.now());
		kd2.setShowWeekNumbers(true);

		kirjaus.setMargin(kd2, new Insets(0, 0, 0, - 100));

		kirjaus.add(info, 0, 0);
		kirjaus.add(kb1, 1, 0);
		kirjaus.add(kl2, 0, 1);
		kirjaus.add(kd1, 0, 2);
		kirjaus.add(kl3, 0, 3);
		kirjaus.add(kt1, 0, 4, 3,1);
		kirjaus.add(kb2, 0, 5);
		kirjaus.add(kl4, 0, 6);
		kirjaus.add(kd2, 1, 6);
		kirjaus.add(kt2, 0, 7, 6,1);



		b1.setOnAction((event) -> {
			sisaan = sql.tarkistaKirjautuja(t1.getText(), p1.getText());
			if(sisaan >= 0) {
				tunnus = t1.getText();
				kehys.setCenter(sisalla);
				ikkuna.setTitle("kirjaaminen");
				kirjaaja = sql.haeTyontekijaTunnuksella(tunnus);
				tyontekijanid = sql.haeTyontekijanId(kirjaaja);
			}else {
				l3.setText("Tarkista tunnus tai salasana");
			}
		});

		b2.setOnAction((event) -> salasananMuuttaminen());
		
		kb1.setOnAction((event) -> {
			asukkaanTiedot();
		});

		kb2.setOnAction((event) -> {
			naytalistaa = false;
			kt2.getItems().clear();
			asukkaatlista.clear();
			String teksti = kt1.getText();
			sql.kirjaaminen(pvm, kello, teksti, tyontekijanid, asukkaanid);
			taulukko();
		});

		kd2.setOnAction(event -> {
			java.sql.Date date2 = java.sql.Date.valueOf(kd2.getValue());
			pvm2 = date2.toString();
			naytalistaa = false;
			kt2.getItems().clear();
			asukkaatlista.clear();
			taulukkopvm();
		});

		vb1.setOnAction((event)->{
			sql.yhteydenSulkeminen();
			System.exit(0);
		});

		vlv1.setOnMouseClicked(e ->{
			asukas  = vlv1.getSelectionModel().getSelectedItem().toString();
			asukkaanid = sql.haeAsukkaanId(asukas);
			info.setText("ASUKAS " + asukas);
			kt2.setItems(lista2);
			info();
			naytalistaa = false;
			asukkaatlista.clear();
			kt2.getItems().clear();
			taulukko();
			kt1.setText("");
		});


		ikkuna.show();

	}

	public void taulukko() {

		ResultSet rs2 = sql.haeKirjauksetA(asukas);
		try{
			while(rs2.next()) {
				int tid = rs2.getInt("kirjaajan_id");
				String kirjaaja = sql.etsiTyontekijaid(tid);
				String pvm = rs2.getString("pvm");
				String kello = rs2.getString("kello");
				String kirjaus = rs2.getString("kirjaus");
				asukkaatlista.add(new KirjauksetHenkilo(kirjaaja,asukas,pvm,kello,kirjaus));
				naytalistaa = true;
			}
			if(naytalistaa) {
				for(KirjauksetHenkilo kh : asukkaatlista) {
					kt2.getItems().add(kh);
				}
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}

	}

	public void taulukkopvm() {

		ResultSet rs3 = sql.haeKirjauksetApvm(pvm2, asukas);
		try{
			while(rs3.next()) {
				int tid = rs3.getInt("kirjaajan_id");
				String kirjaaja = sql.etsiTyontekijaid(tid);
				String pvm = rs3.getString("pvm");
				String kello = rs3.getString("kello");
				String kirjaus = rs3.getString("kirjaus");
				asukkaatlista.add(new KirjauksetHenkilo(kirjaaja,asukas,pvm,kello,kirjaus));
				naytalistaa = true;
			}
			if(naytalistaa) {
				for(KirjauksetHenkilo kh : asukkaatlista) {
					kt2.getItems().add(kh);
				}
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}

	}

	public void salasananMuuttaminen() {

		Stage sikkuna = new Stage();
		sikkuna.initModality(Modality.WINDOW_MODAL);
		sikkuna.setTitle("salasanan muuttaminen");
		BorderPane smuuttaminen = new BorderPane(); 
		Scene scene2 = new Scene(smuuttaminen, 300, 250);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		smuuttaminen.getStyleClass().add("kehys");
		sikkuna.setScene(scene2);
		sikkuna.show();

		GridPane uusisalasana = new GridPane();
		uusisalasana.getStyleClass().add("fontti");
		smuuttaminen.setCenter(uusisalasana);
		uusisalasana.setAlignment(Pos.CENTER);
		uusisalasana.setVgap(10);
		uusisalasana.setHgap(10);
		uusisalasana.setPadding(new Insets(10, 10, 10, 10));

		Label ul1 = new Label("käyttäjätunnus: ");
		TextField ut1 = new TextField();
		Label ul2 = new Label("uusi salasana: ");
		TextField ut2 = new TextField();
		Button ub1 = new Button("tallenna uusi salasana ");
		Label ul3 = new Label(" ");



		uusisalasana.add(ul1, 0, 0);
		uusisalasana.add(ut1, 0, 1);
		uusisalasana.add(ul2, 0, 2);
		uusisalasana.add(ut2, 0, 3);
		uusisalasana.add(ub1, 0, 4);
		uusisalasana.add(ul3, 0, 5);

		ub1.setOnAction((event) -> {
			String ktunnus = ut1.getText();
			String usalasana = ut2.getText();
			sql.salasananMuuttaminen(ktunnus, usalasana);
			ul3.setText("Salasana vaihdettu. Kirjaudu sisään etusivulta");
		});


	}

	public void info() {

		if(!sql.haeInfo(asukas).isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("tärkeä info");
			alert.setHeaderText("Asukkaasta tärkeää infoa!");
			alert.setContentText(sql.haeInfo(asukas));
			alert.showAndWait();
		}
	}

	public void asukkaanTiedot() {
		
		Stage atikkuna = new Stage();
		atikkuna.initModality(Modality.WINDOW_MODAL);
		atikkuna.setTitle("tiedot: " + asukas);
		BorderPane atiedot = new BorderPane(); 
		Scene scene3 = new Scene(atiedot, 700, 200);
		scene3.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		atiedot.getStyleClass().add("kehys");
		atikkuna.setScene(scene3);
		atikkuna.show();

		GridPane gpat = new GridPane();
		gpat.getStyleClass().add("fontti");
		atiedot.setCenter(gpat);
		gpat.setAlignment(Pos.CENTER);
		gpat.setVgap(10);
		gpat.setHgap(10);
		gpat.setPadding(new Insets(10, 10, 10, 10));
		
		Label hetu = new Label();
		Label info2 = new Label();
		Label huoltaja = new Label();
		Label hpuh = new Label();
		Label hosoite = new Label();
		Label edunvalvoja = new Label();
		Label epuh = new Label();
		Label eosoite = new Label();
		Label lh = new Label("HUOLTAJAN TIEDOT:");
		Label le = new Label("EDUNVALVOJAN TIEDOT:");
		Label ln = new Label("NIMI");
		Label lp = new Label("PUHELINNUMERO");
		Label lo = new Label("OSOITE");
		
		ResultSet rs = sql.etsiAsukas(asukas);
		try{
			while(rs.next()) {
				hetu.setText("HENKILÖTURVATUNNUS: " + rs.getString("hetu"));
				info2.setText("TÄRKEÄ INFO: " + rs.getString("info"));
				huoltaja.setText(rs.getString("huoltaja"));
				hpuh.setText(rs.getString("huoltajan_puhelinnumero"));
				hosoite.setText(rs.getString("huoltajan_osoite"));
				edunvalvoja.setText(rs.getString("edunvalvoja"));
				epuh.setText(rs.getString("edunvalvojan_puhelinnumero"));
				eosoite.setText(rs.getString("edunvalvojan_osoite"));
			}} catch (Exception e1) {
				System.out.println(e1);
			}


		gpat.add(hetu, 0, 0,5,1);
		gpat.add(info2, 0, 1,5,1);
		gpat.add(ln, 1, 2);
		gpat.add(lp, 2, 2);
		gpat.add(lo, 3, 2);
		gpat.add(lh, 0, 3);
		gpat.add(huoltaja, 1, 3);
		gpat.add(hpuh, 2, 3);
		gpat.add(hosoite, 3, 3);
		gpat.add(le, 0, 4);
		gpat.add(edunvalvoja, 1, 4);
		gpat.add(epuh, 2, 4);
		gpat.add(eosoite, 3, 4);

	}

	public static void main(String[] args) {
		launch(args);
	}


}

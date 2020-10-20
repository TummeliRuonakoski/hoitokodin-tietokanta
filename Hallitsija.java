package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Hallitsija extends Application {

	//TOIMII
	//asukkaan lisääminen
	//asukkaan poistaminen
	//edunvalvojan tietojen vaihtaminen
	//huoltajan tietojen vaihtaminen
	//työntekijän poistaminen
	//työntekijän tietojen poistaminen
	//listat


	String ammatti;
	Tietokanta sql;
	String salasana = "";
	String asukas;
	String tyontekija;
	String info = "";
	boolean naytataulukkoa = false;
	boolean naytataulukkot = false;
	boolean naytalistaa = false;
	boolean naytalistat = false;
	TableView t2;
	ListView vlv1;
	ListView vlv2;
	Button hb1;
	Button hb2;
	Button hb3;
	Button hb4;
	Button hb5;
	Button hb6;
	Button hb7;
	Button hb8;
	List<KirjauksetHenkilo> asukkaat;
	List<KirjauksetHenkilo> tyontekijat;
	ObservableList<String> lista;
	ObservableList<String> lista2;

	@Override
	public void start(Stage ikkuna) throws SQLException {

		sql = new Tietokanta();
		sql.yhteydenAvaus();
		ikkuna.setTitle("hoitokodin tietokanta");
		BorderPane kehys = new BorderPane();
		Scene scene = new Scene(kehys,1600,800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		kehys.getStyleClass().add("tausta");

		kehys.setPadding(new Insets(10, 20, 20, 20));
		kehys.getStyleClass().add("fontti");

		GridPane g1 = new GridPane();
		kehys.setTop(g1);
		GridPane g2 = new GridPane();
		GridPane g3 = new GridPane();

		g1.setVgap(10);
		g1.setHgap(10);
		g1.setPadding(new Insets(5, 10, 5, 0));
		hb1 = new Button("asukkaan lisääminen");
		hb1.getStyleClass().add("button");
		hb2 = new Button("työntekijän lisääminen");
		g1.add(hb1, 0, 0);
		g1.add(hb2, 1, 0);
		
		g2.setVgap(10);
		g2.setHgap(10);
		g2.setPadding(new Insets(5, 10, 5, 0));
		hb3 = new Button("poista asukas");
		hb5 = new Button("päivitä edunvalvoja");
		hb6 = new Button("päivitä huoltaja");
		hb8 = new Button("muuta info");
		g2.add(hb3, 3, 0);
		g2.add(hb5, 4, 0);
		g2.add(hb6, 5, 0);
		g2.add(hb8, 6, 0);

		g3.setVgap(10);
		g3.setHgap(10);
		g3.setPadding(new Insets(5, 10, 5, 0));
		hb4 = new Button("poista työntekijä");
		hb7 = new Button("muuta tietoja");
		g3.add(hb4, 3, 0);
		g3.add(hb7, 4, 0);
		
		VBox v = new VBox();
		kehys.setLeft(v);
		v.setSpacing(15);
		v.setPadding(new Insets(20, 10, 0, 0));

		Button vb2 = new Button("poistu");


		Label vl1 = new Label("asukkaat:");
		vlv1 = new ListView(); 
		lista = FXCollections.observableArrayList();
		listaa();
		vlv1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		vlv1.setOnMouseClicked(e ->{
			asukas  = vlv1.getSelectionModel().getSelectedItem().toString();
			info();
			naytataulukkoa = false;
			asukkaat.clear();
			t2.getItems().clear();
			taulukkoa();
			kehys.setTop(g2);
		});

		Label vl2 = new Label("työntekijät:");
		vlv2 = new ListView();
		lista2 = FXCollections.observableArrayList();
		listat();
		v.getChildren().addAll(vl1,vlv1,vl2,vlv2,vb2);
		vlv2.setOnMouseClicked(e ->{
			tyontekija  = vlv2.getSelectionModel().getSelectedItem().toString();
			naytataulukkot = false;
			tyontekijat.clear();
			t2.getItems().clear();
			taulukkot();
			kehys.setTop(g3);
		});


		asukkaat = new LinkedList<KirjauksetHenkilo>();
		tyontekijat = new LinkedList<KirjauksetHenkilo>();
		t2 = new TableView<>();
		t2.getStyleClass().add("taulukko");
		
		TableColumn <String, KirjauksetHenkilo> tc1 = new TableColumn<>("Kirjaaja");
		tc1.setPrefWidth(150);
		tc1.setCellValueFactory(new PropertyValueFactory<>("kirjaaja"));
		TableColumn <String, KirjauksetHenkilo> tc2 = new TableColumn<>("Asukas");
		tc2.setPrefWidth(150);
		tc2.setCellValueFactory(new PropertyValueFactory<>("asukas"));
		TableColumn <String, KirjauksetHenkilo> tc3 = new TableColumn<>("Pvm");
		tc3.setPrefWidth(100);
		tc3.setCellValueFactory(new PropertyValueFactory<>("pvm"));
		TableColumn <String, KirjauksetHenkilo> tc4 = new TableColumn<>("Kello");
		tc4.setPrefWidth(100);
		tc4.setSortType(TableColumn.SortType.DESCENDING);
		tc4.setCellValueFactory(new PropertyValueFactory<>("kello"));
		TableColumn <String, KirjauksetHenkilo> tc5 = new TableColumn<>("Kirjaus");
		tc5.setPrefWidth(650);
		tc5.setSortType(TableColumn.SortType.DESCENDING);
		tc5.setCellValueFactory(new PropertyValueFactory<String, KirjauksetHenkilo>("kirjaus"));

		t2.getColumns().addAll(tc1, tc2, tc3, tc4, tc5);
		kehys.setCenter(t2);
		t2.setEditable(false);
		t2.setMouseTransparent(true);
		t2.setFocusTraversable(false);

		
		hb1.setOnAction((event)->{
			asukkaanLisaaminen();
		});

		hb2.setOnAction((event)->{
			tyontekijanLisaaminen();
		});

		hb3.setOnAction((event)->{
			asukkaanPoisto();
		});

		hb4.setOnAction((event)->{
			tyontekijanPoisto();
		});

		hb5.setOnAction((event)->{
			edunvalvojanMuuttaminen();
		});

		hb6.setOnAction((event)->{
			huoltajanMuuttaminen();
		});
		hb7.setOnAction((event)->{
			tyontekijanTietojenMuuttaminen();
		});

		hb8.setOnAction((event)->{
			muutainfo();
		});
		
		vb2.setOnAction((event)->{
			sql.yhteydenSulkeminen();
			System.exit(0);
		});

		ikkuna.setScene(scene);
		ikkuna.show();

	}

	public void listaa() {
		ResultSet rs = sql.haeAsukkaat();
		try{
			while (rs.next()) {
				lista.add(rs.getString("nimi"));
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}
		vlv1.setItems(lista);


	}

	public void listat() {
		ResultSet rs2 = sql.haeTyontekijat();
		try{
			while (rs2.next()) {
				lista2.add(rs2.getString("nimi"));
				naytalistat = true;
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}
		vlv2.setItems(lista2);
	}

	public void taulukkoa() {
		ResultSet rs4 = sql.haeKirjauksetA(asukas);
		try{
			while(rs4.next()) {
				int tid = rs4.getInt("kirjaajan_id");
				String kirjaaja = sql.etsiTyontekijaid(tid);
				String pvm2 = rs4.getString("pvm");
				String kello2 = rs4.getString("kello");
				String kirjaus2 = rs4.getString("kirjaus");
				asukkaat.add(new KirjauksetHenkilo(kirjaaja,asukas,pvm2,kello2,kirjaus2));
				naytataulukkot = true;
			}
			if(naytataulukkot) {
				for(KirjauksetHenkilo kh3 : asukkaat) {
					t2.getItems().add(kh3);
				}
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}
	}

	public void taulukkot() {
		ResultSet rs3 = sql.haeKirjauksetT(tyontekija);
		try{
			while(rs3.next()) {
				int aid = rs3.getInt("asukkaan_id");
				String kasukas = sql.etsiAsukasid(aid);
				String pvm2 = rs3.getString("pvm");
				String kello2 = rs3.getString("kello");
				String kirjaus2 = rs3.getString("kirjaus");
				tyontekijat.add(new KirjauksetHenkilo(tyontekija,kasukas,pvm2,kello2,kirjaus2));
				naytataulukkot = true;
			}
			if(naytataulukkot) {
				for(KirjauksetHenkilo kh2 : tyontekijat) {
					t2.getItems().add(kh2);
				}
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}
	}

	public void asukkaanLisaaminen() {
		Stage aikkuna = new Stage();
		aikkuna.initModality(Modality.WINDOW_MODAL);
		aikkuna.setTitle("asukkaan lisääminen");
		BorderPane alisays = new BorderPane();
		alisays.getStyleClass().add("fontti");
		alisays.getStyleClass().add("kehys");
		alisays.setPadding(new Insets(20, 10, 20, 10));
		Scene scene2 = new Scene(alisays, 700, 400);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		aikkuna.setScene(scene2);
		aikkuna.show();

		GridPane ag = new GridPane();
		ag.setPadding(new Insets(0, 10, 0, 0));
		ag.setVgap(10);
		ag.setHgap(10);
		alisays.setLeft(ag);
		Label gl1 = new Label("nimi:");
		TextField gt1 = new TextField();
		Label gl2 = new Label("hetu:");
		TextField gt2 = new TextField();
		Label gl3 = new Label("tärkeä info:");
		TextArea ga1 = new TextArea();
		double height = 200;
		double width = 80;
		ga1.setPrefHeight(height);
		ga1.setPrefWidth(width);
		Button gb1 = new Button("asukkaan tietojen tallentaminen");
		ag.setMargin(gb1, new Insets(11, 0, 0, 0));
		gb1.getStyleClass().add("button");

		ag.add(gl1, 0, 0);
		ag.add(gt1, 1, 0);
		ag.add(gl2, 0, 1);
		ag.add(gt2, 1, 1);
		ag.add(gl3, 0, 2);
		ag.add(ga1, 1, 2,2,1);
		ag.add(gb1, 0, 3, 2,1);

		GridPane ahg = new GridPane();
		ahg.setPadding(new Insets(0, 10, 0, 0));
		ahg.setVgap(10);
		ahg.setHgap(10);
		alisays.setRight(ahg);
		Label gahl1 = new Label("huoltajan nimi:");
		TextField gaht1 = new TextField();
		Label gahl2 = new Label("huoltajan osoite:");
		TextField gaht2 = new TextField();
		Label gahl3 = new Label("huoltajan puhelinnumero:");
		TextField gaht3 = new TextField();
		Label gahl4 = new Label("edunvalvoja:");
		TextField gaht4 = new TextField();
		Label gahl5 = new Label("edunvalvojan osoite:");
		TextField gaht5 = new TextField();
		Label gahl6 = new Label("edunvalvojan puhelinnumero:");
		TextField gaht6 = new TextField();

		ahg.add(gahl1, 0, 1);
		ahg.add(gaht1, 1, 1);
		ahg.add(gahl2, 0, 2);
		ahg.add(gaht2, 1, 2);
		ahg.add(gahl3, 0, 3);
		ahg.add(gaht3, 1, 3);
		ahg.add(gahl4, 0, 4);
		ahg.add(gaht4, 1, 4);
		ahg.add(gahl5, 0, 5);
		ahg.add(gaht5, 1, 5);
		ahg.add(gahl6, 0, 6);
		ahg.add(gaht6, 1, 6);

		gb1.setOnAction((event) -> {

			String asukasnimi = gt1.getText();
			String hetu = gt2.getText();
			String info = ga1.getText();
			String huoltaja = gaht1.getText();
			String huoltajanosoite = gaht2.getText();
			String huoltajanpuhelinnumero = gaht3.getText();
			String edunvalvoja = gaht4.getText();
			String edunvalvojanosoite = gaht5.getText();
			String edunvalvojanpuhelinnumero = gaht6.getText();
			String tallennaAsukas = sql.lisaaAsukas(asukasnimi, hetu, info, huoltaja, huoltajanpuhelinnumero, huoltajanosoite, edunvalvoja, edunvalvojanpuhelinnumero , edunvalvojanosoite);
			System.out.println("uusi asukas: " + tallennaAsukas);
			
			naytalistaa = false;
			lista.clear();
			listaa();
			
		});
	}

	public void tyontekijanLisaaminen() {
		Stage tikkuna = new Stage();
		tikkuna.initModality(Modality.WINDOW_MODAL);
		tikkuna.setTitle("työntekijän lisääminen");
		BorderPane tlisays = new BorderPane();
		tlisays.getStyleClass().add("fontti");
		tlisays.getStyleClass().add("kehys");
		tlisays.setPadding(new Insets(20, 10, 20, 10));
		Scene scene2 = new Scene(tlisays, 500, 300);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		tikkuna.setScene(scene2);
		tikkuna.show();


		GridPane tg = new GridPane();
		tlisays.setLeft(tg);
		tg.setPadding(new Insets(0, 10, 0, 0));
		tg.setVgap(10);
		tg.setHgap(10);
		Label gl1 = new Label("nimi:");
		TextField gt1 = new TextField();
		Label gl2 = new Label("hetu:");
		TextField gt2 = new TextField();
		Label gl3 = new Label("osoite:");
		TextField gt3 = new TextField();
		TextField gt4 = new TextField();
		Label gl4 = new Label("puhelinnumero:");
		TextField gt5 = new TextField();
		Button gb1 = new Button("lisää työntekijä");
		tg.setMargin(gb1, new Insets(11, 0, 0, 0));
		gb1.getStyleClass().add("button");

		tg.add(gl1, 0, 0);
		tg.add(gt1, 1, 0);
		tg.add(gl2, 0, 1);
		tg.add(gt2, 1, 1);
		tg.add(gl3, 0, 2);
		tg.add(gt3, 1, 2);
		tg.add(gt4, 1, 3);
		tg.add(gl4, 0, 4);
		tg.add(gt5, 1, 4);
		tg.add(gb1, 1, 5);

		VBox tv = new VBox();
		tlisays.setCenter(tv);
		tv.setSpacing(10);
		ToggleGroup vtg = new ToggleGroup();
		RadioButton rb1 = new RadioButton();
		rb1.setText("lähihoitaja");
		rb1.setToggleGroup(vtg);
		RadioButton rb2 = new RadioButton();
		rb2.setText("lääkäri");
		rb2.setToggleGroup(vtg);
		RadioButton rb3 = new RadioButton();
		rb3.setText("fysioterapeutti");
		rb3.setToggleGroup(vtg);
		RadioButton rb4 = new RadioButton();
		rb4.setText("puheterapeutti");
		rb4.setToggleGroup(vtg);
		RadioButton rb5 = new RadioButton();
		rb5.setText("sosiaalityöntekijä");
		rb5.setToggleGroup(vtg);
		RadioButton rb6 = new RadioButton();
		rb6.setText("vapaa-ajanohjaaja");
		rb6.setToggleGroup(vtg);
		RadioButton rb7 = new RadioButton();
		rb7.setText("muu");
		rb7.setToggleGroup(vtg);
		tv.getChildren().addAll(rb1,rb2,rb3,rb4,rb5,rb6,rb7);
		vtg.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
		{ 

			@Override
			public void changed(ObservableValue<? extends Toggle> ovt, Toggle old, Toggle newt) {

				if (vtg.getSelectedToggle() == rb1) {
					ammatti = "lähihoitaja";
				} else if (vtg.getSelectedToggle() == rb2) {
					ammatti = "lääkäri";
				} else if (vtg.getSelectedToggle() == rb3) {
					ammatti = "fysioterapeutti";
				}else if (vtg.getSelectedToggle() == rb4) {
					ammatti = "puheterapeutti";
				}else if (vtg.getSelectedToggle() == rb5) {
					ammatti = "sosiaalityöntekijä";
				}else if (vtg.getSelectedToggle() == rb6) {
					ammatti = "vapaa-ajanohjaaja";
				} else {
					ammatti = "muu";
				}
			}
		});

		gb1.setOnAction((event) -> {
			String nimi = gt1.getText();
			String hetu = gt2.getText();
			String osoite = (gt3.getText() + " " + gt4.getText());
			String puhelinnumero = gt5.getText();

			Random r1 = new Random();
			String nimi2 = gt1.getText().replaceAll(" ", "");
			nimi2 = nimi2.replaceAll("([-])", ""); 
			nimi2 = nimi2.toLowerCase();
			String kayttajatunnus = nimi2.substring(0,4) + r1.nextInt(999);
			salasananArpominen();

			String tallentaminen = sql.lisaaTyontekija(nimi, hetu, osoite, puhelinnumero, ammatti, kayttajatunnus, salasana);
			naytalistat = false;
			lista2.clear();
			listat();
			
			Stage tuikkuna = new Stage();
			tuikkuna.initModality(Modality.WINDOW_MODAL);
			tuikkuna.setTitle("työntekijän tunnukset");
			BorderPane tunnus = new BorderPane();
			tunnus.setPadding(new Insets(20, 10, 20, 10));
			Scene scene3 = new Scene(tunnus, 300, 200);
			scene3.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			tunnus.getStyleClass().add("fontti");
			tunnus.getStyleClass().add("kehys");
			VBox tuv = new VBox();
			tunnus.setCenter(tuv);
			tuv.setSpacing(15);
			tuv.setPadding(new Insets(20, 10, 0, 0));
			Label tuvl1 = new Label("Työntekijän tunnukset kirjautumista varten");
			Label tuvl2 = new Label("käyttäjätunnus: " + kayttajatunnus);
			Label tuvl3 = new Label("salasana: " + salasana);
			Label tuvl4 = new Label("Työntekijä muuttaa salasanan haluamaansa");
			tuv.getChildren().addAll(tuvl1,tuvl2,tuvl3, tuvl4);
			tikkuna.setScene(scene3);
			tikkuna.show();
		});
	}

	public void asukkaanPoisto() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("varmistus");
		alert.setHeaderText("Asukkaan poistaminen tietokannasta");
		alert.setContentText("Haluatko varmasti poistaa asukkaan tietokannasta?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			sql.poistaAsukas(asukas);
			naytalistaa = false;
			lista.clear();
			listaa();
		} else {
			alert.close();
		}

	}

	public void tyontekijanPoisto() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("varmistus");
		alert.setHeaderText("Työntekijän poistaminen tietokannasta");
		alert.setContentText("Haluatko varmasti poistaa työntekijän tietokannasta?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			sql.poistaTyontekija(tyontekija);
			naytalistat = false;
			lista2.clear();
			listat();
		} else {
			alert.close();
		}

	}

	public String salasananArpominen() {
		Random r = new Random();
		int n;
		char c = (char) ('a' + r.nextInt(26));
		for(int i = 0 ; i < 4 ; i++) {
			n = r.nextInt(10);
			salasana += n;
		}
		salasana += c;
		return salasana;


	}

	public void edunvalvojanMuuttaminen() {

		Stage sikkuna = new Stage();
		sikkuna.initModality(Modality.WINDOW_MODAL);
		sikkuna.setTitle("edunvalvojan tietojen muuttaminen");
		BorderPane be = new BorderPane(); 
		Scene scene2 = new Scene(be, 400, 250);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		be.getStyleClass().add("kehys");
		sikkuna.setScene(scene2);
		sikkuna.show();

		GridPane ge = new GridPane();
		ge.getStyleClass().add("fontti");
		be.setCenter(ge);
		ge.setAlignment(Pos.CENTER);
		ge.setVgap(10);
		ge.setHgap(10);
		ge.setPadding(new Insets(10, 10, 10, 10));

		Label el1 = new Label("asukkaan hetu: ");
		TextField et1 = new TextField();
		Label el2 = new Label("edunvalvoja: ");
		TextField et2 = new TextField();
		Label el3 = new Label("puhelinnumero: ");
		TextField et3 = new TextField();
		Label el4 = new Label("osoite: ");
		TextField et4 = new TextField();
		Button eb1 = new Button("tallenna tiedot ");
		Label el5 = new Label(" ");

		ge.add(el1, 0, 0);
		ge.add(et1, 1, 0);
		ge.add(el2, 0, 1);
		ge.add(et2, 1, 1);
		ge.add(el3, 0, 2);
		ge.add(et3, 1, 2);
		ge.add(el4, 0, 3);
		ge.add(et4, 1, 3);
		ge.add(eb1, 0, 4);
		ge.add(el5, 1, 4);

		eb1.setOnAction((event) -> {
			String hetu = et1.getText();
			String edunvalvoja = et2.getText();
			String puhelinnumero = et3.getText();
			String osoite = et4.getText();
			sql.edunvalvojanMuuttaminen(hetu, edunvalvoja, puhelinnumero, osoite);
			el5.setText("Edunvalvojan tiedot vaihdettu");
		});
	}

	public void huoltajanMuuttaminen() {

		Stage sikkuna = new Stage();
		sikkuna.initModality(Modality.WINDOW_MODAL);
		sikkuna.setTitle("huoltajan tietojen muuttaminen");
		BorderPane bh = new BorderPane(); 
		Scene scene2 = new Scene(bh, 400, 250);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		bh.getStyleClass().add("kehys");
		sikkuna.setScene(scene2);
		sikkuna.show();

		GridPane gh = new GridPane();
		gh.getStyleClass().add("fontti");
		bh.setCenter(gh);
		gh.setAlignment(Pos.CENTER);
		gh.setVgap(10);
		gh.setHgap(10);
		gh.setPadding(new Insets(10, 10, 10, 10));

		Label hl1 = new Label("asukkaan hetu: ");
		TextField ht1 = new TextField();
		Label hl2 = new Label("huoltaja: ");
		TextField ht2 = new TextField();
		Label hl3 = new Label("puhelinnumero: ");
		TextField ht3 = new TextField();
		Label hl4 = new Label("osoite: ");
		TextField ht4 = new TextField();
		Button hb1 = new Button("tallenna tiedot ");
		Label hl5 = new Label(" ");

		gh.add(hl1, 0, 0);
		gh.add(ht1, 1, 0);
		gh.add(hl2, 0, 1);
		gh.add(ht2, 1, 1);
		gh.add(hl3, 0, 2);
		gh.add(ht3, 1, 2);
		gh.add(hl4, 0, 3);
		gh.add(ht4, 1, 3);
		gh.add(hb1, 0, 4);
		gh.add(hl5, 1, 4);

		hb1.setOnAction((event) -> {
			String hetu = ht1.getText();
			String huoltaja = ht2.getText();
			String puhelinnumero = ht3.getText();
			String osoite = ht4.getText();
			sql.edunvalvojanMuuttaminen(hetu, huoltaja, puhelinnumero, osoite);
			hl5.setText("Huoltajan tiedot vaihdettu");
		});
	}

	public void tyontekijanTietojenMuuttaminen() {
		Stage sikkuna = new Stage();
		sikkuna.initModality(Modality.WINDOW_MODAL);
		sikkuna.setTitle("tyontekijan tietojen muuttaminen");
		BorderPane bt = new BorderPane(); 
		Scene scene2 = new Scene(bt, 400, 250);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		bt.getStyleClass().add("kehys");
		sikkuna.setScene(scene2);
		sikkuna.show();

		GridPane gt = new GridPane();
		gt.getStyleClass().add("fontti");
		bt.setCenter(gt);
		gt.setAlignment(Pos.CENTER);
		gt.setVgap(10);
		gt.setHgap(10);
		gt.setPadding(new Insets(10, 10, 10, 10));

		Label hl1 = new Label("työntekijän hetu: ");
		TextField ht1 = new TextField();
		Label hl2 = new Label("työntekijä: ");
		TextField ht2 = new TextField();
		Label hl3 = new Label("puhelinnumero: ");
		TextField ht3 = new TextField();
		Label hl4 = new Label("osoite: ");
		TextField ht4 = new TextField();
		Button hb1 = new Button("tallenna tiedot ");
		Label hl5 = new Label(" ");

		gt.add(hl1, 0, 0);
		gt.add(ht1, 1, 0);
		gt.add(hl2, 0, 1);
		gt.add(ht2, 1, 1);
		gt.add(hl3, 0, 2);
		gt.add(ht3, 1, 2);
		gt.add(hl4, 0, 3);
		gt.add(ht4, 1, 3);
		gt.add(hb1, 0, 4);
		gt.add(hl5, 1, 4);

		hb1.setOnAction((event) -> {
			String hetu = ht1.getText();
			String tyontekija = ht2.getText();
			String puhelinnumero = ht3.getText();
			String osoite = ht4.getText();
			sql.tyontekijanMuuttaminen(hetu, tyontekija, puhelinnumero, osoite);
			hl5.setText("Työntekijän tiedot vaihdettu");
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

	public void muutainfo() {

		Stage sikkuna = new Stage();
		sikkuna.initModality(Modality.WINDOW_MODAL);
		sikkuna.setTitle("asukkaan infon muuttaminen");
		BorderPane bt = new BorderPane(); 
		Scene scene2 = new Scene(bt, 400, 250);
		scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		bt.getStyleClass().add("kehys");
		sikkuna.setScene(scene2);
		sikkuna.show();

		GridPane gt = new GridPane();
		gt.getStyleClass().add("fontti");
		bt.setCenter(gt);
		gt.setAlignment(Pos.CENTER);
		gt.setVgap(10);
		gt.setHgap(10);
		gt.setPadding(new Insets(10, 10, 10, 10));

		Label hl1 = new Label("Asukkaan tämän hetkinen info: ");
		hl1.setStyle("-fx-font-size: 18px");
		Label hl2 = new Label(sql.haeInfo(asukas));
		hl2.setTextFill(Color.PERU);
		Label hl3 = new Label("Kirjoita sekä vanha pätevä info sekä mahdollinen uusi lisäys.");
		Label hl4 = new Label("info: ");
		TextField ht1 = new TextField();
		Button hb1 = new Button("tallenna");
		Label hl5 = new Label(" ");

		gt.add(hl1, 0, 0,5,1);
		gt.add(hl2, 0, 1);
		gt.add(hl3, 0, 2,5,1);
		gt.add(hl4, 0, 3);
		gt.add(ht1, 1, 3);
		gt.add(hb1, 0, 4);
		gt.add(hl5, 1, 4);

		hb1.setOnAction((event) -> {
			String info = ht1.getText();
			sql.muutaInfo(asukas, info);
			hl5.setText("Uusi info: " + info);
		});

	}



	public static void main(String[] args) {
		launch(args);
	}
}

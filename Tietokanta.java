package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tietokanta {


	Connection con;
	int id;

	public Connection yhteydenAvaus() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hoitokoti?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "tummeli", "tummeli");
			return con;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	public void yhteydenSulkeminen() {
		try{
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String lisaaAsukas (String nimi, String hetu, String info, String huoltaja, String huoltajan_puhelinnumero, String huoltajan_osoite, String edunvalvoja, String edunvalvojan_puhelinnumero, String edunvalvojan_osoite){
		try {
			ResultSet haku = etsiAsukas(nimi);
			if(haku.next()) {
				return "Asukas löytyy jo tietokannasta. Lisääminen ei täten onnistunut.";
			}else {
				PreparedStatement lauseke = con.prepareStatement("Insert into asukkaat(nimi,hetu,info,huoltaja, huoltajan_puhelinnumero, huoltajan_osoite, edunvalvoja, edunvalvojan_puhelinnumero, edunvalvojan_osoite ) values (?,?,?,?,?,?,?,?,?)");
				lauseke.setString(1, nimi);
				lauseke.setString(2, hetu);
				lauseke.setString(3, info);
				lauseke.setString(4, huoltaja);
				lauseke.setString(5, huoltajan_puhelinnumero);
				lauseke.setString(6, huoltajan_osoite);
				lauseke.setString(7, edunvalvoja);
				lauseke.setString(8, edunvalvojan_puhelinnumero);
				lauseke.setString(9, edunvalvojan_osoite);
				lauseke.executeUpdate();
				return "Uusi asukas on lisätty tietokantaan";
			}
		}

		catch(Exception e) {
			System.out.print(e);

		}
		return null;
	}

	public ResultSet haeKirjauksetA(String nimi) {
		int asukkaan_id = 0;
		try {
			PreparedStatement lauseke = con.prepareStatement("Select id from asukkaat where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				asukkaan_id = haku.getInt("id");
				PreparedStatement lauseke2 = con.prepareStatement("select * from kirjaukset where asukkaan_id = ?");
				lauseke2.setInt(1, asukkaan_id);
				ResultSet haku2 = lauseke2.executeQuery();
				return haku2;
			}
		} catch (Exception e) {
		}return null;
	}

	public ResultSet haeKirjauksetApvm(String pvm, String nimi) {
		int asukkaan_id = 0;
		try {
			PreparedStatement lauseke = con.prepareStatement("Select id from asukkaat where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				asukkaan_id = haku.getInt("id");
				PreparedStatement lauseke2 = con.prepareStatement("select * from kirjaukset where pvm = ? and asukkaan_id =?");
				lauseke2.setString(1, pvm);
				lauseke2.setInt(2, asukkaan_id);
				ResultSet haku2 = lauseke2.executeQuery();
				return haku2;
			}
		} catch (Exception e) {
		}return null;
	}

	public ResultSet haeAsukkaat() {
		try {
			PreparedStatement lauseke = con.prepareStatement("select nimi from asukkaat");
			ResultSet haku = lauseke.executeQuery();
			return haku;
		} catch (Exception e) {
		}return null;
	}

	public ResultSet etsiAsukas(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select * from asukkaat where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			return haku;
		}

		catch(Exception e){ 
			System.out.println(e);
			return null;
		}
	}
	public int haeAsukkaanId(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select id from asukkaat where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getInt("id");
			}
			return -1;
		}

		catch(Exception e){ 
			System.out.println(e);
			return -1;
		}
	}

	public String etsiAsukasid(int id) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select nimi from asukkaat where id = ?");
			lauseke.setInt(1, id);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getString("nimi");
			}
			return "";
		}

		catch(Exception e){ 
			System.out.println(e);
			return null;
		}
	}

	public void edunvalvojanMuuttaminen(String hetu, String edunvalvoja, String edunvalvojan_puhelinnumero, String edunvalvoja_osoite ) {

		try {
			PreparedStatement lauseke2 = con.prepareStatement("Update asukkaat set edunvalvoja = ?, edunvalvojan_puhelinnumero = ?, edunvalvojan_osoite = ? where hetu = ?");
			lauseke2.setString(1, edunvalvoja);
			lauseke2.setString(2, edunvalvojan_puhelinnumero);
			lauseke2.setString(3, edunvalvoja_osoite);
			lauseke2.setString(4, hetu);
			lauseke2.executeUpdate();

		}catch(Exception e){ 
			System.out.println(e);
		} 

	}

	public void huoltajanMuuttaminen(String hetu, String huoltaja, String huoltajan_puhelinnumero, String huoltajan_osoite ) {

		try {
			PreparedStatement lauseke2 = con.prepareStatement("Update asukkaat set huoltaja = ?, huoltajan_puhelinnumero = ?, huoltajan_osoite = ? where hetu = ?");
			lauseke2.setString(1, huoltaja);
			lauseke2.setString(2, huoltajan_puhelinnumero);
			lauseke2.setString(3, huoltajan_osoite);
			lauseke2.setString(4, hetu);
			lauseke2.executeUpdate();

		}catch(Exception e){ 
			System.out.println(e);
		} 
	}

	public void poistaAsukas(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("delete from asukkaat where nimi =?");
			lauseke.setString(1, nimi);
			lauseke.executeUpdate();
		}catch(Exception e){ 
			System.out.println(e);
		}}

	public String haeInfo(String asukas) {
		try {
			PreparedStatement lauseke = con.prepareStatement("select info from asukkaat where nimi =?");
			lauseke.setString(1, asukas);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getString("info");
			}}catch(Exception e){ 
				System.out.println(e);

			}
		return null;
	}

	public void muutaInfo(String nimi, String info) {
		try {
			PreparedStatement lauseke = con.prepareStatement("Update asukkaat set info =? where nimi = ?");
			lauseke.setString(1, info);
			lauseke.setString(2, nimi);
			lauseke.executeUpdate();

		}catch(Exception e){ 
			System.out.println(e);
		} 

	}



	public String lisaaTyontekija(String nimi, String hetu, String osoite, String puhelinnumero,String ammatti, String tunnus, String salasana){
		try {
			ResultSet haku = etsiTyontekija(nimi);
			if(haku.next()) {
				return "Työntekijä jo tietokannasta. Lisääminen ei onnistunut.";
			}else {
				PreparedStatement lauseke = con.prepareStatement("Insert into tyontekija(nimi,hetu,osoite, puhelinnumero,ammatti,tunnus,salasana) values (?,?,?,?,?,?,?)");
				lauseke.setString(1, nimi);
				lauseke.setString(2, hetu);
				lauseke.setString(3, osoite);
				lauseke.setString(4, puhelinnumero);
				lauseke.setString(5, ammatti);
				lauseke.setString(6, tunnus);
				lauseke.setString(7, salasana);
				lauseke.executeUpdate();
				return "Uusi työntekijä on lisätty tietokantaan";
			}
		}

		catch(Exception e) {
			System.out.println(e);
			System.out.println("joku epäonnistui");
		}
		return null;
	}


	public ResultSet haeTyontekijat() {
		try {
			PreparedStatement lauseke = con.prepareStatement("select nimi from tyontekija");
			ResultSet haku = lauseke.executeQuery();
			return haku;
		} catch (Exception e) {
		}return null;
	}


	public ResultSet haeKirjauksetT(String nimi) {
		int kirjaajan_id = 0;
		try {
			PreparedStatement lauseke = con.prepareStatement("Select id from tyontekija where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				kirjaajan_id = haku.getInt("id");
				PreparedStatement lauseke2 = con.prepareStatement("select * from kirjaukset where kirjaajan_id =?");
				lauseke2.setInt(1, kirjaajan_id);
				ResultSet haku2 = lauseke2.executeQuery();
				return haku2;
			}
		} catch (Exception e) {
		}return null;
	}

	public String haeTyontekijaTunnuksella(String tunnus) {
		try {
			PreparedStatement lauseke = con.prepareStatement("Select nimi from tyontekija where tunnus =?");
			lauseke.setString(1, tunnus);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getString("nimi");
			}
			return "";
		} catch (Exception e) {
			return null;
		}

	}

	public int haeTyontekijanId(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select id from tyontekija where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getInt("id");
			}
			return -1;
		}

		catch(Exception e){ 
			System.out.println(e);
			return -1;
		}
	}
	public ResultSet etsiTyontekija(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select * from tyontekija where nimi = ?");
			lauseke.setString(1, nimi);
			ResultSet haku = lauseke.executeQuery();
			return haku;
		}

		catch(Exception e){ 
			System.out.println(e);
			return null;
		}
	}

	public String etsiTyontekijaid(int id) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select nimi from tyontekija where id = ?");
			lauseke.setInt(1, id);
			ResultSet haku = lauseke.executeQuery();
			if(haku.next()) {
				return haku.getString("nimi");
			}
			return "";
		}

		catch(Exception e){ 
			System.out.println(e);
			return null;
		}
	}

	public void salasananMuuttaminen(String tunnus, String uusisalasana) {

		try {
			PreparedStatement lauseke2 = con.prepareStatement("Update tyontekija set salasana = ? where tunnus = ?");
			lauseke2.setString(1, uusisalasana);
			lauseke2.setString(2, tunnus);
			lauseke2.executeUpdate();

		}catch(Exception e){ 
			System.out.println(e);
		} 
	}

	public int tarkistaKirjautuja(String tunnus, String salasana) {

		try {
			PreparedStatement lauseke = con.prepareStatement("Select * from tyontekija where tunnus = ?");
			lauseke.setString(1, tunnus);
			ResultSet haku = lauseke.executeQuery();

			if(haku.next()) {
				if(haku.getString("salasana").equals(salasana)) {
					return haku.getInt("id");
				}
			}
			return -1;
		}
		catch(Exception e){ 
			System.out.println(e);
			return -1;
		} 
	}

	public String kirjaaminen(String pvm, String kello, String kirjaus, int kirjaajan_id, int asukkaan_id) {


		try {
			PreparedStatement lauseke = con.prepareStatement("Insert into kirjaukset(pvm, kello, kirjaus, kirjaajan_id, asukkaan_id) values (?,?,?,?,?)");
			lauseke.setString(1, pvm);
			lauseke.setString(2, kello);
			lauseke.setString(3, kirjaus);
			lauseke.setInt(4, kirjaajan_id);
			lauseke.setInt(5,asukkaan_id);
			lauseke.executeUpdate();
			return "Kirjaus onnistunut";
		}catch(Exception e){ 
			System.out.println(e);
		}
		return null;
	}




	public void tyontekijanMuuttaminen(String hetu, String nimi, String puhelinnumero, String osoite ) {

		try {
			PreparedStatement lauseke2 = con.prepareStatement("Update tyontekija set nimi = ?, osoite = ?, puhelinnumero = ? where hetu = ?");
			lauseke2.setString(1, nimi);
			lauseke2.setString(2, osoite);
			lauseke2.setString(3, puhelinnumero);
			lauseke2.setString(4, hetu);
			lauseke2.executeUpdate();

		}catch(Exception e){ 
			System.out.println(e);
		} 

	}

	public void poistaTyontekija(String nimi) {

		try {
			PreparedStatement lauseke = con.prepareStatement("delete from tyontekija where nimi =?");
			lauseke.setString(1, nimi);
			lauseke.executeUpdate();
		}catch(Exception e){ 
			System.out.println(e);
		}}


}

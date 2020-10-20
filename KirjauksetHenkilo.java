package application;



public class KirjauksetHenkilo {

	private String kirjaaja;
	private String asukas;
	private String pvm;
	private String kello;
	private String kirjaus;

	public KirjauksetHenkilo(String kirjaaja, String asukas, String pvm, String kello, String kirjaus) {
		this.kirjaaja = kirjaaja;
		this.asukas = asukas;
		this.pvm = pvm;
		this.kello = kello;
		this.kirjaus = kirjaus;
	}

	public String getKirjaaja() {
		return kirjaaja;
	}

	public void setKirjaaja(String kirjaaja) {
		this.kirjaaja = kirjaaja;
	}

	public String getAsukas() {
		return asukas;
	}

	public void setAsukas(String asukas) {
		this.asukas = asukas;
	}

	public String getPvm() {
		return pvm;
	}

	public void setPvm(String pvm) {
		this.pvm = pvm;
	}

	public String getKello() {
		return kello;
	}

	public void setKello(String kello) {
		this.kello = kello;
	}

	public String getKirjaus() {
		return kirjaus;
	}

	public void setKirjaus(String kirjaus) {
		this.kirjaus = kirjaus;
	}
}
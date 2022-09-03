package entiteti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Izvestaj {
	public LocalDate datum;
	private int dolasci;
	private int odlasci;
	private HashMap<String, Integer> sobariceSobe;

	public Izvestaj(LocalDate datum, int dolasci, int odlasci, HashMap<String, Integer> sobariceSobe) {
		super();
		this.datum = datum;
		this.odlasci = odlasci;
		this.dolasci = dolasci;
		this.sobariceSobe = sobariceSobe;

	}
	
	public String getDatum() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return this.datum.format(formater);
	}
	
	public int getDolasci() {
		return dolasci;
	}

	public void setDolasci(int dolasci) {
		this.dolasci = dolasci;
	}

	public int getOdlasci() {
		return odlasci;
	}

	public void setOdlasci(int odlasci) {
		this.odlasci = odlasci;
	}

	public HashMap<String, Integer> getSobariceSobe() {
		return sobariceSobe;
	}

	public void setSobariceSobe(HashMap<String, Integer> sobariceSobe) {
		this.sobariceSobe = sobariceSobe;
	}
	
	public void dodajSSPar(String sobarica) {
		if (sobariceSobe.containsKey(sobarica)) {
			int broj = sobariceSobe.get(sobarica) + 1;
			sobariceSobe.put(sobarica, broj);
		} else {
			sobariceSobe.put(sobarica, 1);
		}
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return "\n" + datum.format(formater) + "/" + dolasci + "/" + odlasci + "\n";
	}

	public String stringZaFile() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		String s = "?/" + this.datum.format(formater);
		for (String sobarica : this.sobariceSobe.keySet()) {
			s += "/" + sobarica + "-" + this.sobariceSobe.get(sobarica);
		}
		return this + s;
	}

}

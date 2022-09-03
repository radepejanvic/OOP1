package entiteti;

import java.time.LocalDate;
import java.util.ArrayList;

public class Sobarica extends Zaposleni {
	private ArrayList<String> listaSoba; 
	
	public Sobarica() {
		super();
	}

	public Sobarica(String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa,
			String korisnickoIme, String lozinka, String strucnaSprema, int staz, ArrayList<String> listaSoba) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
	}
	
	
	
	public ArrayList<String> getListaSoba() {
		return listaSoba;
	}

	public void setListaSoba(ArrayList<String> listaSoba) {
		this.listaSoba = listaSoba;
	}

	@Override
	public String stringZaFile() {
		String sobe = "\n?/" + this.korisnickoIme;
		for (String s : listaSoba) {
			sobe += "/" + s;
		}
		return "\nsobarica" + this + sobe;
	}

}

package entiteti;

import java.time.LocalDate;

public class Recepcioner extends Zaposleni {
	public Recepcioner() {
		super();
	}

	public Recepcioner(String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa,
			String korisnickoIme, String lozinka, String strucnaSprema, int staz) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
	}

	@Override
	public String stringZaFile() {
		return "\nrecepcioner" + this;
	}

}

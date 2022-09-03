package entiteti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Gost extends Korisnik {
	public Gost() {
		super();
	}

	public Gost(String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa,
			String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
	}

	@Override
	public String toString() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return "/" + ime + "/" + prezime + "/" + pol + "/" + datumRodjenja.format(formater) + "/" + telefon + "/"
				+ adresa + "/" + korisnickoIme + "/" + lozinka;
	}

	@Override
	public String stringZaFile() {
		return "\ngost" + this;
	}

}

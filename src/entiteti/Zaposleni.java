package entiteti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Zaposleni extends Korisnik {
	private final float OSNOVICA = 20000;
	StrucnaSprema strucnaSprema;
	int staz;
	double plata;

	public Zaposleni() {
		super();
	}

//  double osnovica -> dodaj u konstruktor i u atribut metode izracunaj platu
	public Zaposleni(String ime, String prezime, String pol, LocalDate datumRodjenja, String telefon, String adresa,
			String korisnickoIme, String lozinka, String strucnaSprema, int staz) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.strucnaSprema = StrucnaSprema.valueOf(strucnaSprema);
		this.staz = staz;
		this.plata = izracunajPlatu();
	}

// 	mozda osnovicu da stavim kao konstantu, ukoliko ne postoji opcija za admina gde moze da menja osnovice svima 
//	double osnovica
//	prebacio na pulbic jer se mora pozvati svaki put prilikom izmene struvne spreme ili staza
	public double izracunajPlatu() {
		return this.OSNOVICA * strucnaSprema.getKoeficijent() * staz;
	}

	public StrucnaSprema getStrucnaSprema() {
		return strucnaSprema;
	}

	public void setStrucnaSprema(StrucnaSprema strucnaSprema) {
		this.strucnaSprema = strucnaSprema;
	}

	public int getStaz() {
		return staz;
	}

	public void setStaz(int staz) {
		this.staz = staz;
	}

	public double getPlata() {
		return plata;
	}

	public void setPlata(double plata) {
		this.plata = plata;
	}

	@Override
	public String toString() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return "/" + ime + "/" + prezime + "/" + pol + "/" + datumRodjenja.format(formater) + "/" + telefon + "/"
				+ adresa + "/" + korisnickoIme + "/" + lozinka + "/" + strucnaSprema + "/" + staz;
	}

}

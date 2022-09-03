package entiteti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Cenovnik {
	public String sifraCenovnika;
	LocalDate datumOd;
	LocalDate datumDo;
	HashMap<String, Double> cenovnik = new HashMap<String, Double>();

	public Cenovnik() {
		super();
	}

	public Cenovnik(String sifraCenovnika, LocalDate datumOd, LocalDate datumDo, HashMap<String, Double> cenovnik) {
		super();
		this.sifraCenovnika = sifraCenovnika;
		this.datumOd = datumOd;
		this.datumDo = datumDo;
		this.cenovnik = cenovnik;
	}

	public boolean uporediDatum(LocalDate datumOd) {
		if (!this.datumOd.isAfter(datumOd) && !this.datumDo.isBefore(datumOd)) {
			return true;
		}
		return false;
	}

	public LocalDate getDatumOd() {
		return datumOd;
	}

	public void setDatumOd(LocalDate datumOd) {
		this.datumOd = datumOd;
	}

	public LocalDate getDatumDo() {
		return datumDo;
	}

	public void setDatumDo(LocalDate datumDo) {
		this.datumDo = datumDo;
	}

	public HashMap<String, Double> getCenovnik() {
		return cenovnik;
	}

	public void setCenovnik(HashMap<String, Double> cenovnik) {
		if (this.cenovnik == null) {
			this.cenovnik = cenovnik;
		} else {
			this.cenovnik.putAll(cenovnik);
		}
	}

	@Override
	public String toString() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return "\n" + sifraCenovnika + "/" + datumOd.format(formater) + "/" + datumDo.format(formater) + "\n";
	}

	public String stringZaFile() {
		String cene = "?/" + this.sifraCenovnika;
		for (String kljuc : this.cenovnik.keySet()) {
			cene += "/" + kljuc + "-" + this.cenovnik.get(kljuc);
		}
		return this + cene;
	}

//	TODO premesti ovu funkciju u rezervacije
//	public double obracunajCenu() {
//		return 2;
//	}
}

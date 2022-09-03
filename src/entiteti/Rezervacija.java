package entiteti;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import menadzeri.CenovniciManager;
import menadzeri.SobeManager;

public class Rezervacija {
	public String brojRezervacije;
	String gost;
	String brojSobe;
	LocalDate datumOd;
	LocalDate datumDo;
//	dorucak rucak vecera
	ArrayList<DodatneUsluge> dodatneUsluge;
	// wifi, frizider, klima...
	ArrayList<DodatniKriterijumi> dodatniKriterijumi;
	double cena;
	StatusRezervacije statusRezervacije;

	public Rezervacija() {
		super();
	}

	public Rezervacija(String brojRezervacije, String gost, String brojSobe, LocalDate datumOd, LocalDate datumDo,
			ArrayList<DodatneUsluge> dodatneUsluge, ArrayList<DodatniKriterijumi> dodatniKriterijumi, double cena,
			String statusRezervacije) {
		super();
		this.brojRezervacije = brojRezervacije;
		this.gost = gost;
		this.brojSobe = brojSobe;
		this.datumOd = datumOd;
		this.datumDo = datumDo;
		this.dodatneUsluge = dodatneUsluge;
		this.dodatniKriterijumi = dodatniKriterijumi;
		if (cena == 0) {
			try {
				this.cena = this.obracunajCenu();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.cena = cena;
		}
		this.statusRezervacije = StatusRezervacije.valueOf(statusRezervacije);
	}

//	vraca listu datuma koji su zauzeti ovom rezervacijom
	public ArrayList<LocalDate> generisiDatume() {
		ArrayList<LocalDate> datumi = new ArrayList<LocalDate>();
		LocalDate datum = this.datumOd;
		while (!datum.isAfter(this.datumDo)) {
			datumi.add(datum);
			datum = datum.plusDays(1);
		}
		return datumi;
	}

//	brise zauzeet datume iz liste datuma zeljene sobe
	public void zauzmiDatume() throws IOException {
		SobeManager sm = SobeManager.getInstance();
		ArrayList<LocalDate> datumi = sm.getSoba(brojSobe).getListaDatuma();
		datumi.removeAll(this.generisiDatume());
	}

//	vraca zauzete datume nazad u listu datuma zeljene sobe
	public void oslobodiDatume() throws IOException {
		SobeManager sm = SobeManager.getInstance();
		ArrayList<LocalDate> datumi = sm.getSoba(brojSobe).getListaDatuma();
		ArrayList<LocalDate> izbaceni = this.generisiDatume();
		LocalDate datum = izbaceni.get(0);
		for (int i = 0; i < datumi.size(); i++) {
			if (datumi.get(i).isAfter(datum) && datumi.get(i - 1).isBefore(datum)) {
				datumi.addAll(i, izbaceni);
				break;
			}
		}
	}

	public double obracunajCenu() throws IOException {
		CenovniciManager cm = CenovniciManager.getInstance();
		SobeManager sm = SobeManager.getInstance();
		String tipSobe = "" + sm.getSoba(brojSobe).getTipSobe();
		HashMap<String, Double> mapa = cm.izaberiCenovnik(this).getCenovnik();
		double cena = 0;
		int brojDana = (int) ChronoUnit.DAYS.between(this.datumOd, datumDo) + 1;
		cena += mapa.get(tipSobe);
		for (DodatneUsluge du : this.dodatneUsluge) {
			String kljuc = "" + du;
			cena += mapa.get(kljuc);
		}
		return cena * brojDana;
	}
	
	public ArrayList<LocalDate> getDatumi() {
		ArrayList<LocalDate> datumi = new ArrayList<LocalDate>();
		LocalDate datum = this.datumOd;
		while(!datum.isAfter(this.datumDo)) {
			datumi.add(datum);
			datum = datum.plusDays(1);
		}
		return datumi;
	}

	public String getBrojRezervacije() {
		return brojRezervacije;
	}

	public void setBrojRezervacije(String brojRezervacije) {
		this.brojRezervacije = brojRezervacije;
	}

	public String getGost() {
		return gost;
	}

	public void setGost(String gost) {
		this.gost = gost;
	}

	public String getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(String brojSobe) {
		this.brojSobe = brojSobe;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public StatusRezervacije getStatusRezervacije() {
		return statusRezervacije;
	}

	public void setStatusRezervacije(StatusRezervacije statusRezervacije) {
		this.statusRezervacije = statusRezervacije;
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

	public ArrayList<DodatneUsluge> getDodatneUsluge() {
		return dodatneUsluge;
	}

	public void setDodatneUsluge(ArrayList<DodatneUsluge> dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}

	public ArrayList<DodatniKriterijumi> getDodatniKriterijumi() {
		return dodatniKriterijumi;
	}

	public void setDodatniKriterijumi(ArrayList<DodatniKriterijumi> dodatniKriterijumi) {
		this.dodatniKriterijumi = dodatniKriterijumi;
	}

	@Override
	public String toString() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		return "\n" + brojRezervacije + "/" + gost + "/" + brojSobe + "/" + datumOd.format(formater) + "/"
				+ datumDo.format(formater) + "/" + cena + "/" + statusRezervacije + "\n";
	}

	public String stringZaFile() {
//		string dodatne usluge
		String du = "?/" + this.brojRezervacije;
//		string dodatni kriterijumi
		String dk = "??/" + this.brojRezervacije;
		for (DodatneUsluge d : this.dodatneUsluge) {
			du += "/" + d;
		}
		for (DodatniKriterijumi d : this.dodatniKriterijumi) {
			dk += "/" + d;
		}
		return this + du + "\n" + dk;
	}
}

//	rade/001/10.03.2022./11.03.2022./dorucak/rucak/vecera/wifi/frizider
//	                                / --- odavde moze da ne postoji ---

//	PROTOTIP:
//	1/rade/001/10.03.2022./11.03.2022.
//	?/1/dorucak/rucak/vecera
//  ??/1/wifi/frizider
//	NOVA IDEJA -> svi podaci se nalaze u jednom fajlu 

//  kako konstruktor napraviti ako se ne zna sta i koliko prima -> tako sto ce se prosledjivati cela lista svaki put 
//  -> ucitavacu iz 3 razlicita fajla -> rezervacija -> dodatne usluge -> dodatni kriterijumi
//  medjusobno ih povezuje broj rezervacije -> napravi se 1 HashMapa <String, Rezervacije>, u kojoj se nalazi
//	kombinacija parova brojRezervacije - Reaervacija -> gde ce inicijalno sve rezervacije imati praznu listu, 
//	pa ce se onda naknadno dodati svakoj rezervaciji prava lista elemenata koja ce se ucitavati iz ostalih fajlova
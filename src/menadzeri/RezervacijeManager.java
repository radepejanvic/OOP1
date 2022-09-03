package menadzeri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import entiteti.DodatneUsluge;
import entiteti.DodatniKriterijumi;
import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.StatusRezervacije;
import entiteti.StatusSobe;
import entiteti.TipSobe;
import entiteti.Rezervacija;

public class RezervacijeManager implements Manager {
	private static RezervacijeManager instance = null;
	private final String FILE = "rezervacije.txt";
	private HashMap<String, Rezervacija> mapaRezervacija;
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	private int brojRezervacija = 0;

	private RezervacijeManager() throws IOException {
		super();
		this.mapaRezervacija = new HashMap<String, Rezervacija>();
		ucitaj();
	}

	public static RezervacijeManager getInstance() throws IOException {
		if (instance == null) {
			instance = new RezervacijeManager();
		}
		return instance;
	}

	public boolean proveriBrojRezervacije(String brojRezervacije) {
		if (brojRezervacije.matches("[0-9]+") && !mapaRezervacija.containsKey(brojRezervacije)) {
			return true;
		}
		return false;
	}

	public boolean proveriGosta(String korisnickoIme) {
		try {
			KorisniciManager km = KorisniciManager.getInstance();
			if (km.daLiPostojiKorisnik(korisnickoIme)) {
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public boolean proveriSobu(String soba) {
		try {
			SobeManager sm = SobeManager.getInstance();
			if (sm.daLiPostojiSoba(soba)) {
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public boolean proveriDatumOd(String datumOd) {
		try {
			LocalDate dt = LocalDate.parse(datumOd, formater);
			LocalDate danas = LocalDate.now();
			LocalDate kraj = danas.plusYears(1);
			if (!dt.isBefore(danas) && dt.isBefore(kraj.minusDays(1))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

//	ne moze se rezervisati poslednji dan -> namesteno da bi se lakse azurirala lista
	public boolean proveriDatumDo(String datumOd, String datumDo) {
		try {
			LocalDate dtOd = LocalDate.parse(datumOd, formater);
			LocalDate dtDo = LocalDate.parse(datumDo, formater);
			LocalDate danas = LocalDate.now();
			danas = danas.plusYears(1);
			if (dtDo.isAfter(dtOd) && !dtDo.isAfter(danas) && dtDo.isBefore(danas.plusYears(1))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean proveriDodatneUsluge(String dodatnaUsluga) {
		if (DodatneUsluge.contains(dodatnaUsluga)) {
			return true;
		}
		return false;
	}

	public boolean proveriDodatneKriterijume(String dodatniKriterijum) {
		if (DodatniKriterijumi.contains(dodatniKriterijum)) {
			return true;
		}
		return false;
	}

	public boolean proveriCenu(String cena) {
		try {
			Double.parseDouble(cena);
			return true;
		} catch (Exception e) {
			if (cena.equals("")) {
				return true;
			}
			return false;
		}
	}

	public boolean proveriStatusRezervacije(String statusRezervacije) {
		if (StatusRezervacije.contains(statusRezervacije)) {
			return true;
		}
		return false;
	}

	public ArrayList<Rezervacija> getRezervacije() {
		ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
		for (Rezervacija r : mapaRezervacija.values()) {
			rezervacije.add(r);
		}
		return rezervacije;
	}

	public ArrayList<Rezervacija> mojeRezervacije(Korisnik gost) {
		ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
		for (Rezervacija r : mapaRezervacija.values()) {
			if (r.getGost().equals(gost.korisnickoIme) && r.getStatusRezervacije().toString().equals("NACEKANJU")) {
				rezervacije.add(r);
			}
		}
		return rezervacije;
	}

	@Override
	public void ucitaj() throws IOException {
		String sp = System.getProperty("file.separator");
		BufferedReader br = new BufferedReader(new FileReader("." + sp + this.FILE));
		String linija;
//		mozda ako treba datum
		LocalDate datumOd;
		LocalDate datumDo;
		double cena;
		while ((linija = br.readLine()) != null) {
			if (!linija.startsWith("#") && !linija.isEmpty()) {
				if (linija.startsWith("??")) {
//					ovde se ucitavaju DodatniKriterijumi
					String[] dodatniKriterijumi = linija.split("/");
					ArrayList<DodatniKriterijumi> dk = new ArrayList<DodatniKriterijumi>();
					try {
						for (int i = 2; i < dodatniKriterijumi.length; i++) {
							dk.add(DodatniKriterijumi.valueOf(dodatniKriterijumi[i]));
//							System.out.println(dodatniKriterijumi[i]);
						}
					} catch (Exception e) {
						System.out.println("Nema vise elemenata");
					}
					Rezervacija r = mapaRezervacija.get(dodatniKriterijumi[1]);
					r.setDodatniKriterijumi(dk);

				} else if (linija.startsWith("?")) {
//					ovde se ucitavaju DodatneUsluge
//					lista usluga koje se dobiju parsiranjem
					String[] dodatneUsluge = linija.split("/");
//					lista koja se prosledjuje objektu
					ArrayList<DodatneUsluge> du = new ArrayList<DodatneUsluge>();
					try {
						for (int i = 2; i < dodatneUsluge.length; i++) {
							du.add(DodatneUsluge.valueOf(dodatneUsluge[i]));
//							System.out.println(dodatneUsluge[i]);
						}
					} catch (Exception e) {
						System.out.println("Nema vise elemenata");
					}
					Rezervacija r = mapaRezervacija.get(dodatneUsluge[1]);
					r.setDodatneUsluge(du);

				} else {
//					ovde se ucitavaju ostali podaci i formira opbjekat rezervacija
					String[] rezervacije = linija.split("/");
					datumOd = LocalDate.parse(rezervacije[3], formater);
					datumDo = LocalDate.parse(rezervacije[4], formater);
					cena = Double.parseDouble(rezervacije[5]);
					Rezervacija r = new Rezervacija(rezervacije[0], rezervacije[1], rezervacije[2], datumOd, datumDo,
							null, null, cena, rezervacije[6]);
					mapaRezervacija.put(rezervacije[0], r);
					this.brojRezervacija++;
				}
			} else {
//				linija je komentar ako nije je usla u else granu provere 
			}

		}
		br.close();

	}

	@Override
	public void obrisi(String brojRezervacije) throws IOException {
		mapaRezervacija.get(brojRezervacije).oslobodiDatume();
		mapaRezervacija.remove(brojRezervacije);
	}

	@Override
	public void dodaj(Object o, boolean dodajMapu) throws IOException {
		if (o instanceof Rezervacija) {
			Rezervacija r = (Rezervacija) o;
			if (dodajMapu) {
				mapaRezervacija.put(r.brojRezervacije, r);
			}
			r.zauzmiDatume();
			this.brojRezervacija++;
			String sp = System.getProperty("file.separator");
			PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE, true));
			pw.write(r.stringZaFile());
			pw.close();
		} else {
			throw new IllegalArgumentException("Prosledjeni objekat nije klase Rezervacija");
		}

	}

	@Override
	public void izmeni(Object o) throws IOException {
		Rezervacija nova = (Rezervacija) o;
		mapaRezervacija.put(nova.brojRezervacije, nova);
		azuriraj();
	}

	@Override
	public void azuriraj() throws IOException {
		String sp = System.getProperty("file.separator");
		PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE));
		pw.write("");
		pw.close();
		for (Rezervacija r : mapaRezervacija.values()) {
			dodaj(r, false);
		}

	}

	@Override
	public void ispisi() throws IOException {
		for (Rezervacija r : mapaRezervacija.values()) {
			System.out.println(r.stringZaFile());
		}

	}

	@Override
	public Object get(String kljuc) {
		return mapaRezervacija.get(kljuc);

	}

	public Rezervacija test() throws IOException {
		return mapaRezervacija.get("1");
	}

	@Override
	public void azurirajListe() {
		// TODO Auto-generated method stub

	}

	public String getBrRez() {
		this.brojRezervacija++;
		while (this.mapaRezervacija.containsKey(this.brojRezervacija + "")) {
			this.brojRezervacija++;
		}
		return this.brojRezervacija + "";
	}

	public Rezervacija getRezervacija(String kljuc) {
		return mapaRezervacija.get(kljuc);
	}

	public ArrayList<Rezervacija> getNaCekanju() {
		ArrayList<Rezervacija> naCekanju = new ArrayList<Rezervacija>();
		for (Rezervacija r : mapaRezervacija.values()) {
			if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("NACEKANJU"))) {
				naCekanju.add(r);
			}
		}
		return naCekanju;
	}

	public ArrayList<Rezervacija> getCheckIn() {
		ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
		try {
			SobeManager sm = SobeManager.getInstance();
			for (Rezervacija r : mapaRezervacija.values()) {
				if (LocalDate.now().equals(r.getDatumOd())
						&& r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))) {
					Soba s = sm.getSoba(r.getBrojSobe());
					if (s.getStatusSobe().equals(StatusSobe.valueOf("SLOBODNA"))) {
						rezervacije.add(r);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rezervacije;
	}

	public ArrayList<Rezervacija> getCheckOut() {
		ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
		try {
			SobeManager sm = SobeManager.getInstance();
			for (Rezervacija r : mapaRezervacija.values()) {
				if (LocalDate.now().equals(r.getDatumDo())
						&& r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))) {
					Soba s = sm.getSoba(r.getBrojSobe());
					if (s.getStatusSobe().equals(StatusSobe.valueOf("ZAUZETA"))) {
						rezervacije.add(r);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rezervacije;
	}

	public void otkaziRezervacije() {
		LocalDate danas = LocalDate.now();
		for (Rezervacija r : mapaRezervacija.values()) {
			if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("NACEKANJU"))
					&& r.getDatumOd().isBefore(danas)) {
				r.setStatusRezervacije(StatusRezervacije.valueOf("ODBIJENA"));
			}
		}
		try {
			azuriraj();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getPrihodi(LocalDate datumOd, LocalDate datumDo) {
		double suma = 0;
		for (Rezervacija r : mapaRezervacija.values()) {
			if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))
					|| r.getStatusRezervacije().equals(StatusRezervacije.valueOf("OTKAZANA"))) {
				int i = 0;
				for (LocalDate datum : r.getDatumi()) {
					if (!datum.isBefore(datumOd) && !datum.isAfter(datumDo)) {
						i++;
					}
				}
				suma += r.getCena() / r.getDatumi().size() * i;
			}
		}
		return suma;
	}

	public double getPrihodiTipa(TipSobe tipSobe, LocalDate datumOd, LocalDate datumDo) {
		double suma = 0;
		try {
			SobeManager sm = SobeManager.getInstance();
			for (Rezervacija r : mapaRezervacija.values()) {
				if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))
						|| r.getStatusRezervacije().equals(StatusRezervacije.valueOf("OTKAZANA"))) {
					Soba s = sm.getSoba(r.getBrojSobe());
					if (s.getTipSobe().equals(tipSobe)) {
						int i = 0;
						for (LocalDate datum : r.getDatumi()) {
							if (!datum.isBefore(datumOd) && !datum.isAfter(datumDo)) {
								i++;
							}
						}
						suma += r.getCena() / r.getDatumi().size() * i;
					}

				}
			}
			return suma;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public double getRashodi(LocalDate datumOd, LocalDate datumDo) {
		double suma = 0;
		for (Rezervacija r : mapaRezervacija.values()) {
			if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("ODBIJENA"))) {
				int i = 0;
				for (LocalDate datum : r.getDatumi()) {
					if (!datum.isBefore(datumOd) && !datum.isAfter(datumDo)) {
						i++;
					}
				}
				suma += r.getCena() / r.getDatumi().size() * i;
			}
		}
		return suma;
	}

	public int[] prebrojStatuseRezervacija(LocalDate datumOd, LocalDate datumDo) {
		int[] statusi = { 0, 0, 0, 0 };
		for (Rezervacija r : mapaRezervacija.values()) {
			System.out.println(r.getDatumOd().format(formater));
			if (!r.getDatumOd().isAfter(datumDo) && !r.getDatumOd().isBefore(datumOd)) {
				if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("NACEKANJU"))) {
					statusi[0] += 1;
				} else if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))) {
					statusi[1] += 1;
				} else if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("ODBIJENA"))) {
					statusi[2] += 1;
				} else if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("OTKAZANA"))) {
					statusi[3] += 1;
				}
			}
		}
		System.out.println(datumOd + " " + datumDo);
		for (int s : statusi) {
			System.out.println("-> " + s);
		}
		return statusi;
	}

	public double zaradaSobe(LocalDate datumOd, LocalDate datumDo, String soba) {
		double suma = 0;
		for (Rezervacija r : mapaRezervacija.values()) {
			if (!r.getDatumOd().isAfter(datumDo) && !r.getDatumOd().isBefore(datumOd)) {
				if (r.getBrojSobe().equals(soba)) {
					if (r.getStatusRezervacije().equals(StatusRezervacije.valueOf("POTVRDJENA"))
							|| r.getStatusRezervacije().equals(StatusRezervacije.valueOf("OTKAZANA")))
						suma += r.getCena();
				}
			}
		}
		return suma;
	}

}

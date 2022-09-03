package menadzeri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import entiteti.Administrator;
import entiteti.DodatniKriterijumi;
import entiteti.Gost;
import entiteti.Korisnik;
import entiteti.Recepcioner;
import entiteti.Rezervacija;
import entiteti.Sobarica;
import entiteti.StrucnaSprema;
import entiteti.Zaposleni;

public class KorisniciManager implements Manager {
	private static KorisniciManager instance = null;
	private final String FILE = "korisnici.txt";
//	ArrayList<Korisnik> listaKorisnika;
	private HashMap<String, Korisnik> mapaKorisnika;
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	public ArrayList<Korisnik> listaKorisnika;
	public ArrayList<Zaposleni> listaZaposlenih;
	public ArrayList<Korisnik> listaGostiju;
	public ArrayList<Zaposleni> listaAdmina;
	public ArrayList<Zaposleni> listaSobarica;
	public ArrayList<Zaposleni> listaRecepcionera;

	private KorisniciManager() throws IOException {
		super();
//		this.listaKorisnika = new ArrayList<Korisnik>();
		this.mapaKorisnika = new HashMap<String, Korisnik>();
		ucitaj();
		this.listaKorisnika = getListaKorisnika();
		this.listaZaposlenih = getZaposleni();
		this.listaGostiju = getGosti();
		this.listaAdmina = getAdministratori();
		this.listaSobarica = getSobarice();
		this.listaRecepcionera = getRecepcioneri();
	}

	public static KorisniciManager getInstance() throws IOException {
		if (instance == null) {
			instance = new KorisniciManager();
		}
		return instance;
	}

//  radi i za ime i za prezime
	public boolean proveriIme(String ime) {
		if (ime.matches("[a-zA-Z�?ćšžđČĆŠŽ�?]+")) {
			return true;
		}
		return false;
	}

	public boolean proveriPol(String pol) {
		if (pol.equals("muškarac") || pol.equals("žena") || pol.equals("muskarac") || pol.equals("zena")) {
			return true;
		}
		return false;
	}

//	nije provereno jos uvek -> sustinski isti prinicip za proveru datuma kod rezervacije
	public boolean proveriDatumRodjenja(String datum) {
		try {
			LocalDate dt = LocalDate.parse(datum, formater);
			LocalDate danas = LocalDate.now();
			int godina = danas.getYear() - 18;
			danas = danas.withYear(godina);
			if (dt.isBefore(danas)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean proveriTelefon(String telefon) {
		String sablon = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
		if (telefon.matches(sablon)) {
			return true;
		}
		return false;
	}

	public boolean proveriAdresu(String adresa)

	{
		String[] adr = adresa.split(" ");
		for (int i = 0; i < adr.length; i++) {
			if (adr[i].matches("[a-zA-Z�?ćšžđČĆŠŽ�?]+") && i != adr.length - 1) {
				continue;
			} else if (i == adr.length - 1 && adr[i].matches("[0-9]+")) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

//  zapravo email
	public boolean proveriKorisnickoIme(String korisnickoIme) {
		String sablon = "^[A-Za-z0-9+_.-]+@(.+)$";
		if (korisnickoIme.matches(sablon) && !mapaKorisnika.containsKey(korisnickoIme)) {
			return true;
		}
		return false;
	}

//	zapravo broj pasosa -> mora se isto proveriti
	public boolean proveriLozinku(String lozinka) {
		if (lozinka.matches("[0-9]+") && lozinka.length() == 9) {
			return true;
		}
		return false;
	}

	public boolean proveriStrucnuSpremu(String strucnaSprema) {
		if (StrucnaSprema.contains(strucnaSprema)) {
			return true;
		}
		return false;
	}

	public boolean proveriStaz(String staz) {
		try {
			int s = Integer.parseInt(staz);
			if (s > 0 && s < 40) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean proveriUlogu(String uloga) {
		if (uloga.equals("Administrator") || uloga.equals("Recepcioner") || uloga.equals("Sobarica")
				|| uloga.equals("Gost")) {
			return true;
		}
		return false;
	}

	public boolean proveriZaposlenost(String uloga) {
		if (uloga.equals("Administrator") || uloga.equals("Recepcioner") || uloga.equals("Sobarica")) {
			return true;
		}
		return false;
	}
	
	public void dodeliSobu(String brojSobe) {
		Sobarica najslabijaSobarica = null;
		int min = 0;
		for (Korisnik sobarica : mapaKorisnika.values()) {
			if (sobarica instanceof Sobarica) {
				if (((Sobarica) sobarica).getListaSoba().size() <= min) {
					min = ((Sobarica) sobarica).getListaSoba().size();
					najslabijaSobarica = ((Sobarica) sobarica);
				}
			}
		}
		najslabijaSobarica.getListaSoba().add(brojSobe);
		try {
			azuriraj();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean daLiPostojiKorisnik(String korisnickoIme) {
		return mapaKorisnika.containsKey(korisnickoIme);
	}

	public Korisnik getKorisnik(String korisnickoIme) {
		return mapaKorisnika.get(korisnickoIme);
	}

	public HashMap<String, Korisnik> getKorisnici() {
		return this.mapaKorisnika;
	}

	public ArrayList<Korisnik> getListaKorisnika() {
		ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();
		for (Korisnik k : mapaKorisnika.values()) {
			korisnici.add(k);
		}
		return korisnici;
	}

	public ArrayList<Zaposleni> getZaposleni() {
		ArrayList<Zaposleni> zaposleni = new ArrayList<Zaposleni>();
		for (Korisnik k : mapaKorisnika.values()) {
			if (k instanceof Zaposleni) {
				zaposleni.add((Zaposleni) k);
			}
		}
		return zaposleni;
	}

	public ArrayList<Korisnik> getGosti() {
		ArrayList<Korisnik> gosti = new ArrayList<Korisnik>();
		for (Korisnik k : mapaKorisnika.values()) {
			if (k instanceof Gost) {
				gosti.add((Gost) k);
			}
		}
		return gosti;
	}

	public ArrayList<Zaposleni> getSobarice() {
		ArrayList<Zaposleni> sobarice = new ArrayList<Zaposleni>();
		for (Korisnik s : mapaKorisnika.values()) {
			if (s instanceof Sobarica) {
				sobarice.add((Sobarica) s);
			}
		}
		return sobarice;
	}

	public ArrayList<Zaposleni> getRecepcioneri() {
		ArrayList<Zaposleni> recepcioneri = new ArrayList<Zaposleni>();
		for (Korisnik r : mapaKorisnika.values()) {
			if (r instanceof Recepcioner) {
				recepcioneri.add((Recepcioner) r);
			}
		}
		return recepcioneri;
	}

	public ArrayList<Zaposleni> getAdministratori() {
		ArrayList<Zaposleni> administratori = new ArrayList<Zaposleni>();
		for (Korisnik r : mapaKorisnika.values()) {
			if (r instanceof Administrator) {
				administratori.add((Administrator) r);
			}
		}
		return administratori;
	}

	@Override
	public void azurirajListe() {
		this.listaAdmina = getAdministratori();
		this.listaGostiju = getGosti();
		this.listaKorisnika = getListaKorisnika();
		this.listaSobarica = getSobarice();
		this.listaRecepcionera = getRecepcioneri();
		this.listaZaposlenih = getZaposleni();
	}

//	private boolean proveriPlatu(String plata) {
//		ne bi trebala da postoji provera jer se azurira po formuli
//	}
	@Override
	public void ucitaj() throws IOException {

		String sp = System.getProperty("file.separator");
		BufferedReader br = new BufferedReader(new FileReader("." + sp + this.FILE));
		String linija;
		LocalDate datum;
		while ((linija = br.readLine()) != null) {
			if (!linija.startsWith("#") && !linija.isEmpty()) {
				if (linija.startsWith("?")) {
					String[] sobe = linija.split("/"); 
					ArrayList<String> listaSoba = new ArrayList<String>();
					try {
						for (int i = 2; i < sobe.length; i++) {
							listaSoba.add((sobe[i]));
//							System.out.println(dodatniKriterijumi[i]);
						}
						Sobarica s = (Sobarica)mapaKorisnika.get(sobe[1]);
						s.setListaSoba(listaSoba);
					} catch (Exception e) {
						System.out.println("Nema vise elemenata");
					}
				} else {
					String[] korisnici = linija.split("/");
					datum = LocalDate.parse(korisnici[4], formater);

					if (korisnici[0].equals("gost")) {
						Gost g = new Gost(korisnici[1], korisnici[2], korisnici[3], datum, korisnici[5], korisnici[6],
								korisnici[7], korisnici[8]);
						mapaKorisnika.put(korisnici[7], g);
//						listaKorisnika.add(g);

					} else if (korisnici[0].equals("administrator")) {
						int staz = Integer.parseInt(korisnici[10]);
						Administrator a = new Administrator(korisnici[1], korisnici[2], korisnici[3], datum,
								korisnici[5], korisnici[6], korisnici[7], korisnici[8], korisnici[9], staz);
						mapaKorisnika.put(korisnici[7], a);
//						listaZaposlenih.add(a);
//						listaKorisnika.add(a);

					} else if (korisnici[0].equals("recepcioner")) {
						int staz = Integer.parseInt(korisnici[10]);
						Recepcioner r = new Recepcioner(korisnici[1], korisnici[2], korisnici[3], datum, korisnici[5],
								korisnici[6], korisnici[7], korisnici[8], korisnici[9], staz);
						mapaKorisnika.put(korisnici[7], r);
//						listaZaposlenih.add(r);
//						listaKorisnika.add(r);

					} else if (korisnici[0].equals("sobarica")) {
						int staz = Integer.parseInt(korisnici[10]);
						Sobarica s = new Sobarica(korisnici[1], korisnici[2], korisnici[3], datum, korisnici[5],
								korisnici[6], korisnici[7], korisnici[8], korisnici[9], staz, null);
						mapaKorisnika.put(korisnici[7], s);
//						listaZaposlenih.add(s);
//						listaKorisnika.add(s);

					}
				}
			} else {
//				linija je komentar ako nije je usla u else granu provere 
			}

		}
		br.close();

	}

//  funkcija za brisanje korisnika iz mape -> prilikom gasenja programa se azurira txt
	@Override
	public void obrisi(String korisnickoIme) {
		mapaKorisnika.remove(korisnickoIme);
	}

//	funkcija za dodavanje korisnika u sistem (u mapu i fajl)
	@Override
	public void dodaj(Object o, boolean dodajMapu) throws IOException {
		if (o instanceof Korisnik) {
			Korisnik k = (Korisnik) o;
			if (dodajMapu) {
				mapaKorisnika.put(k.korisnickoIme, k);
			}
			String sp = System.getProperty("file.separator");
			PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE, true));
			pw.write(k.stringZaFile());
			pw.close();
		} else {
			throw new IllegalArgumentException("Prosledjeni objekat nije klase Korisnik");
		}
	}

//	funkcija za izmenu podataka pojedinacnog korisnika -> mozda bespotrebno ovako
	@Override
	public void izmeni(Object o) throws IOException {
		Korisnik novi = (Korisnik) o;
		mapaKorisnika.put(novi.korisnickoIme, novi);
		azuriraj();

//		skontaj jos kako ces ovo realizovati 
	}

//	funkjcija koja ce prilikom gasenja programa da azurira tekstualni fajl
	@Override
	public void azuriraj() throws IOException {
		String sp = System.getProperty("file.separator");
		PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE));
		pw.write("");
		pw.close();
		for (Korisnik k : mapaKorisnika.values()) {
			dodaj(k, false);
		}

	}

	@Override
	public void ispisi() throws IOException {
		for (Korisnik k : mapaKorisnika.values()) {
			System.out.println(k.stringZaFile());
		}
	}

	@Override
	public Object get(String kljuc) {
		return mapaKorisnika.get(kljuc);
	}

//	promeniti mozda u bulijan umesto stringa, ali to kasnije kontaj
//	problem kod promene u bulijan je to sto necu moci da napisem sta je tacno greska 
//	ali ce omoguciti jednostavnost MOZDA
	public String login(String korisnickoIme, String lozinka) {
		Korisnik k = mapaKorisnika.get(korisnickoIme);
		if (k == null) {
			return "Neispravno korisni�?ko ime";
		}
		if (!k.getLozinka().equals(lozinka)) {
			return "Nesipravna lozinka";
		}
		return "Uspešno prijavljivanje";
	}

	public Korisnik test() {
		Korisnik k = mapaKorisnika.get("rade123");
		obrisi("rade123");
		return k;
	}

}

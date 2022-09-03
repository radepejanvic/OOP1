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

import entiteti.Cenovnik;
import entiteti.Rezervacija;

public class CenovniciManager implements Manager {
	private static CenovniciManager instance = null;
	private final String FILE = "cenovnici.txt";
	private HashMap<String, Cenovnik> mapaCenovnika;
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

	private CenovniciManager() throws IOException {
		super();
		this.mapaCenovnika = new HashMap<String, Cenovnik>();
		ucitaj();
	}

	public static CenovniciManager getInstance() throws IOException {
		if (instance == null) {
			instance = new CenovniciManager();
		}
		return instance;
	}

	public boolean proveriSifruCenovnika(String sifraCenovnika) {
		if (sifraCenovnika.matches("[0-9]+") && sifraCenovnika.length() == 3
				&& !mapaCenovnika.containsKey(sifraCenovnika)) {
			return true;
		}
		return false;
	}

	public boolean proveriDatumDo(String datumOd, String datumDo) {
		try {
			LocalDate dtOd = LocalDate.parse(datumOd, formater);
			LocalDate dtDo = LocalDate.parse(datumDo, formater);
			if (dtOd.isBefore(dtDo)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean proveriDatume(String datumOd, String datumDo, Cenovnik cen) {
		if (proveriDatumDo(datumOd, datumDo)) {
			LocalDate dOd = LocalDate.parse(datumOd, formater);
			LocalDate dDo = LocalDate.parse(datumDo, formater);
			for (Cenovnik c : mapaCenovnika.values()) {
				if (dOd.isAfter(c.getDatumOd()) && dOd.isBefore(c.getDatumDo()) && !c.sifraCenovnika.equals("001")
						&& !c.equals(cen)) {
					return false;
				}
			}
			return true;

		} else {
			return false;
		}
	}

	public boolean proveriDouble(String broj) {
		try {
			if (Double.parseDouble(broj) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Cenovnik getCenovnik(String sifraCenovnika) {
		return mapaCenovnika.get(sifraCenovnika);
	}

	public ArrayList<Cenovnik> getCenovnici() {
		ArrayList<Cenovnik> cenovnici = new ArrayList<Cenovnik>();
		for (Cenovnik c : mapaCenovnika.values()) {
			cenovnici.add(c);
		}
		return cenovnici;
	}

	public Cenovnik izaberiCenovnik(Rezervacija r) {
		Cenovnik cenovnik = null;
		for (Cenovnik c : mapaCenovnika.values()) {
			if (c.uporediDatum(r.getDatumOd()) && !c.sifraCenovnika.equals("001")) {
				cenovnik = c;
				break;
			}
		}
		if (cenovnik == null) {
			cenovnik = mapaCenovnika.get("001");
		}
		return cenovnik;
	}

	@Override
	public void ucitaj() throws IOException {
		String sp = System.getProperty("file.separator");
		BufferedReader br = new BufferedReader(new FileReader("." + sp + this.FILE));
		String linija;
//		mozda ako treba datum
		LocalDate datumOd;
		LocalDate datumDo;
		while ((linija = br.readLine()) != null) {
			if (!linija.startsWith("#") && !linija.isEmpty()) {
				if (linija.startsWith("?")) {
					String[] cene = linija.split("/");
					HashMap<String, Double> mapa = new HashMap<String, Double>();
					for (int i = 2; i < cene.length; i++) {
						String[] par = cene[i].split("-");
						mapa.put(par[0], Double.parseDouble(par[1]));
					}
					Cenovnik c = mapaCenovnika.get(cene[1]);
					c.setCenovnik(mapa);
				} else {
					String[] cenovnici = linija.split("/");
					datumOd = LocalDate.parse(cenovnici[1], formater);
					datumDo = LocalDate.parse(cenovnici[2], formater);
					Cenovnik c = new Cenovnik(cenovnici[0], datumOd, datumDo, null);
					mapaCenovnika.put(cenovnici[0], c);
				}
			} else {
//				linija je komentar ako nije je usla u else granu provere 
			}
		}
		br.close();

	}

	@Override
	public void obrisi(String sifraCenovnika) throws IOException {
		mapaCenovnika.remove(sifraCenovnika);
	}

	@Override
	public void dodaj(Object o, boolean dodajMapu) throws IOException {
		if (o instanceof Cenovnik) {
			Cenovnik c = (Cenovnik) o;
			if (dodajMapu) {
				mapaCenovnika.put(c.sifraCenovnika, c);
			}
			String sp = System.getProperty("file.separator");
			PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE, true));
			pw.write(c.stringZaFile());
			pw.close();
		} else {
			throw new IllegalArgumentException("Prosledjeni objekat nije klase Cenovnik");
		}
	}

	@Override
	public void izmeni(Object o) throws IOException {
		Cenovnik nova = (Cenovnik) o;
		mapaCenovnika.put(nova.sifraCenovnika, nova);
		azuriraj();
	}

	@Override
	public void azuriraj() throws IOException {
		String sp = System.getProperty("file.separator");
		PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE));
		pw.write("");
		pw.close();
		for (Cenovnik c : mapaCenovnika.values()) {
			dodaj(c, false);
		}

	}

	@Override
	public void ispisi() throws IOException {
		for (Cenovnik c : mapaCenovnika.values()) {
			System.out.println(c.stringZaFile());
		}

	}

	@Override
	public Object get(String kljuc) {
		return mapaCenovnika.get(kljuc);

	}

	public Cenovnik test() {
		return mapaCenovnika.get("001");
	}

	@Override
	public void azurirajListe() {
		// TODO Auto-generated method stub

	}

}

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

import entiteti.DodatniKriterijumi;
import entiteti.Izvestaj;
import entiteti.Soba;

public class IzvestajiManager implements Manager {
	private static IzvestajiManager instance = null;
	private final String FILE = "izvestaji.txt";
	private HashMap<String, Izvestaj> mapaIzvestaja;
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

	private IzvestajiManager() throws IOException {
		super();
		this.mapaIzvestaja = new HashMap<String, Izvestaj>();
		ucitaj();
	}

	public static IzvestajiManager getInstance() throws IOException {
		if (instance == null) {
			instance = new IzvestajiManager();
		}
		return instance;
	}

	public boolean proveriDatume(String datumOd, String datumDo) {
		try {
			LocalDate dtOd = LocalDate.parse(datumOd, formater);
			LocalDate dtDo = LocalDate.parse(datumDo, formater);
			if (dtDo.isAfter(dtOd)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

//	TODO -> NAPISI SVE PROVERE -> DA LI SU BROJEVI I DA LI SU DATUMI I TAKO TO

	public Izvestaj getIzvestaj(String datum) {
		return mapaIzvestaja.get(datum);
	}

	@Override
	public void ucitaj() throws IOException {
		String sp = System.getProperty("file.separator");
		BufferedReader br = new BufferedReader(new FileReader("." + sp + this.FILE));
		String linija;
//		mozda ako treba datum
		while ((linija = br.readLine()) != null) {
			if (!linija.startsWith("#") && !linija.isEmpty()) {
				if (linija.startsWith("?")) {
					String[] sobariceSobe = linija.split("/");
					HashMap<String, Integer> ssMapa = new HashMap<String, Integer>();
					try {
						for (int i = 2; i < sobariceSobe.length; i++) {
							String[] par = sobariceSobe[i].split("-");
							ssMapa.put(par[0], Integer.parseInt(par[1]));
						}
					} catch (Exception e) {
						System.out.println("Nema vise elemenata");
					}
					Izvestaj i = mapaIzvestaja.get(sobariceSobe[1]);
					i.setSobariceSobe(ssMapa);
				} else {
					String[] izvestaji = linija.split("/");
					LocalDate datum = LocalDate.parse(izvestaji[0], formater);
					Izvestaj i = new Izvestaj(datum, Integer.parseInt(izvestaji[1]), Integer.parseInt(izvestaji[2]),
							null);
					mapaIzvestaja.put(izvestaji[0], i);
				}
			} else {
//				linija je komentar ako nije je usla u else granu provere 
			}

		}
		br.close();

	}

	@Override
	public void obrisi(String kljuc) throws IOException {
		mapaIzvestaja.remove(kljuc);
		azuriraj();
	}

	@Override
	public void dodaj(Object o, boolean dodajMapu) throws IOException {
		if (o instanceof Izvestaj) {
			Izvestaj i = (Izvestaj) o;
			if (dodajMapu) {
				mapaIzvestaja.put(i.getDatum(), i);
			}
			String sp = System.getProperty("file.separator");
			PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE, true));
			pw.write(i.stringZaFile());
			pw.close();
		} else {
			throw new IllegalArgumentException("Prosledjeni objekat nije klase Izvestaj");
		}
	}

	@Override
	public void izmeni(Object o) throws IOException {
		Izvestaj nova = (Izvestaj) o;
		mapaIzvestaja.put(nova.getDatum(), nova);
		azuriraj();
	}

	@Override
	public void azuriraj() throws IOException {
		String sp = System.getProperty("file.separator");
		PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE));
		pw.write("");
		pw.close();
		for (Izvestaj i : mapaIzvestaja.values()) {
			dodaj(i, false);
		}
	}

	@Override
	public void ispisi() throws IOException {
		for (Izvestaj i : mapaIzvestaja.values()) {
			System.out.println(i.stringZaFile());
		}
	}

	@Override
	public Object get(String kljuc) {
		return mapaIzvestaja.get(kljuc);
	}

	@Override
	public void azurirajListe() {
		// TODO Auto-generated method stub

	}

	public HashMap<String, Integer> prebrojSobariceSobe(LocalDate datumOd, LocalDate datumDo) {
		HashMap<String, Integer> mapa = new HashMap<String, Integer>();
		for (Izvestaj i : mapaIzvestaja.values()) {
			if (!i.datum.isBefore(datumOd) && !i.datum.isAfter(datumDo)) {
				for (String sobarica : i.getSobariceSobe().keySet()) {
					if (mapa.containsKey(sobarica)) {
						int brSoba = mapa.get(sobarica) + i.getSobariceSobe().get(sobarica);
						mapa.put(sobarica, brSoba);
					} else {
						mapa.put(sobarica, i.getSobariceSobe().get(sobarica));
					}

				}
			}
		}
		return mapa;
	}

	public int prebrojSobeJedneSobarice(LocalDate datumOd, LocalDate datumDo, String sobarica) {
		if (prebrojSobariceSobe(datumOd, datumDo).containsKey(sobarica)) {
			return prebrojSobariceSobe(datumOd, datumDo).get(sobarica);
		}
		return 0;
	}

	public void napraviDanasnjiIzvestaj() {
		LocalDate danas = LocalDate.now();
		if (!mapaIzvestaja.containsKey(danas.format(formater))) {
			Izvestaj i = new Izvestaj(danas, 0, 0, new HashMap<String, Integer>());
			mapaIzvestaja.put(danas.format(formater), i);
			try {
				azuriraj();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

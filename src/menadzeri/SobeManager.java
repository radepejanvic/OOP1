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

import entiteti.Administrator;
import entiteti.DodatniKriterijumi;
import entiteti.Gost;
import entiteti.Korisnik;
import entiteti.Recepcioner;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;
import entiteti.StatusSobe;
import entiteti.TipSobe;

public class SobeManager implements Manager {
	private static SobeManager instance = null;
	private final String FILE = "sobe.txt";
	private HashMap<String, Soba> mapaSoba;
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

	private SobeManager() throws IOException {
		super();
		this.mapaSoba = new HashMap<String, Soba>();
		ucitaj();
	}

	public static SobeManager getInstance() throws IOException {
		if (instance == null) {
			instance = new SobeManager();
		}
		return instance;
	}

	public boolean proveriBrojSobe(String brojSobe) {
		if (brojSobe.matches("[0-9]+") && brojSobe.length() == 3 && !mapaSoba.containsKey(brojSobe)) {
			return true;
		}
		return false;
	}

	public boolean proveriTipSobe(String tipSobe) {
		if (TipSobe.contains(tipSobe)) {
			return true;
		}
		return false;
	}

	public boolean proveriStatusSobe(String statusSobe) {
		if (StatusSobe.contains(statusSobe)) {
			return true;
		}
		return false;
	}

	public boolean daLiPostojiSoba(String brojSobe) {
		return mapaSoba.containsKey(brojSobe);
	}

	public Soba getSoba(String brojSobe) {
		return mapaSoba.get(brojSobe);
	}

	public ArrayList<Soba> getSobe() {
		ArrayList<Soba> sobe = new ArrayList<Soba>();
		for (Soba s : mapaSoba.values()) {
			sobe.add(s);
		}
		return sobe;
	}

	@Override
	public void ucitaj() throws IOException {
		String sp = System.getProperty("file.separator");
		BufferedReader br = new BufferedReader(new FileReader("." + sp + this.FILE));
		String linija;
//		mozda ako treba datum
		while ((linija = br.readLine()) != null) {
			if (!linija.startsWith("#") && !linija.isEmpty()) {
				if (linija.startsWith("??")) {
					String[] datumi = linija.split("/");
					ArrayList<LocalDate> datumiLista = new ArrayList<LocalDate>();
					for (int i = 2; i < datumi.length; i++) {
						LocalDate datum = LocalDate.parse(datumi[i], formater);
						datumiLista.add(datum);
					}
					Soba s = mapaSoba.get(datumi[1]);
					s.setListaDatuma(datumiLista);
				} else if (linija.startsWith("?")) {
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
					Soba s = mapaSoba.get(dodatniKriterijumi[1]);
					s.setDodatniKriterijumi(dk);
				} else {
					String[] sobe = linija.split("/");
					Soba s = new Soba(sobe[0], sobe[1], sobe[2], null, null, false);
					mapaSoba.put(sobe[0], s);
				}
			} else {
//				linija je komentar ako nije je usla u else granu provere 
			}

		}
		br.close();

	}

	@Override
	public void obrisi(String brojSobe) throws IOException {
		mapaSoba.remove(brojSobe);
		azuriraj();
	}

	@Override
	public void dodaj(Object o, boolean dodajMapu) throws IOException {
		if (o instanceof Soba) {
			Soba s = (Soba) o;
			if (dodajMapu) {
				mapaSoba.put(s.brojSobe, s);
			}
			String sp = System.getProperty("file.separator");
			PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE, true));
			pw.write(s.stringZaFile());
			pw.close();
		} else {
			throw new IllegalArgumentException("Prosledjeni objekat nije klase Soba");
		}

	}

	@Override
	public void izmeni(Object o) throws IOException {
		Soba nova = (Soba) o;
		mapaSoba.put(nova.brojSobe, nova);
		azuriraj();

	}

	@Override
	public void azuriraj() throws IOException {
		String sp = System.getProperty("file.separator");
		PrintWriter pw = new PrintWriter(new FileWriter("." + sp + FILE));
		pw.write("");
		pw.close();
		for (Soba s : mapaSoba.values()) {
			dodaj(s, false);
		}

	}

	@Override
	public void ispisi() throws IOException {
		for (Soba s : mapaSoba.values()) {
			System.out.println(s.stringZaFile());
		}

	}

	@Override
	public Object get(String kljuc) {
		return mapaSoba.get(kljuc);

	}

	public Soba test() throws IOException {
		return mapaSoba.get("101");
	}

	@Override
	public void azurirajListe() {
		// TODO Auto-generated method stub

	}

	public ArrayList<Soba> getListaPoKriterijumima(String tip, LocalDate datumOd, LocalDate datumDo, ArrayList<DodatniKriterijumi> dk) {
		ArrayList<Soba> sobe = new ArrayList<Soba>();
		for (Soba s : mapaSoba.values()) {
			sobe.add(s);
		}
//		TODO
//		return getFilterKriterijumi(getFilterDatumi(getFilterTip(tip), datumOd, datumDo), dk);
		return null;
	}

	public ArrayList<Soba> getFilterTip(String tip) {
		if (tip != null) {
			ArrayList<Soba> lista = new ArrayList<Soba>();
			for (Soba s : mapaSoba.values()) {
				if (s.getTipSobe().toString().equals(tip)) {
					lista.add(s);
				}
			}
			return lista;
		} else {
			return this.getSobe();
		}
	}

	public ArrayList<Soba> getFilterDatumi(ArrayList<Soba> lis, LocalDate datumOd, LocalDate datumDo) {
		if (datumOd != null && datumDo != null) {
			ArrayList<Soba> lista = new ArrayList<Soba>();
			ArrayList<LocalDate> datumi = new ArrayList<LocalDate>();
			while (!datumOd.isAfter(datumDo)) {
				datumi.add(datumOd);
				datumOd = datumOd.plusDays(1);
			}
			for (Soba s : lis) {
				int i = 0;
				for (LocalDate d : datumi) {
					if (!s.getListaDatuma().contains(d)) {
						i++;
						break;
					}
				}
				if (i == 0) {
					lista.add(s);
				}
			}
			return lista;
		} else {
			return lis;
		}
	}
//	TODO
	public ArrayList<Soba> getFilterKriterijumi(ArrayList<Soba> lis, ArrayList<DodatniKriterijumi> dk) {
		if (dk != null) {
			ArrayList<Soba> lista = new ArrayList<Soba>();
			for (Soba s : lis) {
				int i = 0;
				for (DodatniKriterijumi d : dk) {
					if (!s.getDodatniKriterijumi().contains(d)) {
						i++;
						break;
					}
				}
				if (i == 0) {
					lista.add(s);
				}
			}
			return lista;
		} else {
			return lis;
		}
	}
	
	public void azurirajSveDatume() {
		for (Soba s : mapaSoba.values()) {
			s.azurirajDatume();
		}
		try {
			azuriraj();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

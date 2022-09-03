package prikaz;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Zaposleni;
import menadzeri.Manager;
import menadzeri.RezervacijeManager;
import prikaz.dodajIzmeni.NovaRezervacija;
import prikaz.dodajIzmeni.RezervacijeDodajIzmeniDialog;

public class RezervacijeTableFrame extends TableFrame {
	private RezervacijeManager rm;
	private Korisnik gost;
	private JFrame roditelj;
	
	public RezervacijeTableFrame(JFrame roditelj, AbstractTableModel atm, Korisnik gost)
			throws IOException {
		super(roditelj, atm, RezervacijeManager.getInstance(), "Rezervacije");
		this.rm = RezervacijeManager.getInstance();
		this.gost = gost;
		this.roditelj = roditelj;
	}

	@Override
	public void initDodajIzmeniDialog(JFrame roditelj, Object o) {
		if (this.gost == null) {
			RezervacijeDodajIzmeniDialog rdid = new RezervacijeDodajIzmeniDialog(RezervacijeTableFrame.this, o, false);
			rdid.setVisible(true);
		} else {
			NovaRezervacija nr = new NovaRezervacija(RezervacijeTableFrame.this, this.gost);
			nr.setVisible(true);
		}
	}

	@Override
	public String imeArtikla(Object o) {
		Rezervacija r = (Rezervacija) o;
		return r.brojRezervacije;
	}
	
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {
		{
			put(0, 1);
			put(1, 1);
			put(2, 1);
			put(3, 1);
			put(4, 1);
			put(5, 1);
			put(6, 1);
			put(7, 1);
			put(8, 1);
			put(9, 1);
		}
	};
	
	@Override
	protected void sort(int index) {
		rm.getRezervacije().sort(new Comparator<Rezervacija>() {
			int retVal = 0;

			@Override
			public int compare(Rezervacija o1, Rezervacija o2) {
				switch (index) {
				case 0:
					retVal = o1.getClass().getName().compareTo(o2.getClass().getName());
					break;
				case 1:
					retVal = o1.brojRezervacije.compareTo(o2.brojRezervacije);
					break;
				case 2:
					retVal = o1.getGost().compareTo(o2.getGost());
					break;
				case 3:
					retVal = o1.getBrojSobe().compareTo(o2.getBrojSobe());
					break;
				case 4:
					retVal = o1.getDatumOd().compareTo(o2.getDatumOd());
					break;
				case 5:
					retVal = o1.getDatumDo().compareTo(o2.getDatumDo());
					break;
//				case 6:
//					retVal = o1.getDodatneUsluge().compareTo(o2.getDodatneUsluge());
//					break;
//				case 7:
//					retVal = o1.getDodatniKriterijumi().compareTo(o2.getDodatniKriterijumi());
//					break;
				case 8:
					retVal =((Double) o1.getCena()).compareTo((Double) o2.getCena());
					break;
				case 9:
					retVal = (o1.getStatusRezervacije()).compareTo(o2.getStatusRezervacije());
					break;
				default:
					System.out.println("Prosirena tabela");
					System.exit(1);
					break;
				}
				return retVal * sortOrder.get(index);
			}

		});

	}
	
	public JFrame getRoditelj() {
		return this.roditelj;
	}

}

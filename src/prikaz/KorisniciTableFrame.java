package prikaz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import entiteti.Gost;
import entiteti.Korisnik;
import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import model.KorisniciModel;
import prikaz.dodajIzmeni.KorisniciDodajIzmeniDialog;
import prikaz.dodajIzmeni.ZaposleniDodajIzmeniDialog;

public class KorisniciTableFrame extends TableFrame {
	private KorisniciManager km;

	public KorisniciTableFrame(JFrame roditelj, AbstractTableModel atm) throws IOException {
		super(roditelj, atm, KorisniciManager.getInstance(), "Korisnici");
		this.km = KorisniciManager.getInstance();
	}

	@Override
	public void initDodajIzmeniDialog(JFrame roditelj, Object o) {
		if (o == null || o instanceof Gost) {
			KorisniciDodajIzmeniDialog kdid = new KorisniciDodajIzmeniDialog(KorisniciTableFrame.this, o, -1, false);
			kdid.setVisible(true);
		} else {
			ZaposleniDodajIzmeniDialog zdid = new ZaposleniDodajIzmeniDialog(KorisniciTableFrame.this, o, -1, false);
			zdid.setVisible(true);
		}
	}

//	TODO extendovacu sa ovom klasom i overrajdovacu samo initDodajIzmeniDialogg metodu -> genijalan si
	@Override
	public String imeArtikla(Object o) {
		Korisnik k = (Korisnik) o;
		return k.getIme() + " " + k.getPrezime();
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
		}
	};

	@Override
	protected void sort(int index) {
		km.getListaKorisnika().sort(new Comparator<Korisnik>() {
			int retVal = 0;

			@Override
			public int compare(Korisnik o1, Korisnik o2) {
				switch (index) {
				case 0:
					retVal = o1.getClass().getName().compareTo(o2.getClass().getName());
					break;
				case 1:
					retVal = o1.korisnickoIme.compareTo(o2.korisnickoIme);
					break;
				case 2:
					retVal = o1.getIme().compareTo(o2.getIme());
					break;
				case 3:
					retVal = o1.getPrezime().compareTo(o2.getPrezime());
					break;
				case 4:
					retVal = o1.getPol().compareTo(o2.getPol());
					break;
				case 5:
					retVal = o1.getDatumRodjenja().compareTo(o2.getDatumRodjenja());
					break;
				case 6:
					retVal = o1.getTelefon().compareTo(o2.getTelefon());
					break;
				case 7:
					retVal = o1.getAdresa().compareTo(o2.getAdresa());
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
}

package prikaz;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import entiteti.Cenovnik;
import entiteti.Zaposleni;
import menadzeri.CenovniciManager;
import menadzeri.KorisniciManager;
import menadzeri.Manager;
import prikaz.dodajIzmeni.CenovniciDodajIzmeniDialog;

public class CenovniciTableFrame extends TableFrame {
	private CenovniciManager cm;
	
	public CenovniciTableFrame(JFrame roditelj, AbstractTableModel atm)
			throws IOException {
		super(roditelj, atm, CenovniciManager.getInstance(), "Cenovnici");
		this.cm = CenovniciManager.getInstance();
	}

	@Override
	public void initDodajIzmeniDialog(JFrame roditelj, Object o) {
		CenovniciDodajIzmeniDialog cdid = new CenovniciDodajIzmeniDialog(CenovniciTableFrame.this, o);
		cdid.setVisible(true);
	}

	@Override
	public String imeArtikla(Object o) {
		Cenovnik c = (Cenovnik) o;
		return c.sifraCenovnika;
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
			put(10, 1);
			put(11, 1);
		}
	};

	@Override
	protected void sort(int index) {
		cm.getCenovnici().sort(new Comparator<Cenovnik>() {
			int retVal = 0;

			@Override
			public int compare(Cenovnik o1, Cenovnik o2) {
				switch (index) {
				case 0:
					retVal = o1.getClass().getName().compareTo(o2.getClass().getName());
					break;
				case 1:
					retVal = o1.sifraCenovnika.compareTo(o2.sifraCenovnika);
					break;
				case 2:
					retVal = o1.getDatumOd().compareTo(o2.getDatumOd());
					break;
				case 3:
					retVal = o1.getDatumDo().compareTo(o2.getDatumDo());
					break;
				case 4:
					retVal = o1.getCenovnik().get("JEDNOKREVETNA").compareTo(o2.getCenovnik().get("JEDNOKREVETNA"));
					break;
				case 5:
					retVal = o1.getCenovnik().get("DVOKREVETNA1").compareTo(o2.getCenovnik().get("DVOKREVETNA1"));
					break;
				case 6:
					retVal = o1.getCenovnik().get("DVOKREVETNA2").compareTo(o2.getCenovnik().get("DVOKREVETNA2"));
					break;
				case 7:
					retVal = o1.getCenovnik().get("TROKREVETNA").compareTo(o2.getCenovnik().get("TROKREVETNA"));
					break;
				case 8:
					retVal = o1.getCenovnik().get("CETVOROKREVETNA").compareTo(o2.getCenovnik().get("CETVOROKREVETNA"));
					break;
				case 9:
					retVal = o1.getCenovnik().get("DORUCAK").compareTo(o2.getCenovnik().get("DORUCAK"));
					break;
				case 10:
					retVal = o1.getCenovnik().get("RUCAK").compareTo(o2.getCenovnik().get("RUCAK"));
					break;
				case 11:
					retVal = o1.getCenovnik().get("VECERA").compareTo(o2.getCenovnik().get("VECERA"));
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

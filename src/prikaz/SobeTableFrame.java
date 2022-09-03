package prikaz;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

import entiteti.Soba;
import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import menadzeri.Manager;
import menadzeri.SobeManager;
import prikaz.dodajIzmeni.SobeDodajIzmeniDialog;

public class SobeTableFrame extends TableFrame {
	private SobeManager sm;
	private int izbor;
	private boolean zakucaj;

	public SobeTableFrame(JFrame roditelj, AbstractTableModel atm, int izbor, boolean zakucaj) throws IOException {
		super(roditelj, atm, SobeManager.getInstance(), "Sobe");
		this.izbor = izbor;
		this.zakucaj = zakucaj;
	}

	@Override
	public void initDodajIzmeniDialog(JFrame roditelj, Object o) {
		SobeDodajIzmeniDialog sdid = new SobeDodajIzmeniDialog(SobeTableFrame.this, o, izbor, zakucaj);
		sdid.setVisible(true);

	}

	@Override
	public String imeArtikla(Object o) {
		Soba s = (Soba) o;
		return s.brojSobe + " " + s.getTipSobe();
	}

	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {
		{
			put(0, 1);
			put(1, 1);
			put(2, 1);
			put(3, 1);
			put(4, 1);
		}
	};

	@Override
	protected void sort(int index) {
		sm.getSobe().sort(new Comparator<Soba>() {
			int retVal = 0;

			@Override
			public int compare(Soba o1, Soba o2) {
				switch (index) {
				case 0:
					retVal = o1.getClass().getName().compareTo(o2.getClass().getName());
					break;
				case 1:
					retVal = o1.getTipSobe().compareTo(o2.getTipSobe());
					break;
				case 2:
					retVal = o1.brojSobe.compareTo(o2.brojSobe);
					break;
				case 4:
					retVal = o1.getStatusSobe().compareTo(o2.getStatusSobe());
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

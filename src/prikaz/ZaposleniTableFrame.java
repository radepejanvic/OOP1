package prikaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import model.ZaposleniModel;
import prikaz.dodajIzmeni.ZaposleniDodajIzmeniDialog;

public class ZaposleniTableFrame extends TableFrame {
	private KorisniciManager km;
	private int izbor;
	private boolean zakucaj;

	public ZaposleniTableFrame(JFrame roditelj, AbstractTableModel atm, int izbor, boolean zakucaj) throws IOException {
		super(roditelj, atm, KorisniciManager.getInstance(), "Zaposleni");
		this.km = KorisniciManager.getInstance();
		this.izbor = izbor;
		this.zakucaj = zakucaj;
	}

	@Override
	public void initDodajIzmeniDialog(JFrame roditelj, Object o) {
		ZaposleniDodajIzmeniDialog zdid = new ZaposleniDodajIzmeniDialog(ZaposleniTableFrame.this, o, izbor, zakucaj);
		zdid.setVisible(true);
	}

//	TODO extendovacu sa ovom klasom i overrajdovacu samo initDodajIzmeniDialogg metodu -> genijalan si
	@Override
	public String imeArtikla(Object o) {
		Zaposleni z = (Zaposleni) o;
		return z.getIme() + " " + z.getPrezime();
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
		}
	};

	@Override
	protected void sort(int index) {
		km.getZaposleni().sort(new Comparator<Zaposleni>() {
			int retVal = 0;

			@Override
			public int compare(Zaposleni o1, Zaposleni o2) {
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
				case 8:
					retVal = o1.getStrucnaSprema().compareTo(o2.getStrucnaSprema());
					break;
				case 9:
					retVal = ((Integer) o1.getStaz()).compareTo((Integer) o2.getStaz());
					break;
				case 10:
					retVal = ((Double) o1.getPlata()).compareTo((Double) o2.getPlata());
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
package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import entiteti.DodatneUsluge;
import entiteti.DodatniKriterijumi;
import entiteti.Rezervacija;
import entiteti.Soba;
import menadzeri.KorisniciManager;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;
import net.miginfocom.swing.MigLayout;
import prikaz.TableFrame;

public class RezervacijeDodajIzmeniDialog extends DodajIzmeniDialog {
	private RezervacijeManager rm;
	private Rezervacija r;
	private boolean racunajCenu;
	
	public RezervacijeDodajIzmeniDialog(JFrame roditelj, Object izmena, boolean racunajCenu) {
		super(roditelj, izmena);
		this.r = (Rezervacija) izmena;
		try {
			this.rm = RezervacijeManager.getInstance();
		} catch (IOException e) {
			System.out.println("Greska kod konstruktora rezervacija dodaj izmeni dialog");
			e.printStackTrace();
		}

		this.racunajCenu = racunajCenu;
		initGUI(1, false);
		pack();
	}

	@Override
	protected void initGUI(int selektovaniIndex, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblBrojRezervacije = new JLabel("Broj rezervacije");
		add(lblBrojRezervacije);

		JTextField txtBrojRezervacije = new JTextField(20);
		add(txtBrojRezervacije, "span 2");

		JLabel lblGost = new JLabel("Gost");
		add(lblGost);

		JTextField txtGost = new JTextField(20);
		add(txtGost, "span 2");

		JLabel lblSoba = new JLabel("Soba");
		add(lblSoba);

		JTextField txtSoba = new JTextField(20);
		add(txtSoba, "span 2");

		JLabel lblDatumOd = new JLabel("Datum od");
		add(lblDatumOd);
//		ovo zameni sa JDateChooser-om
		JTextField txtDatumOd = new JTextField(20);
		add(txtDatumOd, "span 2");

		JLabel lblDatumDo = new JLabel("Datum do");
		add(lblDatumDo);
//		ovo zameni sa JDateChooser-om
		JTextField txtDatumDo = new JTextField(20);
		add(txtDatumDo, "span 2");
//		dodaj datume x 2
//		dodaj dodatne kriterijume i usluge u formi list boxa
//      cena se sama preracunava automatski
//		status automatski "NA CEKANJU"
		JLabel lblDodatneUsluge = new JLabel("Dodatne usluge");
		add(lblDodatneUsluge);

		DefaultListModel<String> dlm1 = new DefaultListModel<String>();
		dlm1.addElement("DORUCAK");
		dlm1.addElement("RUCAK");
		dlm1.addElement("VECERA");
		JList<String> dodatneUsluge = new JList<String>();
		dodatneUsluge.setModel(dlm1);
		add(dodatneUsluge, "span 2");

		JLabel lblDodatniKriterijumi = new JLabel("Dodatni kriterijumi");
		add(lblDodatniKriterijumi);

		DefaultListModel<String> dlm2 = new DefaultListModel<String>();
		dlm2.addElement("WIFI");
		dlm2.addElement("KLIMA");
		dlm2.addElement("KADA");
		dlm2.addElement("TUSKABINA");
		dlm2.addElement("TERASA");
		JList<String> dodatniKriterijumi = new JList<String>();
		dodatniKriterijumi.setModel(dlm2);
		add(dodatniKriterijumi, "span 2");

		JLabel lblCena = new JLabel("Cena");
		add(lblCena);

		JTextField txtCena = new JTextField(20);
		add(txtCena, "span 2");

		JLabel lblStatus = new JLabel("Status rezervcije");
		add(lblStatus);

		String[] statusi = { "NACEKANJU", "POTVRDJENA", "OTKAZANA", "ODBIJENA" };
		JComboBox status = new JComboBox(statusi);
		status.setSelectedIndex(-1);
		add(status, "span 2");
		add(new JLabel());

		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Potvrdi");
		add(btnPotvrdi);

		if (this.r != null) {
			txtBrojRezervacije.setText(r.brojRezervacije);
			txtGost.setText(r.getGost());
			txtSoba.setText(r.getBrojSobe());
			txtDatumOd.setText(r.getDatumOd().format(rm.formater));
			txtDatumDo.setText(r.getDatumDo().format(rm.formater));
			
			
			System.out.println(r.getDodatneUsluge());
			dodatneUsluge.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			int[] indexi1 = { -1, -1, -1 };
			int i = 0;
			for (DodatneUsluge du : r.getDodatneUsluge()) {
				indexi1[i] = dlm1.indexOf(du.toString());
				i++;
			}
			dodatneUsluge.setSelectedIndices(indexi1);

			System.out.println(r.getDodatniKriterijumi());
			dodatniKriterijumi.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			int[] indexi2 = { -1, -1, -1, -1, -1 };
			i = 0;
			for (DodatniKriterijumi dk : r.getDodatniKriterijumi()) {
				indexi2[i] = dlm2.indexOf(dk.toString());
				i++;
			}
			dodatniKriterijumi.setSelectedIndices(indexi2);

			txtCena.setText(r.getCena() + "");
			status.setSelectedItem(r.getStatusRezervacije() + "");
		} else {
			status.setSelectedItem("NACEKANJU");
			status.setEnabled(false);
			txtCena.setEnabled(false);
		}

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String brojRezervacije = txtBrojRezervacije.getText().trim();
				String gost = txtGost.getText().trim();
				String soba = txtSoba.getText().trim();
				String datumOd = txtDatumOd.getText().trim();
				String datumDo = txtDatumDo.getText().trim();
				int[] dodUsl = dodatneUsluge.getSelectedIndices();
				ArrayList<DodatneUsluge> listaDU = new ArrayList<DodatneUsluge>();
				for (int i : dodUsl) {
					listaDU.add(DodatneUsluge.valueOf(dlm1.get(i)));
				}

				int[] dodKrit = dodatniKriterijumi.getSelectedIndices();
				ArrayList<DodatniKriterijumi> listaDK = new ArrayList<DodatniKriterijumi>();
				for (int i : dodKrit) {
					listaDK.add(DodatniKriterijumi.valueOf(dlm2.get(i)));
				}
				String sStatus = (String) status.getSelectedItem();
				System.out.println(sStatus);
				String cena = txtCena.getText().trim();

				if (!rm.proveriBrojRezervacije(brojRezervacije)) {
					JOptionPane.showMessageDialog(null, "Neispravan broj rezervacije", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriGosta(gost)) {
					JOptionPane.showMessageDialog(null, "Neispravan gost", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriSobu(soba)) {
					JOptionPane.showMessageDialog(null, "Neispravan broj sobe", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriDatumOd(datumOd)) {
					JOptionPane.showMessageDialog(null, "Neispravan po�?etni datum", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriDatumDo(datumOd, datumDo)) {
					JOptionPane.showMessageDialog(null, "Neispravan krajnji datum", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriStatusRezervacije(sStatus + "")) {
					JOptionPane.showMessageDialog(null, "Neispravan status rezervacije", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriCenu(cena)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					Rezervacija novaRezervacija;
					if (r != null) {
						double c = 0;
						if (RezervacijeDodajIzmeniDialog.this.racunajCenu) {
							c = 0;
						} else {
							c =  Double.parseDouble(cena);
						}
						novaRezervacija = new Rezervacija(brojRezervacije, gost, soba,
								LocalDate.parse(datumOd, rm.formater), LocalDate.parse(datumDo, rm.formater), listaDU,
								listaDK, c, sStatus);
//						
						try {
							rm.izmeni(novaRezervacija);
							JOptionPane.showMessageDialog(null,
									"Rezervacija " + novaRezervacija.brojRezervacije + " uspešno izmenjena.");
							RezervacijeDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
						} catch (IOException e1) {
							System.out.println("Greska kod izmene rezervacije");
							e1.printStackTrace();
						}

					} else {
						novaRezervacija = new Rezervacija(brojRezervacije, gost, soba,
								LocalDate.parse(datumOd, rm.formater), LocalDate.parse(datumDo, rm.formater), listaDU,
								listaDK, 0, sStatus);
						try {
							rm.dodaj(novaRezervacija, true);
							JOptionPane.showMessageDialog(null,
									"Rezervacija " + novaRezervacija.brojRezervacije + " uspešno dodata.");
							RezervacijeDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
						} catch (Exception e1) {
							System.out.println("Greska kod dodavanja rezervacije");
							e1.printStackTrace();
						}
					}
				}
			}

		});

		btnOtkazi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RezervacijeDodajIzmeniDialog.this.dispose();
			}

		});
	}
}

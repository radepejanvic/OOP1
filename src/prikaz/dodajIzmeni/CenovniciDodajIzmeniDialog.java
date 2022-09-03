package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entiteti.Cenovnik;
import entiteti.Rezervacija;
import menadzeri.CenovniciManager;
import net.miginfocom.swing.MigLayout;
import prikaz.TableFrame;

public class CenovniciDodajIzmeniDialog extends DodajIzmeniDialog {
	private CenovniciManager cm;
	private Cenovnik c;

	public CenovniciDodajIzmeniDialog(JFrame roditelj, Object izmena) {
		super(roditelj, izmena);
		this.c = (Cenovnik) izmena;
		try {
			this.cm = CenovniciManager.getInstance();
		} catch (IOException e) {
			System.out.println("Greska kod konstruktora cenovnici dodaj izmeni dialog");
			e.printStackTrace();
		}
		initGUI(1, false);
		pack();
	}

	@Override
	protected void initGUI(int selektovaniIndex, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblSifraCenovnika = new JLabel("Šifra cenovnika");
		add(lblSifraCenovnika);

		JTextField txtSifraCenovnika = new JTextField(20);
		add(txtSifraCenovnika, "span 2");

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

		JLabel lblJednokrevetna = new JLabel("JEDNOKREVETNA");
		add(lblJednokrevetna);

		JTextField txtJednokrevetna = new JTextField(20);
		add(txtJednokrevetna, "span 2");

		JLabel lblDvokrevetna1 = new JLabel("DVOKREVETNA1");
		add(lblDvokrevetna1);

		JTextField txtDvokrevetna1 = new JTextField(20);
		add(txtDvokrevetna1, "span 2");

		JLabel lblDvokrevetna2 = new JLabel("DVOKREVETNA2");
		add(lblDvokrevetna2);

		JTextField txtDvokrevetna2 = new JTextField(20);
		add(txtDvokrevetna2, "span 2");

		JLabel lblTrokrevetna = new JLabel("TROKREVETNA");
		add(lblTrokrevetna);

		JTextField txtTrokrevetna = new JTextField(20);
		add(txtTrokrevetna, "span 2");

		JLabel lblCetvorokrevetna = new JLabel("CETVOROKREVETNA");
		add(lblCetvorokrevetna);

		JTextField txtCetvorokrevetna = new JTextField(20);
		add(txtCetvorokrevetna, "span 2");

		JLabel lblDorucak = new JLabel("DORUCAK");
		add(lblDorucak);

		JTextField txtDorucak = new JTextField(20);
		add(txtDorucak, "span 2");

		JLabel lblRucak = new JLabel("RUCAK");
		add(lblRucak);

		JTextField txtRucak = new JTextField(20);
		add(txtRucak, "span 2");

		JLabel lblVecera = new JLabel("VECERA");
		add(lblVecera);

		JTextField txtVecera = new JTextField(20);
		add(txtVecera, "span 2");

		add(new JLabel());
		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Potvrdi");
		add(btnPotvrdi);

		if (c != null) {
			txtSifraCenovnika.setText(c.sifraCenovnika);
			txtSifraCenovnika.setEditable(false);
			txtDatumOd.setText(c.getDatumOd().format(cm.formater));
			txtDatumDo.setText(c.getDatumDo().format(cm.formater));
			txtJednokrevetna.setText(c.getCenovnik().get("JEDNOKREVETNA") + "");
			txtDvokrevetna1.setText(c.getCenovnik().get("DVOKREVETNA1") + "");
			txtDvokrevetna2.setText(c.getCenovnik().get("DVOKREVETNA2") + "");
			txtTrokrevetna.setText(c.getCenovnik().get("TROKREVETNA") + "");
			txtCetvorokrevetna.setText(c.getCenovnik().get("CETVOROKREVETNA") + "");
			txtDorucak.setText(c.getCenovnik().get("DORUCAK") + "");
			txtRucak.setText(c.getCenovnik().get("RUCAK") + "");
			txtVecera.setText(c.getCenovnik().get("VECERA") + "");
		} else {

		}

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String sifraCenovnika = txtSifraCenovnika.getText().trim();
				String datumOd = txtDatumOd.getText().trim();
				String datumDo = txtDatumDo.getText().trim();
				String jed = txtJednokrevetna.getText().trim();
				String dvo1 = txtDvokrevetna1.getText().trim();
				String dvo2 = txtDvokrevetna2.getText().trim();
				String tro = txtTrokrevetna.getText().trim();
				String cet = txtCetvorokrevetna.getText().trim();
				String dorucak = txtDorucak.getText().trim();
				String rucak = txtRucak.getText().trim();
				String vecera = txtVecera.getText().trim();

//				/* ??if (!cm.) */
				
				if (c == null && !cm.proveriSifruCenovnika(sifraCenovnika)) {
					JOptionPane.showMessageDialog(null, "Neispravna šifra cenovnika", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDatume(datumOd, datumDo, c)) {
					JOptionPane.showMessageDialog(null, "Neispravni datumi", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(jed)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena JEDNOKREVETNA", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(dvo1)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena DVOKREVETNA1", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(dvo2)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena DVOKREVETNA2", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(tro)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena TROKREVETNA", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(cet)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena CETVOROKREVETNA", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(dorucak)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena DORUCAK", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(rucak)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena RUCAK", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!cm.proveriDouble(vecera)) {
					JOptionPane.showMessageDialog(null, "Neispravna cena VECERA", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					HashMap<String, Double> cenovnik = new HashMap<String, Double>();
					Cenovnik noviCenovnik;
					cenovnik.put("JEDNOKREVETNA", Double.parseDouble(jed));
					cenovnik.put("DVOKREVETNA1", Double.parseDouble(dvo1));
					cenovnik.put("DVOKREVETNA2", Double.parseDouble(dvo2));
					cenovnik.put("TROKREVETNA", Double.parseDouble(tro));
					cenovnik.put("CETVOROKREVETNA", Double.parseDouble(cet));
					cenovnik.put("DORUCAK", Double.parseDouble(dorucak));
					cenovnik.put("RUCAK", Double.parseDouble(rucak));
					cenovnik.put("VECERA", Double.parseDouble(vecera));
					
					if (c != null) {
						noviCenovnik = new Cenovnik(sifraCenovnika, LocalDate.parse(datumOd, cm.formater), LocalDate.parse(datumDo, cm.formater), cenovnik);
						try {
							cm.izmeni(noviCenovnik);
							JOptionPane.showMessageDialog(null,
									"Cenovnik " + noviCenovnik.sifraCenovnika + " uspešno izmenjen.");
							CenovniciDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
						} catch (IOException e1) {
							System.out.println("Greska kod izmene cenovnika");
							e1.printStackTrace();
						}

					} else {
						noviCenovnik = new Cenovnik(sifraCenovnika, LocalDate.parse(datumOd, cm.formater), LocalDate.parse(datumDo, cm.formater), cenovnik);
						try {
							cm.dodaj(noviCenovnik, true);
							JOptionPane.showMessageDialog(null,
									"Cenovnik " + noviCenovnik.sifraCenovnika + " uspešno dodata.");
							CenovniciDodajIzmeniDialog.this.dispose();
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
				CenovniciDodajIzmeniDialog.this.dispose();
			}

		});
	}

}

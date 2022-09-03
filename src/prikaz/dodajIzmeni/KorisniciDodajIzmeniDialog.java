package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entiteti.Administrator;
import entiteti.Gost;
import entiteti.Korisnik;
import entiteti.Recepcioner;
import entiteti.Sobarica;
import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import net.miginfocom.swing.MigLayout;
import prikaz.TableFrame;

public class KorisniciDodajIzmeniDialog extends DodajIzmeniDialog {

	private KorisniciManager km;
	private Korisnik k;

	public KorisniciDodajIzmeniDialog(JFrame roditelj, Object izmena, int selektovanaUloga, boolean zakucaj) {
		super(roditelj, izmena);
		try {
			this.km = KorisniciManager.getInstance();
		} catch (Exception e) {
			System.out.println("Greska kod zaposleni dijaloga");
		}
		this.k = (Korisnik) izmena;
		initGUI(selektovanaUloga, zakucaj);
		pack();
	}

	@Override
	protected void initGUI(int selektovanaUloga, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblUloga = new JLabel("Uloga");
		add(lblUloga);

		String[] ulogeLista = { "Gost" };
		JComboBox uloga = new JComboBox(ulogeLista);
		uloga.setSelectedIndex(selektovanaUloga);
		add(uloga, "span 2");

		uloga.setEnabled(!zakucaj);

		JLabel lblIme = new JLabel("Ime");
		add(lblIme);

		JTextField txtIme = new JTextField(20);
		add(txtIme, "span 2");

		JLabel lblPrezime = new JLabel("Prezime");
		add(lblPrezime);

		JTextField txtPrezime = new JTextField(20);
		add(txtPrezime, "span 2");

		JLabel lblPol = new JLabel("Pol");
		add(lblPol);

		String[] polLista = { "muskarac", "zena" };
		JComboBox pol = new JComboBox(polLista);
		pol.setSelectedIndex(-1);
		add(pol, "span 2");

		JLabel lblDatumRodjenja = new JLabel("Datum rođenja");
		add(lblDatumRodjenja);

		JTextField txtDatumRodjenja = new JTextField(20);
		add(txtDatumRodjenja, "span 2");

		JLabel lblTelefon = new JLabel("Telefon");
		add(lblTelefon);

		JTextField txtTelefon = new JTextField(20);
		add(txtTelefon, "span 2");

		JLabel lblAdresa = new JLabel("Adresa");
		add(lblAdresa);

		JTextField txtAdresa = new JTextField(20);
		add(txtAdresa, "span 2");

		JLabel lblKorisnickoIme = new JLabel("Korisni�?ko ime");
		add(lblKorisnickoIme);

		JTextField txtKorisnickoIme = new JTextField(20);
		add(txtKorisnickoIme, "span 2");

		JLabel lblLozinka = new JLabel("Lozinka");
		add(lblLozinka);

		JPasswordField txtLozinka = new JPasswordField(20);
		add(txtLozinka, "span 2");

		add(new JLabel());

		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Potvrdi");
		add(btnPotvrdi);

//		UPISIVANJE TEKSTA U TEXT POLJA -> AKO JE IZMENA U PITANJU
		if (k != null) {
			uloga.setSelectedIndex(0);
			uloga.setEnabled(false);
			txtKorisnickoIme.setText(k.korisnickoIme);
			txtKorisnickoIme.setEditable(false);
			txtTelefon.setText(k.getTelefon());
			txtLozinka.setText(k.getLozinka());
			txtIme.setText(k.getIme());
			txtPrezime.setText(k.getPrezime());

			if (k.getPol().equals("muskarac")) {
				pol.setSelectedIndex(0);
			} else {
				pol.setSelectedIndex(1);
			}
			pol.setEnabled(false);

			txtDatumRodjenja.setText(k.getDatumRodjenja().format(km.formater));
			txtAdresa.setText(k.getAdresa());
		}

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				validacije svih pojedinacnih podataka 
//				JOptionPane.showMessageDialog(null, "Neispravno korisni�?ko ime", "Greška", JOptionPane.ERROR_MESSAGE);

				String sUloga = (String) uloga.getSelectedItem();
				String ime = capitalize(txtIme.getText());
				String prezime = capitalize(txtPrezime.getText());
				String sPol = (String) pol.getSelectedItem();
				String datumRodjenja = txtDatumRodjenja.getText().trim();
				String telefon = txtTelefon.getText().trim();
				String adresa = txtAdresa.getText().trim();
				String korisnickoIme = txtKorisnickoIme.getText().trim();
				String lozinka = new String(txtLozinka.getPassword()).trim();

				if (!sUloga.equals("Gost")) {
					JOptionPane.showMessageDialog(null, "Neispravna uloga", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriIme(ime)) {
					JOptionPane.showMessageDialog(null, "Neispravno ime", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriIme(prezime)) {
					JOptionPane.showMessageDialog(null, "Neispravno prezime", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriPol(sPol + "")) {
					JOptionPane.showMessageDialog(null, "Neispravan pol", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriDatumRodjenja(datumRodjenja)) {
					JOptionPane.showMessageDialog(null, "Neispravan datum rođenja", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriTelefon(telefon)) {
					JOptionPane.showMessageDialog(null, "Neispravan broj telefona", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriAdresu(adresa)) {
					JOptionPane.showMessageDialog(null, "Neispravna adresa", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (k == null && !km.proveriKorisnickoIme(korisnickoIme)) {
					JOptionPane.showMessageDialog(null, "Neispravno korisni�?ko ime", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriLozinku(lozinka)) {
					JOptionPane.showMessageDialog(null, "Neispravna lozinka", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					Korisnik noviKorisnik = null;
					if (sUloga.equals("Gost")) {
						noviKorisnik = new Gost(ime, prezime, sPol, LocalDate.parse(datumRodjenja, km.formater),
								telefon, adresa, korisnickoIme, lozinka);
					}
					if (k != null) {
						try {
							km.izmeni(noviKorisnik);
							JOptionPane.showMessageDialog(null,
									"Korisnik " + noviKorisnik.imeKorisnika() + " uspešno izmenjen.");
							KorisniciDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
							km.azurirajListe();
						} catch (IOException e1) {
							System.out.println("Greska kod izmene korisnika");
							e1.printStackTrace();
						}
					} else {
						try {
							if (noviKorisnik != null) {
								km.dodaj(noviKorisnik, true);
								JOptionPane.showMessageDialog(null,
										"Korisnik " + noviKorisnik.imeKorisnika() + " uspešno dodat.");
								KorisniciDodajIzmeniDialog.this.dispose();
								((TableFrame) roditelj).azurirajTabelu();
								km.azurirajListe();
//								TODO azuriranje liste podataka -> prosledi parametar da zna koju funkcijunda zove
							} else {
								throw new Exception();
							}
						} catch (Exception e1) {
							System.out.println();
							e1.printStackTrace();
						}
					}

				}

			}

		});

		btnOtkazi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				KorisniciDodajIzmeniDialog.this.dispose();
			}

		});
	}
}

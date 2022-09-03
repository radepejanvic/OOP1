package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import entiteti.Administrator;
import entiteti.Korisnik;
import entiteti.Recepcioner;
import entiteti.Sobarica;
import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import net.miginfocom.swing.MigLayout;
import prikaz.TableFrame;

public class ZaposleniDodajIzmeniDialog extends DodajIzmeniDialog {
	private KorisniciManager km;
	private Zaposleni z;

	public ZaposleniDodajIzmeniDialog(JFrame roditelj, Object izmena, int selektovanaUloga, boolean zakucaj) {
		super(roditelj, izmena);
		try {
			this.km = KorisniciManager.getInstance();
		} catch (Exception e) {
			System.out.println("Greska kod zaposleni dijaloga");
		}
		this.z = (Zaposleni) izmena;
		initGUI(selektovanaUloga, zakucaj);
		pack();
	}

	@Override
	protected void initGUI(int selektovanaUloga, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblUloga = new JLabel("Uloga");
		add(lblUloga);

		String[] ulogeLista = { "Administrator", "Recepcioner", "Sobarica" };
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

		JLabel lblStrucnaSprema = new JLabel("Stru�?na sprema");
		add(lblStrucnaSprema);

		String[] sp = { "I", "II", "III", "IV", "V", "VI", "VII1", "VII2", "VIII" };
		JComboBox sSprema = new JComboBox(sp);
		sSprema.setSelectedIndex(-1);
		add(sSprema, "span 2");

		JLabel lblStaz = new JLabel("Staž");
		add(lblStaz);

		JTextField txtStaz = new JTextField(20);
		add(txtStaz, "span 2");

		add(new JLabel());

		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Potvrdi");
		add(btnPotvrdi);

//		UPISIVANJE TEKSTA U TEXT POLJA -> AKO JE IZMENA U PITANJU
		if (z != null) {
			if (z instanceof Administrator) {
				uloga.setSelectedIndex(0);
			} else if (z instanceof Recepcioner) {
				uloga.setSelectedIndex(1);
			} else if (z instanceof Sobarica) {
				uloga.setSelectedIndex(2);
			}
			uloga.setEnabled(false);
			txtKorisnickoIme.setText(z.korisnickoIme);
			txtKorisnickoIme.setEditable(false);
			txtTelefon.setText(z.getTelefon());
			txtLozinka.setText(z.getLozinka());
			txtIme.setText(z.getIme());
			txtPrezime.setText(z.getPrezime());

			if (z.getPol().equals("muskarac")) {
				pol.setSelectedIndex(0);
			} else {
				pol.setSelectedIndex(1);
			}
			pol.setEnabled(false);

			txtDatumRodjenja.setText(z.getDatumRodjenja().format(km.formater));
			txtAdresa.setText(z.getAdresa());

			for (int i = 0; i < sp.length; i++) {
				if (sp[i].equals(z.getStrucnaSprema() + "")) {
					sSprema.setSelectedIndex(i);
					break;
				}
			}

			txtStaz.setText(z.getStaz() + "");
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
				String strucnaSprema = (String) sSprema.getSelectedItem();
				String staz = txtStaz.getText().trim();

				if (!km.proveriZaposlenost(sUloga + "")) {
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
				} else if (z == null && !km.proveriKorisnickoIme(korisnickoIme)) {
					JOptionPane.showMessageDialog(null, "Neispravno korisni�?ko ime", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriLozinku(lozinka)) {
					JOptionPane.showMessageDialog(null, "Neispravna lozinka", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriStrucnuSpremu(strucnaSprema + "")) {
					JOptionPane.showMessageDialog(null, "Neispravna stru�?na sprema", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!km.proveriStaz(staz)) {
					JOptionPane.showMessageDialog(null, "Neispravan staž", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					Zaposleni noviKorisnik = null;
					if (sUloga.equals("Administrator")) {
						noviKorisnik = new Administrator(ime, prezime, sPol,
								LocalDate.parse(datumRodjenja, km.formater), telefon, adresa, korisnickoIme, lozinka,
								strucnaSprema, Integer.parseInt(staz));
					} else if (sUloga.equals("Recepcioner")) {
						noviKorisnik = new Recepcioner(ime, prezime, sPol, LocalDate.parse(datumRodjenja, km.formater),
								telefon, adresa, korisnickoIme, lozinka, strucnaSprema, Integer.parseInt(staz));
					} else if (sUloga.equals("Sobarica")) {
						noviKorisnik = new Sobarica(ime, prezime, sPol, LocalDate.parse(datumRodjenja, km.formater),
								telefon, adresa, korisnickoIme, lozinka, strucnaSprema, Integer.parseInt(staz), new ArrayList<String>());
					}
					if (z != null) {
						try {
							km.izmeni(noviKorisnik);
							JOptionPane.showMessageDialog(null,
									"Korisnik " + noviKorisnik.imeKorisnika() + " uspešno izmenjen.");
							ZaposleniDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
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
								ZaposleniDodajIzmeniDialog.this.dispose();
								((TableFrame) roditelj).azurirajTabelu();
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
				ZaposleniDodajIzmeniDialog.this.dispose();
			}

		});
	}
}

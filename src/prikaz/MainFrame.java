package prikaz;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entiteti.Administrator;
import entiteti.Gost;
import entiteti.Recepcioner;
import entiteti.Sobarica;
import menadzeri.CenovniciManager;
import menadzeri.IzvestajiManager;
import menadzeri.KorisniciManager;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {
	private KorisniciManager km;
	private SobeManager sm;
	private CenovniciManager cm;
	private RezervacijeManager rm;
	private IzvestajiManager im;

	public MainFrame() throws HeadlessException, IOException {
		super();
		this.km = KorisniciManager.getInstance();
		this.sm = SobeManager.getInstance();
		this.cm = CenovniciManager.getInstance();
		this.rm = RezervacijeManager.getInstance();
		this.im = IzvestajiManager.getInstance();
		sm.azurirajSveDatume();
		rm.otkaziRezervacije();
		im.napraviDanasnjiIzvestaj();

		loginDialog();
		mainFrame();
	}

	private void loginDialog() {
		JDialog d = new JDialog();
		d.setTitle("Prijava");
		d.setLocationRelativeTo(null);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		initLoginGUI(d);
		d.pack();
		d.setVisible(true);
	}

	private void initLoginGUI(JDialog dijalog) {
		MigLayout layout = new MigLayout("wrap 2", "[][]", "[]20[][]20[]");
		dijalog.setLayout(layout);

		JTextField tfKorisnickoIme = new JTextField(20);
		dijalog.add(tfKorisnickoIme);
		JPasswordField pfLozinka = new JPasswordField(20);
		JButton btnPotvrdi = new JButton("Potvrdi");
		JButton btnOtkazi = new JButton("OtkaÅ¾i");
		dijalog.getRootPane().setDefaultButton(btnPotvrdi);

		dijalog.add(new JLabel("DobrodoÅ¡li! Prijavite se."), "span 2");
		dijalog.add(new JLabel("KorisniÄ?ko ime"));
		dijalog.add(tfKorisnickoIme);
		dijalog.add(new JLabel("Lozinka"));
		dijalog.add(pfLozinka);
		dijalog.add(new JLabel());
		dijalog.add(btnPotvrdi, "split 2");
		dijalog.add(btnOtkazi);

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String korisnickoIme = tfKorisnickoIme.getText().trim();
				String lozinka = new String(pfLozinka.getPassword()).trim();
				System.out.println(korisnickoIme + "  " + lozinka);
				if (km.login(korisnickoIme, lozinka).equals("Neispravno korisniÄ?ko ime")) {
					JOptionPane.showMessageDialog(null, "Neispravno korisniÄ?ko ime", "GreÅ¡ka", JOptionPane.ERROR_MESSAGE);
				} else if (km.login(korisnickoIme, lozinka).equals("Nesipravna lozinka")) {
					JOptionPane.showMessageDialog(null, "Neispravna lozinka", "GreÅ¡ka", JOptionPane.ERROR_MESSAGE);
				} else {
					dijalog.setVisible(false);
					dijalog.dispose();
					if (km.getKorisnik(korisnickoIme) instanceof Gost) {
						GostMainFrame gmf = new GostMainFrame((Gost) km.getKorisnik(korisnickoIme));
						gmf.setVisible(true);
					} else if (km.getKorisnik(korisnickoIme) instanceof Administrator) {
						AdministratorMainFrame amf = new AdministratorMainFrame((Administrator) km.getKorisnik(korisnickoIme));
						amf.setVisible(true);
					} else if (km.getKorisnik(korisnickoIme) instanceof Recepcioner) {
						RecepcionerMainFrame rmf = new RecepcionerMainFrame((Recepcioner) km.getKorisnik(korisnickoIme));
						rmf.setVisible(true);
					} else if (km.getKorisnik(korisnickoIme) instanceof Sobarica) {
						SobaricaMainFrame smf = new SobaricaMainFrame((Sobarica) km.getKorisnik(korisnickoIme));
						smf.setVisible(true);
//						poziva se prozor sobarice
					}
//					MainFrame.this.setVisible(true);
				}

			}

		});

		btnOtkazi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dijalog.dispose();

			}

		});

	}

//	mozda suvisno ukoliko cu imati pojedinacan prozor za svakog korisnika
	private void mainFrame() {
		this.setTitle("Hotel");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setIconImage(new ImageIcon("img/icon.png").getImage());
//		promeni ikonicu -> nadji neku dobru na netu 

		initMainGUI();
	}

	private void initMainGUI() {
		
	}

	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

}

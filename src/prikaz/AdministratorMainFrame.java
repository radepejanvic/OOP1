package prikaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import entiteti.Administrator;
import entiteti.Izvestaj;
import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.StatusSobe;
import menadzeri.KorisniciManager;
import menadzeri.SobeManager;
import model.AdminiModel;
import model.CenovniciModel;
import model.GostiModel;
import model.KorisniciModel;
import model.RecepcioneriModel;
import model.RezervacijeModel;
import model.SobariceModel;
import model.SobeModel;
import model.ZaposleniModel;
import prikaz.dodajIzmeni.DatumiDialog;

public class AdministratorMainFrame extends JFrame {
	Administrator admin;
	private KorisniciManager km;
	private SobeManager sm;

	public AdministratorMainFrame(Administrator admin) {
		super();
		this.admin = admin;
		try {
			this.km = KorisniciManager.getInstance();
			this.sm = SobeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}

		administratorMainFrame();
	}

	private void administratorMainFrame() {
		this.setTitle("Administrator - " + admin.imeKorisnika());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.decode("#56876D"));

		LocalDate danas = LocalDate.now();
		JPanel datum = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblDatum = new JLabel(danas.format(km.formater));
		datum.add(lblDatum);
		this.add(datum, BorderLayout.SOUTH);

		initAdminGUI();
	}

	private void initAdminGUI() {
		JMenuBar glavniMeni = new JMenuBar();
//		glavniMeni.setBackground(Color.decode("#56876D"));

		JMenu korisnici = new JMenu("Korisnici");
		JMenuItem sviKor = new JMenuItem("Svi korisnici");
		JMenu zaposleni = new JMenu("Zaposleni");
		JMenuItem sviZap = new JMenuItem("Svi zaposleni");
		JMenuItem administratori = new JMenuItem("Administratori");
		JMenuItem recepcioneri = new JMenuItem("Recepcioneri");
		JMenuItem sobarice = new JMenuItem("Sobarice");
		JMenuItem gosti = new JMenuItem("Gosti");

		JMenu sobe = new JMenu("Sobe");
		JMenuItem sveSobe = new JMenuItem("Sve sobe");
		JMenuItem jednokrevetne = new JMenuItem("Jednokrevetne");
		JMenuItem dvokrevetne1 = new JMenuItem("Dvokrevetne(1+1)");
		JMenuItem dvokrevetne2 = new JMenuItem("Dvokrevetne(2)");
		JMenuItem trokrevetne = new JMenuItem("Trokrevetne(2+1)");
		JMenuItem cetvorokrevetne = new JMenuItem("Četvorokrevetne(2+2)");

		JMenu rezervacije = new JMenu("Rezervacije");
		JMenuItem sveRez = new JMenuItem("Sve rezervacije");
		JMenuItem nacekanju = new JMenuItem("Na �?ekanju");
		JMenuItem potvrdjene = new JMenuItem("Potvrđene");
		JMenuItem otkazane = new JMenuItem("Otkazane");
		JMenuItem odbijene = new JMenuItem("Odbijene");

		JMenu cenovnici = new JMenu("Cenovinici");
		JMenuItem sviCen = new JMenuItem("Svi cenovnici");
		cenovnici.add(sviCen);
//		JMenuItem aktuelni = new 
//		mozda staviti menu iteme za aktivne i neaktivne cenovnike

		JMenu izvestaji = new JMenu("Izveštaji");
		JMenuItem godisnji = new JMenuItem("Godišnji izveštaj");
		JMenu mesecni = new JMenu("Mese�?ni izveštaji");
		JMenuItem opterecenjeSob = new JMenuItem("Opterećenje sobarica");
		JMenuItem statusiRez = new JMenuItem("Statusi rezervacija");
		JMenu ostali = new JMenu("Ostali...");
		JMenuItem prihodi = new JMenuItem("Prihodi");
		JMenuItem rashodi = new JMenuItem("Rashodi");
		JMenuItem potvrdjeneIz = new JMenuItem("Broj potvrdjenih");
		JMenuItem odbijeneIz = new JMenuItem("Broj odbijenih");
		JMenuItem otakazaneIz = new JMenuItem("Broj otkazanih");
		JMenuItem zaradaSobe = new JMenuItem("Zarada sobe");
		JMenuItem sobaricaSobe = new JMenuItem("Spremljene sobe sobarice");

		// dodaj razlicite menu iteme za razlicite izvestaje

		this.setJMenuBar(glavniMeni);
//		SPAJANJE SVEGA U GLAVNIMENI
		glavniMeni.add(korisnici);
		glavniMeni.add(sobe);
		glavniMeni.add(rezervacije);
		glavniMeni.add(cenovnici);
		glavniMeni.add(izvestaji);

//		SPAJANJE KORISNICI MENIJA
		korisnici.add(sviKor);
		korisnici.add(zaposleni);
		korisnici.add(gosti);

//		SPAJANJE ZAPOSLENI MENIJA
		zaposleni.add(sviZap);
		zaposleni.add(administratori);
		zaposleni.add(recepcioneri);
		zaposleni.add(sobarice);

//		SPAJANJE SOBE MENIJA
		sobe.add(sveSobe);
		sobe.add(jednokrevetne);
		sobe.add(dvokrevetne1);
		sobe.add(dvokrevetne2);
		sobe.add(trokrevetne);
		sobe.add(cetvorokrevetne);

//		SPAJANJE REZERVACIJE MENIJA
		rezervacije.add(sveRez);
		rezervacije.add(nacekanju);
		rezervacije.add(potvrdjene);
		rezervacije.add(otkazane);
		rezervacije.add(odbijene);

//		SPAJANJE CENOVNICI MENIJA

//		SPAJANJE IZVESTAJI MENIJA
		izvestaji.add(godisnji);
		izvestaji.add(mesecni);
		izvestaji.add(ostali);
		mesecni.add(opterecenjeSob);
		mesecni.add(statusiRez);
		ostali.add(prihodi);
		ostali.add(rashodi);
		ostali.add(potvrdjeneIz);
		ostali.add(odbijeneIz);
		ostali.add(otakazaneIz);
		ostali.add(zaradaSobe);
		ostali.add(sobaricaSobe);

//		ACTION LISTENERI
		sviKor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					KorisniciTableFrame ktf = new KorisniciTableFrame(AdministratorMainFrame.this,
							new KorisniciModel());
					ktf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		sviZap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					ZaposleniTableFrame ztf = new ZaposleniTableFrame(AdministratorMainFrame.this, new ZaposleniModel(),
							-1, false);
					ztf.setVisible(true);
				} catch (Exception e) {
					System.out.println("nesto se sjebalo");
				}

			}

		});

		gosti.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					KorisniciTableFrame ktf = new KorisniciTableFrame(AdministratorMainFrame.this, new GostiModel());
//					ktf.eksperiment.setText("Gost");
					ktf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		administratori.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					ZaposleniTableFrame ztf = new ZaposleniTableFrame(AdministratorMainFrame.this, new AdminiModel(), 0,
							true);
					ztf.eksperiment.setText("Administrator");
					ztf.setVisible(true);
				} catch (Exception e) {
					System.out.println("nesto se sjebalo");
				}

			}

		});

		sobarice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					ZaposleniTableFrame ztf = new ZaposleniTableFrame(AdministratorMainFrame.this, new SobariceModel(),
							2, true);
					ztf.eksperiment.setText("Sobarica");
					ztf.setVisible(true);
				} catch (Exception e) {
					System.out.println("nesto se sjebalo");
				}

			}

		});

		recepcioneri.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					ZaposleniTableFrame ztf = new ZaposleniTableFrame(AdministratorMainFrame.this,
							new RecepcioneriModel(), 1, true);
					ztf.eksperiment.setText("Recepcioner");
					ztf.setVisible(true);
				} catch (Exception e) {
					System.out.println("nesto se sjebalo");
				}

			}

		});

		sveSobe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					SobeTableFrame stf = new SobeTableFrame(AdministratorMainFrame.this, new SobeModel(null, null), 1,
							true);
//					stf.eksperiment.setText("Recepcioner");
					stf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("nesto se sjebalo");
				}

			}

		});

		sveRez.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					RezervacijeTableFrame rtf = new RezervacijeTableFrame(AdministratorMainFrame.this,
							new RezervacijeModel(null, ""), null);
//					stf.eksperiment.setText("Recepcioner");
					rtf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("nesto se sjebalo");
				}

			}

		});

		sviCen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				try {
					CenovniciTableFrame ctf = new CenovniciTableFrame(AdministratorMainFrame.this,
							new CenovniciModel());
//					stf.eksperiment.setText("Recepcioner");
					ctf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("nesto se sjebalo");
				}

			}

		});

		godisnji.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Grafici grafik = new Grafici();
//              grafik.godisnjiPrikazPrihodaPoSobama();
				XYChart chart = grafik.getGppps();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						new SwingWrapper(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					}
				});
				t.start();

			}
		});

		opterecenjeSob.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Grafici grafik = new Grafici();
//              grafik.godisnjiPrikazPrihodaPoSobama();
				PieChart chart = grafik.getPos();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						new SwingWrapper(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					}
				});
				t.start();

			}
		});

		statusiRez.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Grafici grafik = new Grafici();
//              grafik.godisnjiPrikazPrihodaPoSobama();
				PieChart chart = grafik.getPsr();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						new SwingWrapper(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					}
				});
				t.start();
			}
		});

		prihodi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "PRIHODI");
				dd.setVisible(true);

			}
		});

		rashodi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "RASHODI");
				dd.setVisible(true);

			}
		});

		potvrdjeneIz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "BROJ POTVRDJENIH");
				dd.setVisible(true);

			}
		});

		odbijeneIz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "BROJ ODBIJENIH");
				dd.setVisible(true);

			}
		});

		otakazaneIz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "BROJ OTKAZANIH");
				dd.setVisible(true);

			}
		});
//		ovde ide overrajd za tableframe -> tj njegovog dugmeta

		otakazaneIz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, null, "BROJ OTKAZANIH");
				dd.setVisible(true);

			}
		});

		zaradaSobe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SobeTableFrame stf;
				try {
					stf = new SobeTableFrame(AdministratorMainFrame.this, new SobeModel(null, null), 1, true);
					stf.setVisible(true);
					stf.onesposobiIzmeni();
					stf.onesposobiObrisi();
					stf.getDodaj().setToolTipText("Generiši izveštaj");

					stf.getDodaj().removeActionListener(stf.getImeAkcijeD());
					stf.getDodaj().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = stf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = stf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Soba s = sm.getSoba(kljuc);
								stf.dispose();
								DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, s, "ZARADA SOBE");
								dd.setVisible(true);
							}

						}
					});
				
				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja Izvestaja");
					e1.printStackTrace();
				}

			}
		});

		sobaricaSobe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				KorisniciTableFrame ktf;
				try {
					ktf = new KorisniciTableFrame(AdministratorMainFrame.this, new SobariceModel());
					ktf.setVisible(true);
					ktf.onesposobiIzmeni();
					ktf.onesposobiObrisi();
					ktf.getDodaj().setToolTipText("Generiši izveštaj");

					ktf.getDodaj().removeActionListener(ktf.getImeAkcijeD());
					ktf.getDodaj().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = ktf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = ktf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Korisnik sobarica = km.getKorisnik(kljuc);
								ktf.dispose();
								DatumiDialog dd = new DatumiDialog(AdministratorMainFrame.this, sobarica, "BROJ SOBA");
								dd.setVisible(true);
							}

						}
					});

				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja Izvestaja");
					e1.printStackTrace();
				}

			}
		});

	}

//	ostali.add(potvrdjeneIz);
//	ostali.add(odbijeneIz);
//	ostali.add(otakazaneIz);
//	ostali.add(zaradaSobe);
//	ostali.add(sobaricaSobe);
}

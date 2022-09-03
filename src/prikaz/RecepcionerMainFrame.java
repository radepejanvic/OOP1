package prikaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entiteti.Izvestaj;
import entiteti.Recepcioner;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.StatusRezervacije;
import entiteti.StatusSobe;
import menadzeri.IzvestajiManager;
import menadzeri.KorisniciManager;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;
import model.GostiModel;
import model.RezervacijeModel;
import model.SobariceModel;

public class RecepcionerMainFrame extends JFrame {
	private Recepcioner recepcioner;
	private KorisniciManager km;
	private SobeManager sm;
	private IzvestajiManager im;
	private RezervacijeManager rm;

	public RecepcionerMainFrame(Recepcioner recepcioner) {
		this.recepcioner = recepcioner;
		try {
			this.im = IzvestajiManager.getInstance();
			this.km = KorisniciManager.getInstance();
			this.sm = SobeManager.getInstance();
			this.rm = RezervacijeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}

		recepcionerMainFrame();
	}

	private void recepcionerMainFrame() {
		this.setTitle("Recepcioner - " + this.recepcioner.imeKorisnika());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
//		this.setIconImage(new ImageIcon().getImage());
		this.getContentPane().setBackground(Color.decode("#E1DD8F"));

		LocalDate danas = LocalDate.now();
		JPanel datum = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblDatum = new JLabel(danas.format(sm.formater));
		datum.add(lblDatum);
		this.add(datum, BorderLayout.SOUTH);
		initRecepcionerGUI();
	}

	private void initRecepcionerGUI() {
		JMenuBar glavniMeni = new JMenuBar();
		this.setJMenuBar(glavniMeni);
		JMenu korisnici = new JMenu("Korisnici");
		JMenuItem gosti = new JMenuItem("Gosti");
		JMenuItem sobarice = new JMenuItem("Sobarice");

		JMenu rezervacije = new JMenu("Rezervacije");
		JMenuItem rez = new JMenuItem("Potvrdi/Odbij");

		JMenu checks = new JMenu("Prijave/Odjave");
		JMenuItem checkIn = new JMenuItem("Prijava - Check in");
		JMenuItem checkOut = new JMenuItem("Odjava - Check out");

		glavniMeni.add(korisnici);
		glavniMeni.add(rezervacije);
		glavniMeni.add(checks);

		korisnici.add(gosti);
		korisnici.add(sobarice);

		rezervacije.add(rez);

		checks.add(checkIn);
		checks.add(checkOut);

		gosti.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					KorisniciTableFrame ktf = new KorisniciTableFrame(RecepcionerMainFrame.this, new GostiModel());
					ktf.setVisible(true);
				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja gostiju");
					e1.printStackTrace();
				}

			}

		});

		sobarice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ZaposleniTableFrame ztf = new ZaposleniTableFrame(RecepcionerMainFrame.this, new SobariceModel(),
							-1, false);
					ztf.setVisible(true);
				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja sobarica");
					e1.printStackTrace();
				}

			}

		});

		rez.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					RezervacijeTableFrame rtf = new RezervacijeTableFrame(RecepcionerMainFrame.this,
							new RezervacijeModel(null, "NACEKANJU"), null);
					rtf.setVisible(true);
					rtf.onesposobiIzmeni();
					rtf.getDodaj().setToolTipText("Potvrdi rezervaciju");
					rtf.getObrisi().setToolTipText("Otkaži rezervaciju");

					JButton potvrdiRez = rtf.getDodaj();
					potvrdiRez.removeActionListener(rtf.getImeAkcijeD());
					potvrdiRez.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = rtf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = rtf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Rezervacija r = rm.getRezervacija(kljuc);
								if (r != null) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da želite da potvrdite rezervaciju?",
											r.brojRezervacije + " - Potvrda potvrde ", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("Potvrđena rezervacija " + r.brojRezervacije);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										try {
											r.setStatusRezervacije(StatusRezervacije.valueOf("POTVRDJENA"));
//											r.oslobodiDatume();
											rm.azuriraj();
											rm.azurirajListe();
											rtf.azurirajTabelu();
										} catch (IOException e1) {
											System.out.println("Greška kod brisanja/ažuriranja");
											e1.printStackTrace();
										}
									}
								} else {
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!",
											"Greška", JOptionPane.ERROR_MESSAGE);
								}
							}

						}

					});

					JButton otkaziRez = rtf.getObrisi();
					otkaziRez.removeActionListener(rtf.getImeAkcijeO());
					otkaziRez.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = rtf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = rtf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Rezervacija r = rm.getRezervacija(kljuc);
								if (r != null) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da želite da odbijete rezervaciju?",
											r.brojRezervacije + " - Potvrda odbijanja", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("Odbijena rezervacija " + r.brojRezervacije);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										try {
											r.setStatusRezervacije(StatusRezervacije.valueOf("ODBIJENA"));
											r.oslobodiDatume();
											rm.azuriraj();
											rm.azurirajListe();
											rtf.azurirajTabelu();
										} catch (IOException e1) {
											System.out.println("Greška kod brisanja/ažuriranja");
											e1.printStackTrace();
										}
									}
								} else {
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!",
											"Greška", JOptionPane.ERROR_MESSAGE);
								}
							}

						}

					});

//					odradi potvrdu i odbijanje rezervacija -> overrajd + i X dugmeta
				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja rezervacija");
					e1.printStackTrace();
				}

			}

		});

		checkIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RezervacijeTableFrame rtf;
				try {
					rtf = new RezervacijeTableFrame(RecepcionerMainFrame.this, new RezervacijeModel(null, "CHECKIN"),
							null);
					rtf.setVisible(true);
					rtf.onesposobiIzmeni();
					rtf.onesposobiObrisi();
					rtf.getDodaj().setToolTipText("Prijava gosta - Check in");
					JButton dodaj = rtf.getDodaj();
					dodaj.removeActionListener(rtf.getImeAkcijeD());
					
//					rtf.getDodaj().removeActionListener(rtf.getImeAkcijeD());

					rtf.getDodaj().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = rtf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = rtf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Rezervacija r = rm.getRezervacija(kljuc);
								Soba s = sm.getSoba(r.getBrojSobe());
								if (r != null && s.getStatusSobe().equals(StatusSobe.valueOf("SLOBODNA"))) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da želite da prijavite gosta u sobu ",
											r.getBrojSobe() + " - Potvrda check in-a", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("CheckIn " + r.brojRezervacije);
										LocalDate danas = LocalDate.now();
										Izvestaj i = im.getIzvestaj(danas.format(im.formater));
										i.setDolasci(i.getDolasci() + 1);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										try {
											s.setStatusSobe(StatusSobe.valueOf("ZAUZETA"));
											sm.azuriraj();
											rtf.azurirajTabelu();
										} catch (IOException e1) {
											System.out.println("Greška kod brisanja/ažuriranja");
											e1.printStackTrace();
										}
									}
								} else {
									if (!s.getStatusSobe().equals(StatusSobe.valueOf("SLOBODNA"))) {
										JOptionPane.showMessageDialog(null,
												"Izabrana soba još uvek nije spremna za sledećeg gosta!", "Greška",
												JOptionPane.ERROR_MESSAGE);
									}
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!",
											"Greška", JOptionPane.ERROR_MESSAGE);
								}
							}

						}
					});

				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja CheckIn-a");
					e1.printStackTrace();
				}

			}

		});

		checkOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RezervacijeTableFrame rtf;
				try {
					rtf = new RezervacijeTableFrame(RecepcionerMainFrame.this, new RezervacijeModel(null, "CHECKOUT"),
							null);
					rtf.setVisible(true);
					rtf.onesposobiDodaj();
					rtf.onesposobiIzmeni();
					rtf.getObrisi().setToolTipText("Odjava gosta - Check out");

					rtf.getObrisi().removeActionListener(rtf.getImeAkcijeO());
					rtf.getObrisi().addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int red = rtf.getTabela().getSelectedRow();
							if (red == -1) {
								JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
										JOptionPane.WARNING_MESSAGE);
							} else {
								String kljuc = rtf.getTabela().getValueAt(red, 1).toString();
//								promeniti u korisnika -> odnosno u objekat 
								Rezervacija r = rm.getRezervacija(kljuc);
								Soba s = sm.getSoba(r.getBrojSobe());
								if (r != null && s.getStatusSobe().equals(StatusSobe.valueOf("ZAUZETA"))) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da želite da prijavite gosta u sobu ",
											r.getBrojSobe() + " - Potvrda check in-a", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("CheckIn " + r.brojRezervacije);
										LocalDate danas = LocalDate.now();
										Izvestaj i = im.getIzvestaj(danas.format(im.formater));
										i.setOdlasci(i.getOdlasci() + 1);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										try {
											s.setStatusSobe(StatusSobe.valueOf("SPREMANJE"));
											km.dodeliSobu(s.brojSobe);
											sm.azuriraj();
											rtf.azurirajTabelu();
										} catch (IOException e1) {
											System.out.println("Greška kod brisanja/ažuriranja");
											e1.printStackTrace();
										}
									}
								} else {
									if (!s.getStatusSobe().equals(StatusSobe.valueOf("ZAUZETA"))) {
										JOptionPane.showMessageDialog(null,
												"Izabrana soba još uvek nije zauzeta!", "Greška",
												JOptionPane.ERROR_MESSAGE);
									}
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!",
											"Greška", JOptionPane.ERROR_MESSAGE);
								}
							}

						}
					});

				} catch (IOException e1) {
					System.out.println("Greska kod otvaranja CheckOut-a");
					e1.printStackTrace();
				}

			}

		});
	}
}

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
import entiteti.Soba;
import entiteti.Sobarica;
import entiteti.StatusSobe;
import menadzeri.IzvestajiManager;
import menadzeri.KorisniciManager;
import menadzeri.SobeManager;
import model.SobeModel;

public class SobaricaMainFrame extends JFrame{
	private Sobarica sobarica;
	private SobeManager sm;
	private IzvestajiManager im;
	private KorisniciManager km;
	
	public SobaricaMainFrame(Sobarica sobarica) {
		this.sobarica = sobarica;
		try {
			this.im = IzvestajiManager.getInstance();
			this.km = KorisniciManager.getInstance();
			this.sm = SobeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sobaricaMainFrame();
	}
	
	private void sobaricaMainFrame() {
		this.setTitle("Sobarica - " + this.sobarica.imeKorisnika());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
//		this.setIconImage(new ImageIcon().getImage());
		this.getContentPane().setBackground(Color.decode("#ce6550"));
		
		LocalDate danas = LocalDate.now();
		JPanel datum = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblDatum = new JLabel(danas.format(sm.formater));
		datum.add(lblDatum);
		this.add(datum, BorderLayout.SOUTH);
		
		initSobaricaGUI();
	}

	private void initSobaricaGUI() {
		JMenuBar glavniMeni = new JMenuBar();
		this.setJMenuBar(glavniMeni);
		JMenu sobe = new JMenu("Sobe");
		JMenuItem mojeSob = new JMenuItem("Sobe za spremanje");
		
		glavniMeni.add(sobe);
		sobe.add(mojeSob);
		
		mojeSob.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				ovde prikazi sobetableframe -> sa onemogucenim dugmicima izmeni i obrisi i overrajd na dugme dodaj
				try {
					SobeTableFrame stf = new SobeTableFrame(SobaricaMainFrame.this, new SobeModel(null, SobaricaMainFrame.this.sobarica), -1, false);
					stf.setVisible(true);
					stf.onesposobiIzmeni();
					stf.onesposobiDodaj();
					JButton izbaci = stf.getObrisi();
					izbaci.removeActionListener(stf.getImeAkcijeO());
					izbaci.addActionListener(new ActionListener() {

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
								if (s != null) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da ste o�?istili sobu ",
											s.brojSobe + " - Potvrda spremanja", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("Spremna soba " + s.brojSobe);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										LocalDate danas = LocalDate.now();
										Izvestaj i = im.getIzvestaj(danas.format(im.formater));
										i.dodajSSPar(SobaricaMainFrame.this.sobarica.korisnickoIme);
//										i.
										try {
											s.setStatusSobe(StatusSobe.valueOf("SLOBODNA"));
											SobaricaMainFrame.this.sobarica.getListaSoba().remove(s.brojSobe);
											System.out.println(SobaricaMainFrame.this.sobarica.getListaSoba());
											sm.azuriraj();
											km.azuriraj();
											sm.azurirajListe();
											stf.azurirajTabelu();
										} catch (IOException e1) {
											System.out.println("Greška kod brisanja/ažuriranja");
											e1.printStackTrace();
										}
									}
								} else {
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabranu sobu!", "Greška",
											JOptionPane.ERROR_MESSAGE);
								}
							}
							
						}
						
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
			
		});
	}
	
}

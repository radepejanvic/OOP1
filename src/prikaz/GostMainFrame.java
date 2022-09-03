
package prikaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entiteti.Gost;
import entiteti.Rezervacija;
import entiteti.StatusRezervacije;
import menadzeri.KorisniciManager;
import menadzeri.RezervacijeManager;
import model.RezervacijeModel;
import net.miginfocom.swing.MigLayout;
import prikaz.dodajIzmeni.NovaRezervacija;

public class GostMainFrame extends JFrame{
	public Gost gost;
	private KorisniciManager km;
	private RezervacijeManager rm;
	private LocalDate datumOd;
	private LocalDate datumDo;
	
	public GostMainFrame(Gost gost) {
		super();
		this.gost = gost;
		try {
			this.km = KorisniciManager.getInstance();
			this.rm = RezervacijeManager.getInstance();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		gostMainFrame();
	}

	private void gostMainFrame() {
		this.setTitle("Gost - " + this.gost.imeKorisnika());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
//		this.setIconImage(new ImageIcon().getImage());
		this.getContentPane().setBackground(Color.decode("#9c516e"));
		
		LocalDate danas = LocalDate.now();
		JPanel datum = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblDatum = new JLabel(danas.format(km.formater));
		datum.add(lblDatum);
		this.add(datum, BorderLayout.SOUTH);
		
		initGostGUI();
	}
	
	private void initGostGUI() {
		JMenuBar glavniMeni = new JMenuBar();
		this.setJMenuBar(glavniMeni);
		JMenu rez = new JMenu("Rezervacije");
		JMenuItem mojeRez = new JMenuItem("Moje rezervacije");
		JMenuItem novaRez = new JMenuItem("Nova rezervacija");
		
		glavniMeni.add(rez);
		rez.add(mojeRez);
		rez.add(novaRez);
		
		mojeRez.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					RezervacijeTableFrame rtf = new RezervacijeTableFrame(GostMainFrame.this,
							new RezervacijeModel(GostMainFrame.this.gost, ""), GostMainFrame.this.gost);
					rtf.onesposobiIzmeni();
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
											"Da li ste sigurni da želite da otkažete rezervaciju?",
											r.brojRezervacije + " - Potvrda otkazivanja", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
										System.out.println("Otkazana rezervacija " + r.brojRezervacije);
//										OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
										try {
											r.setStatusRezervacije(StatusRezervacije.valueOf("OTKAZANA"));
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
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!", "Greška",
											JOptionPane.ERROR_MESSAGE);
								}
							}
							
						}
						
					});
//					stf.eksperiment.setText("Recepcioner");
					rtf.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("nesto se sjebalo");
				}
				
			}
			
		});
		
		novaRez.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				NovaRezervacija nr = new NovaRezervacija(GostMainFrame.this, GostMainFrame.this.gost);
				nr.setVisible(true);
			}
			
		});
		
//		JPanel panel = new JPanel();
//		add(panel, BorderLayout.CENTER);
//		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]20[]");
//		panel.setLayout(ml);
//		JLabel naslov = new JLabel("Nova rezervacija");
//		panel.add(naslov, "span 3");
//		
//		JLabel tipSobe = new JLabel("Tip sobe");
//		panel.add(tipSobe, "span 3");
//		
////		kombo boks
//		
//		JLabel datumOd = new JLabel("Datum od");
//		panel.add(datumOd);
//		JLabel datumDo = new JLabel("Datum do");
//		panel.add(datumDo);

		
		
//		1) POP-UP u koji se upisuju datum od datum do i tip sobe -> prost dodajIzmeni 
//		2) iskace tabela sa slobodnim sobama -> samo drugi model napravim 
//		3) overrajdujem dugme add da napravi rezervaciju sa datim podacima i iskljucim dugme X i izmeni
//		4) Moje rezervacije -> drugi model i overrajd dugmeta X, onesposobim dodaj i izmeni
		
//		gostu iskace mali dodaj zimeni dialog -> on unosi datume i bira tip sobe 
//		pravi se lista slobodnih soba za datume i otvara se automatski tabela sa slobodnim sobama
//		pritiskom na dugme za edit dobija pop-up prozor u kom on 
	}
	
	public String getGost() {
		return this.gost.korisnickoIme;
	}
}

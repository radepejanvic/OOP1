package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entiteti.DodatneUsluge;
import entiteti.DodatniKriterijumi;
import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;
import model.SobeModel;
import net.miginfocom.swing.MigLayout;
import prikaz.AdministratorMainFrame;
import prikaz.GostMainFrame;
import prikaz.RezervacijeTableFrame;
import prikaz.SobeTableFrame;
import prikaz.TableFrame;

public class NovaRezervacija extends DodajIzmeniDialog {
	private RezervacijeManager rm;
	private SobeManager sm;
	private Korisnik gost;

	public NovaRezervacija(JFrame roditelj, Korisnik gost) {
		super(roditelj, null);
		this.gost = gost;
		try {
			this.rm = RezervacijeManager.getInstance();
			this.sm = SobeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setTitle("Nova rezervacija");
		initGUI(1, false);
		pack();
	}

	@Override
	protected void initGUI(int selektovaniIndex, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblTipSobe = new JLabel("Tip sobe");
		add(lblTipSobe);

		String[] tipovi = { "JEDNOKREVETNA", "DVOKREVETNA1", "DVOKREVETNA2", "TROKREVETNA", "CETVOROKREVETNA" };
		JComboBox tip = new JComboBox(tipovi);
		tip.setSelectedIndex(-1);
		add(tip, "span 2");

		JLabel lblKriterijumi = new JLabel("Kriterijumi");
		add(lblKriterijumi);

		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addElement("WIFI");
		dlm.addElement("KLIMA");
		dlm.addElement("KADA");
		dlm.addElement("TUSKABINA");
		dlm.addElement("TERASA");
		JList<String> dodatniKriterijumi = new JList<String>();
		dodatniKriterijumi.setModel(dlm);
		add(dodatniKriterijumi, "span 2");

		JLabel lblDatumOd = new JLabel("Datum od");
		add(lblDatumOd);
		JTextField txtDatumOd = new JTextField(20);
		add(txtDatumOd, "span 2");

		JLabel datumDo = new JLabel("Datum do");
		add(datumDo);
		JTextField txtDatumDo = new JTextField(20);
		add(txtDatumDo, "Span 2");

		add(new JLabel());
		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Pretraži");
		add(btnPotvrdi);

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				provera podataka, pozivanje funkcije pretrage i pravljenje soba frejma sa kastom listom
				String sTip = (String) tip.getSelectedItem() + "";
				String datumOd = txtDatumOd.getText().trim();
				String datumDo = txtDatumDo.getText().trim();

				int[] dodKrit = dodatniKriterijumi.getSelectedIndices();
				ArrayList<DodatniKriterijumi> listaDK = new ArrayList<DodatniKriterijumi>();
				for (int i : dodKrit) {
					listaDK.add(DodatniKriterijumi.valueOf(dlm.get(i)));
				}

				if (!sm.proveriTipSobe(sTip)) {
					JOptionPane.showMessageDialog(null, "Neispravan tip sobe", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriDatumOd(datumOd)) {
					JOptionPane.showMessageDialog(null, "Neispravan po�?etni datum", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else if (!rm.proveriDatumDo(datumOd, datumDo)) {
					JOptionPane.showMessageDialog(null, "Neispravan krajnji datum", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try {
//						nekako proslediti sve ove podatke modelu sobe 
						ArrayList<Object> lista = new ArrayList<Object>();
						lista.add(sTip);
						lista.add(LocalDate.parse(datumDo, rm.formater));
						lista.add(LocalDate.parse(datumDo, rm.formater));
						lista.add(listaDK);
						SobeTableFrame stf = new SobeTableFrame(NovaRezervacija.this.roditelj, new SobeModel(lista, null), 1,
								true);
						stf.onesposobiIzmeni();
						stf.onesposobiObrisi();
						NovaRezervacija.this.dispose();
						JButton rezervisi = stf.getDodaj();
						rezervisi.removeActionListener(stf.getImeAkcijeD());
						rezervisi.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								int red = stf.getTabela().getSelectedRow();
								String kljuc = stf.getTabela().getValueAt(red, 1).toString();
								Soba s = sm.getSoba(kljuc);
								if (s != null) {
									int izbor = JOptionPane.showConfirmDialog(null,
											"Da li ste sigurni da rezervišete sobu broj ",
											s.brojSobe + " - Potvrda rezervacije", JOptionPane.YES_NO_OPTION);
									if (izbor == JOptionPane.YES_OPTION) {
//										njega poziva rezervacijatable frame -> nju poziva gost main frame -> roditelj roditelja
//										ArrayList<Object> prosledjenaLista = new ArrayList<Object>();
//										prosledjenaLista.add(rm.getBrRez());
//										prosledjenaLista.add(gost.korisnickoIme);
//										prosledjenaLista.add(s.brojSobe);
//										prosledjenaLista.add(datumOd);
//										prosledjenaLista.add(datumDo);
//										prosledjenaLista.add(null);
//										prosledjenaLista.add(listaDK);
//										prosledjenaLista.add(0);
//										prosledjenaLista.add("NACEKANJU");

										Rezervacija r = new Rezervacija(rm.getBrRez(), gost.korisnickoIme, s.brojSobe,
												LocalDate.parse(datumOd, rm.formater),
												LocalDate.parse(datumDo, rm.formater), new ArrayList<DodatneUsluge>(),
												listaDK, 0, "NACEKANJU");

										RezervacijeDodajIzmeniDialog rdid = new RezervacijeDodajIzmeniDialog(stf, r, true);
										rdid.setVisible(true);
									}
								} else {
									JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!",
											"Greška", JOptionPane.ERROR_MESSAGE);
								}
							}

						});
						stf.setVisible(true);
						NovaRezervacija.this.dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}

		});

	}

}

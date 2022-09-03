package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import entiteti.DodatniKriterijumi;
import entiteti.Soba;
import menadzeri.SobeManager;
import net.miginfocom.swing.MigLayout;
import prikaz.TableFrame;

public class SobeDodajIzmeniDialog extends DodajIzmeniDialog {
	private SobeManager sm;
	private Soba s;

	public SobeDodajIzmeniDialog(JFrame roditelj, Object izmena, int izabrani, boolean zakucaj) {
		super(roditelj, izmena);
		try {
			this.sm = SobeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.s = (Soba) izmena;
		initGUI(izabrani, zakucaj);
		pack();
	}

	@Override
	protected void initGUI(int selektovaniIndex, boolean zakucaj) {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]20[]");
		setLayout(ml);

		JLabel lblBrojSobe = new JLabel("Broj sobe");
		add(lblBrojSobe);
		JTextField txtBrojSobe = new JTextField(20);
		add(txtBrojSobe, "span 2");

		JLabel lblTipSobe = new JLabel("Tip sobe");
		add(lblTipSobe);

		String[] sp = { "JEDNOKREVETNA", "DVOKREVETNA1", "DVOKREVETNA2", "TROKREVETNA", "CETVOROKREVETNA" };
		JComboBox tipSobe = new JComboBox(sp);
		tipSobe.setSelectedIndex(-1);
		add(tipSobe, "span 2");

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

		JLabel lblStatusSobe = new JLabel("Status sobe");
		add(lblStatusSobe);

		String[] mp = { "SLOBODNA", "ZAUZETA", "SPREMANJE" };
		JComboBox statusSobe = new JComboBox(mp);
		statusSobe.setSelectedIndex(-1);
		add(statusSobe, "span 2");

		add(new JLabel());

		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Potvrdi");
		add(btnPotvrdi);

//		UPISIVANJE TEKSTA U TEXT POLJA -> AKO JE IZMENA U PITANJU
		if (s != null) {
			txtBrojSobe.setText(s.brojSobe);
			txtBrojSobe.setEditable(false);

			tipSobe.setSelectedItem(s.getTipSobe() + "");

			statusSobe.setSelectedItem(s.getStatusSobe() + "");
			statusSobe.setEnabled(false);

			dodatniKriterijumi.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			int[] indexi2 = { -1, -1, -1, -1, -1 };
			int i = 0;
			for (DodatniKriterijumi dk : s.getDodatniKriterijumi()) {
				indexi2[i] = dlm2.indexOf(dk.toString());
				i++;
			}
			dodatniKriterijumi.setSelectedIndices(indexi2);
		} else {
			statusSobe.setSelectedIndex(0);
		}

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String brojSobe = txtBrojSobe.getText();
				String sTipSobe = (String) tipSobe.getSelectedItem();
				String sStatusSobe = (String) statusSobe.getSelectedItem();

				int[] dodKrit = dodatniKriterijumi.getSelectedIndices();
				ArrayList<DodatniKriterijumi> listaDK = new ArrayList<DodatniKriterijumi>();
				for (int i : dodKrit) {
					listaDK.add(DodatniKriterijumi.valueOf(dlm2.get(i)));
				}

				if (s == null && !sm.proveriBrojSobe(brojSobe)) {
					JOptionPane.showMessageDialog(null, "Neispravan broj sobe", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!sm.proveriTipSobe(sTipSobe + "")) {
					JOptionPane.showMessageDialog(null, "Neispravan tip sobe", "Greška", JOptionPane.ERROR_MESSAGE);
				} else if (!sm.proveriStatusSobe(sStatusSobe + "")) {
					JOptionPane.showMessageDialog(null, "Neispravan status sobe", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					Soba novaSoba = null;
					if (s != null) {
						novaSoba = new Soba(brojSobe, sTipSobe, sStatusSobe, s.getListaDatuma(), listaDK, false);
						try {
							sm.izmeni(novaSoba);
							JOptionPane.showMessageDialog(null, "Soba " + novaSoba.brojSobe + " uspešno izmenjena.");
							SobeDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();
						} catch (IOException e1) {
							System.out.println("Greska kod izmene sobe");
							e1.printStackTrace();
						}
					} else {
						try {
							novaSoba = new Soba(brojSobe, sTipSobe, sStatusSobe, null, listaDK, true);
							sm.dodaj(novaSoba, true);
							JOptionPane.showMessageDialog(null, "Soba " + novaSoba.brojSobe + " uspešno dodata.");
							SobeDodajIzmeniDialog.this.dispose();
							((TableFrame) roditelj).azurirajTabelu();

						} catch (Exception e1) {
							System.out.println("Greska kod dodavanja sobe");
							e1.printStackTrace();
						}
					}
				}
			}

		});

		btnOtkazi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SobeDodajIzmeniDialog.this.dispose();
			}

		});

	}

}

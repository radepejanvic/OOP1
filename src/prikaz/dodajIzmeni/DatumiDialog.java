package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entiteti.Cenovnik;
import entiteti.Korisnik;
import entiteti.Soba;
import entiteti.Sobarica;
import menadzeri.CenovniciManager;
import menadzeri.IzvestajiManager;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;
import net.miginfocom.swing.MigLayout;

public class DatumiDialog extends DodajIzmeniDialog {
	private RezervacijeManager rm;
	private SobeManager sm;
	private IzvestajiManager im;
	private Soba s;
	private Korisnik sobarica;
	private String naslov;

	public DatumiDialog(JFrame roditelj, Object izmena, String naslov) {
		super(roditelj, izmena);

		this.naslov = naslov;
		try {
			this.rm = RezervacijeManager.getInstance();
			this.sm = SobeManager.getInstance();
			this.im = IzvestajiManager.getInstance();
		} catch (IOException e) {
			System.out.println("Greska kod konstruktora cenovnici dodaj izmeni dialog");
			e.printStackTrace();
		}

		if (this.naslov.equals("ZARADA SOBE")) {
			this.s = (Soba) izmena;
		} else if (this.naslov.equals("BROJ SOBA")) {
			this.sobarica = (Korisnik) izmena;
		}
		initGUI(1, false);
		pack();
	}

	@Override
	protected void initGUI(int selektovaniIndex, boolean zakucaj) {
		// TODO Auto-generated method stub
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]20[]");
		setLayout(ml);
		setTitle(this.naslov);
		JLabel lblDatumOd = new JLabel("Datum od");
		add(lblDatumOd);
		JTextField txtDatumOd = new JTextField(20);
		add(txtDatumOd, "span 2");

		JLabel lblDatumDo = new JLabel("Datum do");
		add(lblDatumDo);
		JTextField txtDatumDo = new JTextField(20);
		add(txtDatumDo, "span 2");

		JLabel lbl = new JLabel(this.naslov + ": ");
		add(lbl);
		JLabel lbl2 = new JLabel("");
		add(lbl2, "span 2");

		add(new JLabel());
		JButton btnOtkazi = new JButton("Otkaži");
		add(btnOtkazi);

		JButton btnPotvrdi = new JButton("Generiši izveštaj");
		add(btnPotvrdi);

		if (s != null) {
			setTitle(this.naslov + " " + s.brojSobe);
		} else if (sobarica != null) {
			setTitle(this.naslov + " " + sobarica.korisnickoIme);
		} else {

		}

		btnPotvrdi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String datumOd = txtDatumOd.getText().trim();
				String datumDo = txtDatumDo.getText().trim();

				if (!im.proveriDatume(datumOd, datumDo)) {
					JOptionPane.showMessageDialog(null, "Neispravni datumi", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					LocalDate dtOd = LocalDate.parse(datumOd, rm.formater);
					LocalDate dtDo = LocalDate.parse(datumDo, rm.formater);

					if (naslov.equals("PRIHODI")) {
						double broj = rm.getPrihodi(dtOd, dtDo);
						lbl2.setText(broj + "");
					} else if (naslov.equals("RASHODI")) {
						double broj = rm.getRashodi(dtOd, dtDo);
						lbl2.setText("-" + broj + "");
					} else if (naslov.equals("BROJ POTVRDJENIH")) {
						int broj = rm.prebrojStatuseRezervacija(dtOd, dtDo)[1];
						lbl2.setText(broj + "");
					} else if (naslov.equals("BROJ ODBIJENIH")) {
						int broj = rm.prebrojStatuseRezervacija(dtOd, dtDo)[2];
						lbl2.setText(broj + "");
					} else if (naslov.equals("BROJ OTKAZANIH")) {
						int broj = rm.prebrojStatuseRezervacija(dtOd, dtDo)[3];
						lbl2.setText(broj + "");
					} else if (naslov.equals("ZARADA SOBE")) {
						double broj = rm.zaradaSobe(dtOd, dtDo, s.brojSobe);
						lbl2.setText(broj + "");
					} else if (naslov.equals("BROJ SOBA")) {
						int broj = im.prebrojSobeJedneSobarice(dtOd, dtDo, sobarica.korisnickoIme);
						lbl2.setText(broj + "");
					}

				}

			}
		});

		btnOtkazi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DatumiDialog.this.dispose();

			}
		});

	}

}

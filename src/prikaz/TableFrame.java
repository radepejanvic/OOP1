package prikaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import menadzeri.Manager;
import prikaz.dodajIzmeni.ZaposleniDodajIzmeniDialog;

public abstract class TableFrame extends JFrame {

	protected JToolBar glavniAlati = new JToolBar();
	protected JButton btnDodaj = new JButton();
	protected JButton btnIzmeni = new JButton();
	protected JButton btnObrisi = new JButton();
	protected JTextField tfPretraga = new JTextField(20);
	protected JTextField eksperiment = new JTextField(20);
	protected JTable tabela;
	protected TableRowSorter<AbstractTableModel> sorterTabele = new TableRowSorter<AbstractTableModel>();
	private JFrame roditelj;
	private AbstractTableModel atm;
	private Manager menadzer;
	private ActionListener dodaj;
	private ActionListener obrisi;

	public TableFrame(JFrame roditelj, AbstractTableModel atm, Manager menadzer, String naslov) throws IOException {
		this.roditelj = roditelj;
		this.atm = atm;
		this.menadzer = menadzer;
		setTitle(naslov);
		setSize(1100, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/icon.png").getImage());

//		PODESAVANJE ALATA
		ImageIcon dodajIkona = new ImageIcon("img/add.png");
		ImageIcon scaled = new ImageIcon(dodajIkona.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		dodajIkona = scaled;
		btnDodaj.setIcon(dodajIkona);
		glavniAlati.add(btnDodaj);
		ImageIcon izmeniIkona = new ImageIcon("img/edit.gif");
		btnIzmeni.setIcon(izmeniIkona);
		glavniAlati.add(btnIzmeni);
		ImageIcon obrisiIkona = new ImageIcon("img/remove.gif");
		btnObrisi.setIcon(obrisiIkona);
		glavniAlati.add(btnObrisi);
		glavniAlati.setFloatable(false);
		glavniAlati.setBackground(Color.decode("#8DB38B"));
		add(glavniAlati, BorderLayout.NORTH);
		
		btnDodaj.setToolTipText("Dodaj");
		btnIzmeni.setToolTipText("Izmeni");
		btnObrisi.setToolTipText("Ukloni");
		
//		PODESAVANJE TABELE
//		TODO ovde se poziva AbstractTableModel
		tabela = new JTable(atm);
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.getTableHeader().setReorderingAllowed(false);
//		podesavanje manuelnog sortera tabele
		sorterTabele.setModel((AbstractTableModel) tabela.getModel());
		tabela.setRowSorter(sorterTabele);
		JScrollPane sc = new JScrollPane(tabela);
		add(sc, BorderLayout.CENTER);

		tabela.getTableHeader().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// preuzimanje indeksa kolone potrebnog za sortiranje
				int index = tabela.getTableHeader().columnAtPoint(arg0.getPoint());
				System.out.println(index);
				// call abstract sort method

			}

		});

		JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pSearch.setBackground(Color.decode("#8DB38B"));
		pSearch.add(new JLabel("Pretraži:"));
		pSearch.add(tfPretraga);
		pSearch.add(eksperiment);
		eksperiment.setVisible(false);

		add(pSearch, BorderLayout.SOUTH);

		tfPretraga.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// System.out.println("~ "+tfSearch.getText());
				if (tfPretraga.getText().trim().length() == 0) {
					sorterTabele.setRowFilter(null);
				} else {
					sorterTabele.setRowFilter(RowFilter.regexFilter("(?i)" + tfPretraga.getText().trim()));
				}
			}
		});
		
		eksperiment.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// System.out.println("~ "+tfSearch.getText());
				if (eksperiment.getText().trim().length() == 0) {
					sorterTabele.setRowFilter(null);
				} else {
					sorterTabele.setRowFilter(RowFilter.regexFilter("(?i)" + eksperiment.getText().trim()));
				}
			}
		});

		initActions();
	}

	private void initActions() {
//		btnDodaj.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				initDodajIzmeniDialog(roditelj, null);
//			}
//		});
		
		ActionListener dodaj = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initDodajIzmeniDialog(roditelj, null);
			}
		};
		
		btnDodaj.addActionListener(dodaj);

		btnIzmeni.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = tabela.getSelectedRow();
				if (red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String kljuc = tabela.getValueAt(red, 1).toString();
					Object o = menadzer.get(kljuc);
					System.out.println(o);
					if (o != null) {
						initDodajIzmeniDialog(roditelj, o);
					} else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!", "Greška",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		ActionListener obrisi = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = tabela.getSelectedRow();
				if (red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String kljuc = tabela.getValueAt(red, 1).toString();
//					promeniti u korisnika -> odnosno u objekat 
					Object o = menadzer.get(kljuc);
					if (o != null) {
						int izbor = JOptionPane.showConfirmDialog(null,
								"Da li ste sigurni da želite da obrišete artikal?",
								imeArtikla(o) + " - Potvrda brisanja", JOptionPane.YES_NO_OPTION);
						if (izbor == JOptionPane.YES_OPTION) {
							System.out.println("Obrisan korisnik" + imeArtikla(o));
//							OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
							try {
								menadzer.obrisi(kljuc);
								menadzer.azuriraj();
								menadzer.azurirajListe();
								azurirajTabelu();
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
		};
		
		btnObrisi.addActionListener(obrisi);
		
//		btnObrisi.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int red = tabela.getSelectedRow();
//				if (red == -1) {
//					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška",
//							JOptionPane.WARNING_MESSAGE);
//				} else {
//					String kljuc = tabela.getValueAt(red, 1).toString();
////					promeniti u korisnika -> odnosno u objekat 
//					Object o = menadzer.get(kljuc);
//					if (o != null) {
//						int izbor = JOptionPane.showConfirmDialog(null,
//								"Da li ste sigurni da želite da obrišete artikal?",
//								imeArtikla(o) + " - Potvrda brisanja", JOptionPane.YES_NO_OPTION);
//						if (izbor == JOptionPane.YES_OPTION) {
//							System.out.println("Obrisan korisnik" + imeArtikla(o));
////							OVO DOLE AKTIVIRAJ TEK KADA DODAS JOS PAR KORISNIKA
//							try {
//								menadzer.obrisi(kljuc);
//								menadzer.azuriraj();
//								menadzer.azurirajListe();
//								azurirajTabelu();
//							} catch (IOException e1) {
//								System.out.println("Greška kod brisanja/ažuriranja");
//								e1.printStackTrace();
//							}
//						}
//					} else {
//						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani artikal!", "Greška",
//								JOptionPane.ERROR_MESSAGE);
//					}
//				}
//			}
//		});
	}
	
	public void onesposobiDodaj() {
		btnDodaj.setEnabled(false);
	}
	
	public void onesposobiIzmeni() {
		btnIzmeni.setEnabled(false);
	}
	
	public void onesposobiObrisi() {
		btnObrisi.setEnabled(false);
	}
	
	public JButton getDodaj() {
		return this.btnDodaj;
	}
	
	public JButton getIzmeni() {
		return this.btnIzmeni;
	}
	
	public JButton getObrisi() {
		return this.btnObrisi;
	}
	
	public JTable getTabela() {
		return tabela;
	}
	
	public ActionListener getImeAkcijeD(){
		return this.dodaj;
	}
	
	public ActionListener getImeAkcijeO() {
		return this.obrisi;
	}

	public abstract void initDodajIzmeniDialog(JFrame roditelj, Object o);

	public abstract String imeArtikla(Object o);

	public void azurirajTabelu() {
		AbstractTableModel atm = (AbstractTableModel) this.tabela.getModel();
		atm.fireTableDataChanged();
	}

	protected abstract void sort(int index);

}

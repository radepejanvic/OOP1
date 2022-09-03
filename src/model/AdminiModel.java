package model;

import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import entiteti.Zaposleni;
import menadzeri.KorisniciManager;

public class AdminiModel extends AbstractTableModel{
	private KorisniciManager km;
	private String[] imenaKolona = { "Uloga", "KorisniÄ?ko ime", "Ime", "Prezime", "Pol", "Datum roÄ‘enja", "Telefon",
			"Adresa", "StruÄ?na sprema", "StaÅ¾", "Plata" };

	public AdminiModel() throws IOException {
		this.km = KorisniciManager.getInstance();
	}

	@Override
	public int getRowCount() {
		return km.getAdministratori().size();
	}

	@Override
	public int getColumnCount() {
		return imenaKolona.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Zaposleni z = km.getAdministratori().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return z.getClass().getName().toString();
		case 1:
			return z.korisnickoIme;
		case 2:
			return z.getIme();
		case 3:
			return z.getPrezime();
		case 4:
			return z.getPol();
		case 5:
			return z.getDatumRodjenja();
		case 6:
			return z.getTelefon();
		case 7:
			return z.getAdresa();
		case 8:
			return z.getStrucnaSprema();
		case 9:
			return z.getStaz();
		case 10:
			return z.getPlata();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return this.imenaKolona[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}
}

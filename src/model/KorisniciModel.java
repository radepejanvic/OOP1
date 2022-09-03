package model;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entiteti.Korisnik;
import menadzeri.KorisniciManager;

public class KorisniciModel extends AbstractTableModel {
	private KorisniciManager km;
	private String[] imenaKolona = {"Uloga", "Korisnicko ime", "Ime", "Prezime", "Pol", "Datum rodjenja", "Telefon", "Adresa"};
	
	public KorisniciModel() throws IOException {
		this.km = KorisniciManager.getInstance();
	}

	@Override
	public int getRowCount() {
		return km.getListaKorisnika().size();
	}

	@Override
	public int getColumnCount() {
		return imenaKolona.length;
	}

	@Override
	public Object getValueAt(int indeksReda, int ideksKolone) {
		Korisnik k = km.getListaKorisnika().get(indeksReda);
		switch (ideksKolone) {
		case 0:
			return k.getClass().getName().toString();
		case 1:
			return k.korisnickoIme;
		case 2:
			return k.getIme();
		case 3:
			return k.getPrezime();
		case 4:
			return k.getPol();
		case 5:
			return k.getDatumRodjenja().format(km.formater);
		case 6:
			return k.getTelefon();
		case 7:
			return k.getAdresa();
		default:
			return null;
		}
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


package model;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
import menadzeri.RezervacijeManager;

public class RezervacijeModel extends AbstractTableModel {

	private RezervacijeManager rm;
	private String[] imenaKolona = { "Klasa", "Broj rezervacije", "Gost", "Broj Sobe", "PoÄ?etni datum", "Krajnji datum",
			"Dodatne usluge", "Dodatni kriterijumi", "Cena", "Status" };
	private Korisnik gost;
	private String tip;

	public RezervacijeModel(Korisnik gost, String tip) throws IOException {
		this.rm = RezervacijeManager.getInstance();
		this.gost = gost;
		this.tip = tip;
	}

	private ArrayList<Rezervacija> listaRezervacija() {
		if (this.gost == null) {
			if (this.tip.equals("NACEKANJU")) {
				return rm.getNaCekanju();
			} else if (this.tip.equals("CHECKIN")) {
				return rm.getCheckIn();
			} else if (this.tip.equals("CHECKOUT")) {
				return rm.getCheckOut();
			} else {
				return rm.getRezervacije();
			}
		} else {
			return rm.mojeRezervacije(this.gost);
		}
	}

	@Override
	public int getRowCount() {
		return listaRezervacija().size();
	}

	@Override
	public int getColumnCount() {
		return imenaKolona.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Rezervacija r = listaRezervacija().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return r.getClass().getName().toString();
		case 1:
			return r.brojRezervacije;
		case 2:
			return r.getGost();
		case 3:
			return r.getBrojSobe();
		case 4:
			return r.getDatumOd().format(rm.formater);
		case 5:
			return r.getDatumDo().format(rm.formater);
		case 6:
			return r.getDodatneUsluge();
		case 7:
			return r.getDodatniKriterijumi();
		case 8:
			return r.getCena();
		case 9:
			return r.getStatusRezervacije();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return this.imenaKolona[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		try { 
			return this.getValueAt(0, columnIndex).getClass();
		} catch (IndexOutOfBoundsException e) {
			Rezervacija r = new Rezervacija();
			return r.getClass();
		}
	}
}

package model;

import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import entiteti.Cenovnik;
import entiteti.Soba;
import menadzeri.CenovniciManager;

public class CenovniciModel extends AbstractTableModel {

	private CenovniciManager cm;
	private String[] imenaKolona = { "Klasa", "Sifra cenovnika", "Datum od", "Datum do", "JEDNOKREVETNA",
			"DVOKREVETNA1", "DVOKREVETNA2", "TROKREVETNA", "CETVOROKREVETNA", "DORUCAK", "RUCAK", "VECERA" };

	public CenovniciModel() throws IOException {
		this.cm = CenovniciManager.getInstance();
	}

	@Override
	public int getRowCount() {
		return cm.getCenovnici().size();
	}

	@Override
	public int getColumnCount() {
		return imenaKolona.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Cenovnik c = cm.getCenovnici().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return c.getClass().getName().toString();
		case 1:
			return c.sifraCenovnika;
		case 2:
			return c.getDatumOd().format(cm.formater);
		case 3:
			return c.getDatumDo().format(cm.formater);
		case 4:
			return c.getCenovnik().get("JEDNOKREVETNA");
		case 5:
			return c.getCenovnik().get("DVOKREVETNA1");
		case 6:
			return c.getCenovnik().get("DVOKREVETNA2");
		case 7:
			return c.getCenovnik().get("TROKREVETNA");
		case 8:
			return c.getCenovnik().get("CETVOROKREVETNA");
		case 9:
			return c.getCenovnik().get("DORUCAK");
		case 10:
			return c.getCenovnik().get("RUCAK");
		case 11:
			return c.getCenovnik().get("VECERA");
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

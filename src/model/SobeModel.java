package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entiteti.DodatniKriterijumi;
import entiteti.Soba;
import entiteti.Sobarica;
import entiteti.Zaposleni;
import menadzeri.KorisniciManager;
import menadzeri.SobeManager;

public class SobeModel extends AbstractTableModel{
	private SobeManager sm;
	private String[] imenaKolona = {"Klasa", "Broj Sobe", "Tip",  "Dodatni kriterijumi", "Status"};
	private ArrayList<Object> lista;
	private Sobarica sobarica;

	public SobeModel(ArrayList<Object> lista, Sobarica sobarica) {
		try {
			this.sm = SobeManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.lista = lista;
		this.sobarica = sobarica;
	}
	
//	private ArrayList<Soba> listaSoba() {
//		
//	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Soba> listaSoba() {
		if (this.lista == null) {
			if (this.sobarica == null) {
				return sm.getSobe();
			} else {
				ArrayList<Soba> sobe = new ArrayList<Soba>();
				for (Soba s : sm.getSobe()) {
					if (this.sobarica.getListaSoba().contains(s.brojSobe)) {
						sobe.add(s);
					}
				}
				return sobe;
			}
			
		} else {
//			TODO 
			ArrayList<Soba> sobe = new ArrayList<Soba>();
			sobe = sm.getFilterTip((String)lista.get(0));
			sobe = sm.getFilterDatumi(sobe, (LocalDate)lista.get(1), (LocalDate)lista.get(2));
			sobe = sm.getFilterKriterijumi(sobe, (ArrayList<DodatniKriterijumi>)lista.get(3));
			return sobe;
//			return sm.getListaPoKriterijumima((String)lista.get(0), (LocalDate)lista.get(1), (LocalDate)lista.get(2), (ArrayList<DodatniKriterijumi>)lista.get(3));
		}
	}
	
	@Override
	public int getRowCount() {
		return listaSoba().size();
	}

	@Override
	public int getColumnCount() {
		return imenaKolona.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Soba s;
		if (listaSoba().equals(null)) {
			s = new Soba();
		} else {
			s = listaSoba().get(rowIndex);
		}
//		Soba praznaSoba = new Soba();
		switch (columnIndex) {
		case 0:
			return s.getClass().getName().toString();
		case 1:
			return s.brojSobe;
		case 2:
			return s.getTipSobe();
		case 3: 
			return s.getDodatniKriterijumi();
		case 4:
			return s.getStatusSobe();
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
			Soba s = new Soba();
			return s.getClass();
		}
	}
}

package entiteti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Soba {
	public String brojSobe;
	TipSobe tipSobe;
	StatusSobe statusSobe;
	ArrayList<LocalDate> listaDatuma;
	ArrayList<DodatniKriterijumi> dodatniKriterijumi;

	public Soba() {
		super();
	}

	public Soba(String brojSobe, String tipSobe, String statusSobe, ArrayList<LocalDate> listaDatuma,
			ArrayList<DodatniKriterijumi> dodatniKriterijumi, boolean novaSoba) {
		super();
		this.brojSobe = brojSobe;
		this.tipSobe = TipSobe.valueOf(tipSobe);
		this.statusSobe = StatusSobe.valueOf(statusSobe);
		if (novaSoba) {
			this.listaDatuma = this.generisiDatume();
		} else {
			this.listaDatuma = listaDatuma;
		}
		this.dodatniKriterijumi = dodatniKriterijumi;
	}

//	public String generisiListuDatuma() {
//		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
//		String d = "?/" + this.brojSobe;
//		LocalDate danas = LocalDate.now();
//		LocalDate datum = danas;
//		while (!datum.isAfter(danas.plusYears(1))) {
//			d += "/" + datum.format(formater);
//			datum = datum.plusDays(1);
//		}
//		return d;
//	}

	public ArrayList<LocalDate> generisiDatume() {
		LocalDate danas = LocalDate.now();
		LocalDate datum = danas;
		ArrayList<LocalDate> datumi = new ArrayList<LocalDate>();
		while (!datum.isAfter(danas.plusYears(1))) {
			LocalDate ld = datum;
			datumi.add(ld);
			datum = datum.plusDays(1);
		}
		return datumi;
	}

	public ArrayList<DodatniKriterijumi> getDodatniKriterijumi() {
		return dodatniKriterijumi;
	}

	public void setDodatniKriterijumi(ArrayList<DodatniKriterijumi> dodatniKriterijumi) {
		this.dodatniKriterijumi = dodatniKriterijumi;
	}

	public String getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(String brojSobe) {
		this.brojSobe = brojSobe;
	}

	public StatusSobe getStatusSobe() {
		return statusSobe;
	}

	public void setStatusSobe(StatusSobe statusSobe) {
		this.statusSobe = statusSobe;
	}

	// funkcija koja azurira pocetne i krajnje datume iz liste shodno danasnjem
	// datumu
	public void azurirajDatume() {
		LocalDate danas = LocalDate.now();
		ArrayList<LocalDate> datumi = new ArrayList<LocalDate>();
		for (LocalDate datum : this.listaDatuma) {
			if (!datum.isBefore(danas)) {
				datumi.add(datum);
			}
		}
		LocalDate datum = datumi.get(datumi.size() - 1).plusDays(1);
		System.out.println(datum);
		while (!datum.isAfter(danas.plusYears(1))) {
			datumi.add(datum);
			datum = datum.plusDays(1);
		}
		this.listaDatuma = datumi;
	}

	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public ArrayList<LocalDate> getListaDatuma() {
		return listaDatuma;
	}

	public void setListaDatuma(ArrayList<LocalDate> listaDatuma) {
		this.listaDatuma = listaDatuma;
	}

	@Override
	public String toString() {
		return "\n" + brojSobe + "/" + tipSobe + "/" + statusSobe + "\n";
	}

	public String stringZaFile() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		String dk = "?/" + this.brojSobe;
		for (DodatniKriterijumi d : this.dodatniKriterijumi) {
			dk += "/" + d;
		}
		String datumi = "??/" + this.brojSobe;
		for (LocalDate d : this.listaDatuma) {
			datumi += "/" + d.format(formater);
		}
		return this + dk + "\n" + datumi;
	}

}

package menadzeri;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import entiteti.Cenovnik;
// OBRISI OVE IMPORTE KORISNIKA I SOBE
// MADA MOZDA NI NE MORAS JER JE OVO SVAKAKO TEST KLASA
import entiteti.Korisnik;
import entiteti.Rezervacija;
import entiteti.Soba;
// DOVDE OBRISATI ------

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		KorisniciManager km = KorisniciManager.getInstance();
		km.ispisi();
		CenovniciManager cm = CenovniciManager.getInstance();
		RezervacijeManager rm = RezervacijeManager.getInstance();
		System.out.println("----------------------");
		rm.ispisi();
		Rezervacija r = rm.test();
		SobeManager sm = SobeManager.getInstance();
		sm.ispisi();
		Soba s = sm.test();
		cm.ispisi();
		System.out.println("Cena boravka -> " + r.obracunajCenu());
//		r.zauzmiDatume();
//		System.out.println(s.stringZaFile());
//		s.azurirajDatume();
//		System.out.println(s.stringZaFile());
//		System.out.println(km.login("rade123", "rade123"));
		Korisnik k = km.test();
//		System.out.println(km.proveriDatumRodjenja("06.08.2004."));
		System.out.println(km.proveriLozinku("123456789"));
		System.out.println(rm.proveriDatumOd("20.08.2022."));
		System.out.println(rm.proveriDatumDo("20.08.2022.", "23.08.2022."));
		System.out.println(cm.proveriDouble("12r"));
		System.out.println("datumi -> " + cm.proveriDatumDo("21.01.2022.", "23.01.2022."));

		Cenovnik c = cm.getCenovnik("002");
		System.out.println("datumi 002 -> " + cm.proveriDatume("21.08.2022.", "23.08.2022.", c));
		System.out.println("POSLEDNJA SOBA -> " + sm.getSoba("105").stringZaFile());
		System.out.println("\nFILTERI:");
		System.out.println(sm.getFilterTip("JEDNOKREVETNA"));
		LocalDate datumOd = LocalDate.parse("20.08.2022.", sm.formater);
		LocalDate datumDo = LocalDate.parse("05.09.2022.", sm.formater);
		System.out.println();
		System.out.println(sm.getFilterDatumi(sm.getFilterTip("JEDNOKREVETNA"), datumOd, datumDo));
		System.out.println("PO KRITERIJUMU: ");
		System.out.println(sm.getFilterKriterijumi(sm.getSobe(), null));
		System.out.println("PO FILTERIMA: ");
		System.out.println(
				sm.getFilterKriterijumi(sm.getFilterDatumi(sm.getFilterTip("JEDNOKREVETNA"), datumOd, datumDo), null));
		km.azuriraj();

		System.out.println(rm.getRezervacija("1").getDatumi());
		System.out.println(rm.getPrihodi(datumOd, datumDo));
		System.out.println(rm.getRashodi(datumOd, datumDo));

		IzvestajiManager im = IzvestajiManager.getInstance();
		im.ispisi();
		System.out.println(im.prebrojSobariceSobe(datumOd, datumDo));
	

//		double[] xData = new double[] { 0.0, 1.0, 2.0 };
//		double[] yData = new double[] { 2.0, 1.0, 0.0 };
//
//		// Create Chart
//		XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
//
//		// Show it
//		new SwingWrapper(chart).displayChart();
//		
	}

//	korisnik
//	zaposleni
//	gost
//	admin
//	recepcioner
//	sobarica
//	soba
//	rezervacija
//	cenovnik

}

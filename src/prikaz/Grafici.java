package prikaz;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import entiteti.TipSobe;
import menadzeri.CenovniciManager;
import menadzeri.IzvestajiManager;
import menadzeri.KorisniciManager;
import menadzeri.RezervacijeManager;
import menadzeri.SobeManager;

public class Grafici {
	private KorisniciManager km;
	private SobeManager sm;
	private CenovniciManager cm;
	private RezervacijeManager rm;
	private IzvestajiManager im;
	private XYChart gppps;
	private PieChart pos;
	private PieChart psr;
	
	public DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	public SimpleDateFormat sdfFormat = new SimpleDateFormat("dd.MM.yyyy.");

	public Grafici() {
		super();
		try {
			this.km = KorisniciManager.getInstance();
			this.sm = SobeManager.getInstance();
			this.cm = CenovniciManager.getInstance();
			this.rm = RezervacijeManager.getInstance();
			this.im = IzvestajiManager.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.gppps = godisnjiPrikazPrihodaPoSobama();
		this.pos = pitaOpterecenjeSobarica();
		this.psr = pitaStatusiRezervacija();

//		godisnjiPrikazPrihodaPoSobama();
//		pitaOpterecenjeSobarica();
//		pitaStatusiRezervacija();
	}

	public XYChart godisnjiPrikazPrihodaPoSobama() {
		XYChart chart = new XYChartBuilder().width(800).height(600).title("Prihodi po tipu sobe u poslednjih 12 meseci")
				.xAxisTitle("Meseci").yAxisTitle("Prihodi").build();

		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		chart.getStyler().setPlotMargin(0);
		chart.getStyler().setPlotContentSize(.95);
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		chart.getStyler().setDatePattern("d.MMM");
		chart.getStyler().setDecimalPattern("#0.00");

		LocalDate danas = LocalDate.now();
		LocalDate danasProsli = LocalDate.now().minusYears(1);
		for (TipSobe ts : TipSobe.values()) {
//	  	TODO -> OVIM OBGRLI SVE STO NAPISES DOLE
			ArrayList<Date> xData = new ArrayList<Date>();
			ArrayList<Double> yData = new ArrayList<Double>();

			LocalDate datum = danasProsli;
			Date dateDatum = null;
			int i = 0;
			while (!datum.isAfter(danas)) {
				String sDatum = datum.format(formater);
				System.out.println(i);
				i++;
				try {
					dateDatum = sdfFormat.parse(sDatum);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				xData.add(dateDatum);
				System.out.println(dateDatum);
				yData.add(rm.getPrihodiTipa(ts, datum, datum.plusMonths(1)));
				datum = datum.plusMonths(1);
			}
			chart.addSeries("" + ts, xData, yData);
		}

		return chart;
	}

	public PieChart pitaOpterecenjeSobarica() {
		PieChart chart = new PieChartBuilder().width(800).height(600).title("Opterećenje sobarica u prethodnih 30 dana").build();
		 
	    // Customize Chart
//	    Color[] sliceColors = new Color[] { new Color(224, 68, 14), new Color(230, 105, 62), new Color(236, 143, 110), new Color(243, 180, 159), new Color(246, 199, 182) };
//	    chart.getStyler().setSeriesColors(sliceColors);
	 
	    // Series
		LocalDate danas = LocalDate.now();
		HashMap<String, Integer> mapa = im.prebrojSobariceSobe(danas.minusMonths(1), danas);
		System.out.println(mapa);
		for (String sobarica : mapa.keySet()) {
			System.out.println(sobarica);
			chart.addSeries(sobarica, mapa.get(sobarica));
		}
//	    chart.addSeries("Gold", 24);
//	    chart.addSeries("Silver", 21);
//	    chart.addSeries("Platinum", 39);
//	    chart.addSeries("Copper", 17);
//	    chart.addSeries("Zinc", 40);
//	    
	    return chart;
	}

	public PieChart pitaStatusiRezervacija() {
		PieChart chart = new PieChartBuilder().width(800).height(600).title("Statusi rezervacija u prethodnih 30 dana").build();
		LocalDate danas = LocalDate.now();
		
		int[] statusi = rm.prebrojStatuseRezervacija(danas.minusMonths(1), danas);
		chart.addSeries("NACEKANJU", statusi[0]);
	    chart.addSeries("POTVRDJENA", statusi[1]);
	    chart.addSeries("ODBIJENA", statusi[2]);
	    chart.addSeries("OTKAZANA", statusi[3]);
	    
	    return chart;
	}

	public XYChart getGppps() {
		return gppps;
	}

	public void setGppps(XYChart gppps) {
		this.gppps = gppps;
	}

	public PieChart getPos() {
		return pos;
	}

	public void setPos(PieChart pos) {
		this.pos = pos;
	}

	public PieChart getPsr() {
		return psr;
	}

	public void setPsr(PieChart psr) {
		this.psr = psr;
	}
	
	

//	double[] xData = new double[] { 0.0, 1.0, 2.0 };
//	double[] yData = new double[] { 2.0, 1.0, 0.0 };
//
//	// Create Chart
//	XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
//
//	// Show it
//	new SwingWrapper(chart).displayChart();

}

package entiteti;

import java.io.IOException;

import menadzeri.KorisniciManager;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String s = "VII2";
//		StrucnaSprema ss = StrucnaSprema.VII1;
		StrucnaSprema ss = StrucnaSprema.valueOf(s);
		System.out.println(ss);
		System.out.println(ss.getKoeficijent());

		KorisniciManager km = KorisniciManager.getInstance();
		km.ispisi();
//		Soba soba = new Soba();
//		System.out.println(soba.generisiDatume());
//		km.obrisiKorisnika("rade123");
//		km.azurirajKorisnike();
//		System.out.println("--------------------------------");
//		km.ispisiKorisnike();

 	}

}

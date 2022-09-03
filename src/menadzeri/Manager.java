package menadzeri;

import java.io.IOException;

public interface Manager {

//	public static Object getInstance()  throws IOException {
//		return null;
//	};
	
	void ucitaj() throws IOException;

	public void obrisi(String kljuc) throws IOException;

	public void dodaj(Object o, boolean dodajMapu) throws IOException;

//  jos treba istraziti ideju izmene, sta ce biti prosledjeno
	public void izmeni(Object o) throws IOException;

	public void azuriraj() throws IOException;

	public void ispisi() throws IOException;
	
	public Object get(String kljuc);

	void azurirajListe();

}

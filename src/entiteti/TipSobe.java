package entiteti;

public enum TipSobe {
	JEDNOKREVETNA, DVOKREVETNA1, DVOKREVETNA2, TROKREVETNA, CETVOROKREVETNA;
//  trebalo bi da svaki tip sadrzi svoj koef kojim se mnozi osnovica 
//	da bi se mogla izracunati cena boravka
//  a osnovica bi trebala da bude jedinstvena za svaki mesecni cenovnik
	public static boolean contains(String test) {
		for (TipSobe s : TipSobe.values()) {
			if (s.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}

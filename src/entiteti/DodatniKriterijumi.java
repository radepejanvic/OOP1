package entiteti;

public enum DodatniKriterijumi {
	WIFI, KLIMA, KADA, TUSKABINA, TERASA;
	
	public static boolean contains(String test) {
		for (DodatniKriterijumi d : DodatniKriterijumi.values()){
			if (d.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}

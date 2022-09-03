package entiteti;

public enum DodatneUsluge {
	DORUCAK, RUCAK, VECERA;
	
	public static boolean contains(String test) {
		for (DodatneUsluge d : DodatneUsluge.values()){
			if (d.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}

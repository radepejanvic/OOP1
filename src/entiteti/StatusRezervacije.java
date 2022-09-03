package entiteti;

public enum StatusRezervacije {
	NACEKANJU, POTVRDJENA, OTKAZANA, ODBIJENA;

	StatusRezervacije() {
	}

	public static boolean contains(String test) {
		for (StatusRezervacije s : StatusRezervacije.values()) {
			if (s.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

}

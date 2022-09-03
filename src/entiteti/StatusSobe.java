package entiteti;

public enum StatusSobe {
	SLOBODNA, ZAUZETA, SPREMANJE;

	StatusSobe() {
	}

//	funkcija za proveru unosa -> mozda nepotrebno ali cemo videti kasnije
	public static boolean contains(String test) {
		for (StatusSobe s : StatusSobe.values()) {
			if (s.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
// funkcija koja menja status na sledeci -> u navedenom redosledu
	public StatusSobe next() {
		StatusSobe[] ss = StatusSobe.values();
		int i = 0;
		while (ss[i] != this) {
			i++;
		}
		i++;
		i %= ss.length;
		return ss[i];
	}
}

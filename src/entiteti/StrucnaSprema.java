package entiteti;

public enum StrucnaSprema {
	I(1.29), II(1.65), III(2.00), IV(2.52), V(2.88), VI(3.09), VII1(3.40), VII2(3.71), VIII(4.12);

	double koeficijent;

	StrucnaSprema() {
	}

	StrucnaSprema(double koeficijent) {
		this.koeficijent = koeficijent;
	}

	public double getKoeficijent() {
		return this.koeficijent;
	}
	
	public static boolean contains(String test) {
		for (StrucnaSprema s : StrucnaSprema.values()){
			if (s.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

}

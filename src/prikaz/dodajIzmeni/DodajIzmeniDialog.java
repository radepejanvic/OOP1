package prikaz.dodajIzmeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entiteti.Korisnik;
import menadzeri.KorisniciManager;
import net.miginfocom.swing.MigLayout;

public abstract class DodajIzmeniDialog extends JDialog {
	private Object izmena;
	JFrame roditelj;
	
	public DodajIzmeniDialog(JFrame roditelj, Object izmena) {
		super(roditelj, true);
		this.roditelj = roditelj;
		if (izmena != null) {
			setTitle("Izmena");
		} else {
			setTitle("Dodavanje");
		}
		
		this.izmena = izmena;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return (str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()).trim();
	}
	
	protected abstract void initGUI(int selektovaniIndex, boolean zakucaj);
}

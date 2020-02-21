package ca.mcgill.ecse223.quoridor;

import java.awt.EventQueue;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.view.QuoridorMenu;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class QuoridorApplication {

	private static Quoridor quoridor;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuoridorMenu frame = new QuoridorMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
 		return quoridor;

	}	

	public static void setQuoridor(Quoridor quoridorInput) {
		quoridor = quoridorInput;
	}
	}


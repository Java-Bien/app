package controleur;

import cod.Professeur;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vue.FenCreerCours;
import vue.FenCreerProf;
import vue.FenEdt;
import vue.FenGererCours;
import vue.FenGererProf;
import vue.FenModifProf;

public class Main extends Application {
	static private FenEdt fEdt;
	static private FenCreerCours fCreerCours;
	static private FenGererProf fProf;
	static private FenGererCours fCours;
	static private FenModifProf fModifProf;
	static private FenCreerProf fNouvProf;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		fEdt = new FenEdt();
		fEdt.initModality(Modality.APPLICATION_MODAL);
		fEdt.setResizable(false);
		fProf = new FenGererProf();
		fProf.initModality(Modality.APPLICATION_MODAL);
		fCreerCours = new FenCreerCours();
		fCreerCours.initModality(Modality.APPLICATION_MODAL);
		fCours = new FenGererCours();
		fCours.initModality(Modality.APPLICATION_MODAL);
		fNouvProf = new FenCreerProf();
		fNouvProf.initModality(Modality.APPLICATION_MODAL);
		fEdt.show();
	}
	
	public static void ouvrirFenNouvCours() {
		fCreerCours.show();
	}
	
	public static void ouvrirFenGestionProf() {
		fProf.show();
	}
	public static void ouvrirFenNouvProf() {
		fNouvProf.show();
		}
	public static void fermerFenGestionProf() {
		fProf.close();
		}


	
	public static void ouvrirModifierProf(Professeur professeur) {
		 try {
		            // Fermer l'ancienne fenêtre si elle existe
		            if (fModifProf != null) {
		                fModifProf.close();
		            }
		            
		            // Créer une nouvelle fenêtre avec le professeur à modifier
		            fModifProf = new FenModifProf(professeur);
		            fModifProf.initModality(Modality.APPLICATION_MODAL);
		            fModifProf.show();
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		            System.err.println("Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
		        }
		}
	
	public static void ouvrirFenGestionCours() {
		fCours.show();
	}
	
	public static void fermerModifierProf() {
		fModifProf.close();
	}
	
	public static void fermerAppli() {
		System.exit(0);
	}
	
	public static void fermerFenNouvCours() {
		fCreerCours.close();
	}
	
	public static void fermerFenProf() {
		fProf.close();
	}
	
	public static void fermerFenCours() {
		fCours.close();
	}
	
	public static void fermerFenNouvProf() {
		fNouvProf.close();
	}

		
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
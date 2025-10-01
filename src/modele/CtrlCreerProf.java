package modele;

import cod.Admin;
import cod.Professeur;
import controleur.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CtrlCreerProf {

    @FXML
    private Button bnAnnuler;

    @FXML
    private Button bnValider;

    @FXML
    private TextField nomProf;

    @FXML
    private TextField prenomProf;

    @FXML
    private TextField specialiteProf;

    @FXML
    void creerProfesseur(ActionEvent event) {
    	 try {
             // Récupération des données des champs
             String nom = nomProf.getText().trim();
             String prenom = prenomProf.getText().trim();
             String specialite = specialiteProf.getText().trim();

             // Vérification des champs vides
             if (nom.isEmpty() || prenom.isEmpty() || specialite.isEmpty()) {
            	 showAlert("Erreur", "Tous les champs doivent être remplis.");
            	 return;
             }
             
          // Vérifie si les champs contiennent un chiffre ou un décimal
             if (estEntier(nom) || estEntier(prenom) || estEntier(specialite)) {
                 showAlert("Erreur", "Les champs ne doivent pas contenir de chiffres.");
                 return;
             }
             
             boolean existe = Admin.getProfesseurs().stream().anyMatch(p ->
             p.getNom().get().equalsIgnoreCase(nom) &&
             p.getPrenom().get().equalsIgnoreCase(prenom)
             );

             if (existe) {
            	 showAlert("Erreur", "Un professeur avec ce nom et prénom existe déjà.");
            	 return;
             }

             // Création du professeur
             Professeur prof = new Professeur(
                 new SimpleStringProperty(nom),
                 new SimpleStringProperty(prenom),
                 new SimpleStringProperty(specialite)
             );

             // Ajout à la liste
             Admin.ajouterProfesseur(prof);

             // Réinitialiser les champs
             nomProf.clear();
             prenomProf.clear();
             specialiteProf.clear();
         } catch (Exception e) {
             // Affichage d'une alerte en cas d'erreur
        	 showAlert("Erreur", e.getMessage());
        	 return;
         }
    }

    @FXML
    void fermerFen(ActionEvent event) {
    	Main.fermerFenNouvProf();
    }
    
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean estEntier(String str) {
    	return str.matches(".*\\d.*") || str.matches("\\d*(\\.\\d+)?");
	}
    
    public void initialize() {
    	bnValider.setDefaultButton(true);
        bnAnnuler.setCancelButton(true);
    }
}

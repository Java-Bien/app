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

public class CtrlModifProf {
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
	    
	 // Le professeur à modifier
	    private Professeur professeurAModifier;
	    	    
	    /**
	     * Méthode pour définir le professeur à modifier et pré-remplir les champs
	     */
	    public void setProfesseurAModifier(Professeur professeur) {
	        this.professeurAModifier = professeur;
	        preremplirChamps();
	    }
	    
	    /**
	     * Pré-remplit les champs avec les données du professeur existant
	     */
	    private void preremplirChamps() {
	        if (professeurAModifier != null) {
	            nomProf.setText(professeurAModifier.getNom().get());
	            prenomProf.setText(professeurAModifier.getPrenom().get());
	            specialiteProf.setText(professeurAModifier.getSpecialite().get());
	        }
	    }
	    
	    @FXML
	    void modifierProfesseur(ActionEvent event) {
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
	             
	          // Vérifier l'unicité seulement si le nom/prénom ont changé
	             String nomActuel = professeurAModifier.getNom().get();
	             String prenomActuel = professeurAModifier.getPrenom().get();
	             
	             if (!nom.equalsIgnoreCase(nomActuel) || !prenom.equalsIgnoreCase(prenomActuel)) {
	                 boolean existe = Admin.getProfesseurs().stream().anyMatch(p ->
	                     p != professeurAModifier && // Exclure le professeur actuel
	                     p.getNom().get().equalsIgnoreCase(nom) &&
	                     p.getPrenom().get().equalsIgnoreCase(prenom)
	                 );
	                 
	                 if (existe) {
	                     showAlert("Erreur", "Un autre professeur avec ce nom et prénom existe déjà.");
	                     return;
	                 }
	             }

	             // Créer et ajouter le nouveau
	             Professeur nouveau = new Professeur(
	            	 professeurAModifier.getId(),
	                 new SimpleStringProperty(nom),
	                 new SimpleStringProperty(prenom),
	                 new SimpleStringProperty(specialite)
	             );

	          // Remplacer dans la liste à la même position
	             int index = Admin.getProfesseurs().indexOf(professeurAModifier);
	             if (index >= 0) {
	                 Admin.getProfesseurs().set(index, nouveau);
	             }
	             
	 	    	Main.fermerModifierProf();

	         } catch (Exception e) {
	             // Affichage d'une alerte en cas d'erreur
	        	 showAlert("Erreur", e.getMessage());
	        	 return;
	         }
	    }
	    
	    @FXML
	    void fermerFen(ActionEvent event) {
	    	Main.fermerModifierProf();
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

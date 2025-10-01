package modele;

import cod.Admin;
import modele.CtrlEdt;
import cod.Cours;
import cod.Professeur;
import cod.Seance;
import controleur.Main;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CtrlCreerCours {
    @FXML
    private Button bnAnnuler;

    @FXML
    private Button bnValider;

    @FXML
    private DatePicker laDate;

    @FXML
    private TextField leLieu;

    @FXML
    private MenuButton menuNiveau;

    @FXML
    private MenuButton menuProfesseur;

    @FXML
    private TextField typeCours;
    
    @FXML
    private MenuButton menuJour;
    
    @FXML
    private TextField duree;

    @FXML
    private TextField heure;
    
    @FXML
    private Button bnSupprimer;
    
    @FXML
    private Button bnAjouter;
    
    @FXML
    private ListView<String> listeProfs;
    
    @FXML
    private Label listeProfAdd;
    
    private ObservableList<Professeur> selectedProfs = FXCollections.observableArrayList();
    private StringProperty lesProfs = new SimpleStringProperty("Aucun Prof");
    private String niveau;
    private String jour;
    
    private IntegerProperty laDuree = new SimpleIntegerProperty(0);
    private boolean isInitialized = false;

    
    @FXML
    void valider(ActionEvent event) throws Exception {
        try {
            // Vérifications préliminaires
            if (selectedProfs.isEmpty()) {
                throw new Exception("Aucun professeur sélectionné.");
            }
            
            if (typeCours.getText() == null || typeCours.getText().trim().isEmpty()) {
                throw new Exception("Le type de cours doit être spécifié.");
            }
            
            if (leLieu.getText() == null || leLieu.getText().trim().isEmpty()) {
                throw new Exception("Le lieu doit être spécifié.");
            }
            
            if (heure.getText() == null || heure.getText().trim().isEmpty()) {
                throw new Exception("L'heure doit être spécifiée.");
            }
            
            // NOUVELLE VALIDATION: Format d'heure
            String heureText = heure.getText().trim();
            if (!heureText.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                throw new Exception("L'heure doit être au format HH:MM (ex: 09:30, 14:00)");
            }
            
            // Vérifier que l'heure correspond à un créneau valide de l'EDT
            if (!isHeureValide(heureText)) {
                throw new Exception("L'heure doit correspondre à un créneau de l'EDT.\n" +
                    "Créneaux disponibles: 08:00, 08:30, 09:00, 09:30, 10:00, 10:30, 11:00, 11:30, " +
                    "12:00, 12:30, 13:00, 13:30, 14:00, 14:30, 15:00, 15:30, 16:00, 16:30, " +
                    "17:00, 17:30, 18:00, 18:30");
            }
            
            if (duree.getText() == null || duree.getText().trim().isEmpty()) {
                throw new Exception("La durée doit être spécifiée.");
            }
            
            // Vérifier que tous les profs ont la bonne spécialité
            for (Professeur prof : selectedProfs) {
                if (!prof.getSpecialite().get().equals(typeCours.getText().trim())) {
                    throw new Exception("Le professeur " + prof.getNom().get() + 
                        " n'a pas la spécialité requise (" + typeCours.getText().trim() + ")");
                }
            }
            
            // Créer UNE SEULE séance
            int durationInSeconds = Integer.parseInt(duree.getText().trim());
            laDuree.set(durationInSeconds);
            
            Seance nouvelleSeance = new Seance(
                new SimpleStringProperty(jour),
                new SimpleStringProperty(heureText), // Utiliser heureText validé
                new SimpleStringProperty(leLieu.getText().trim())
            );
            
            // Ajouter TOUS les professeurs à cette séance D'ABORD
            for (Professeur prof : selectedProfs) {
                nouvelleSeance.ajouterProfesseur(prof);
            }
            
            // Créer le cours et l'associer à la séance APRÈS avoir ajouté les professeurs
            Cours nouveauCours = new Cours(
                new SimpleStringProperty(typeCours.getText().trim()),
                laDuree,
                new SimpleStringProperty(niveau)
            );
            nouvelleSeance.setCours(nouveauCours);
            
            // Vérifier les conflits
            if (CtrlEdt.getInstance().detectionConflit(nouvelleSeance)) {
                CtrlEdt.getInstance().afficherAlerte(nouvelleSeance);
            } else {
                // Ajouter la séance à chaque professeur (important pour la persistance)
                for (Professeur prof : selectedProfs) {
                    prof.ajouterSeance(nouvelleSeance);
                }
                
                // Ajouter la séance à la liste globale
                CtrlEdt.getInstance().seances.add(nouvelleSeance);
                
                // Actualiser l'EDT ET le menu de filtrage
                CtrlEdt.getInstance().actualiserEdt();
                CtrlEdt.getInstance().remplirMenuFiltre(); // AJOUT IMPORTANT
                
                Main.fermerFenNouvCours();
            }
            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR, "La durée doit être un nombre valide.", ButtonType.OK);
            alert.setTitle("Erreur de saisie");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Erreur");
            alert.showAndWait();
        }
    }

    // MÉTHODE UTILITAIRE pour valider les heures
    private boolean isHeureValide(String heure) {
        // Liste des heures valides selon CtrlEdt
        String[] heuresValides = {
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", 
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", 
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", 
            "17:00", "17:30", "18:00", "18:30"
        };
        
        for (String heureValide : heuresValides) {
            if (heureValide.equals(heure)) {
                return true;
            }
        }
        return false;
    }
    
    @FXML
    void selectJour(ActionEvent event) {
        // Normaliser le jour pour être cohérent avec CtrlEdt
        if (jour != null) {
            jour = jour.substring(0, 1).toUpperCase() + jour.substring(1).toLowerCase();
            System.out.println("Jour sélectionné et normalisé : " + jour);
        }
    }
    
    @FXML
    void ajouterProf(ActionEvent event) {
        String selected = listeProfs.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            int debut = selected.indexOf("[") + 1;
            int fin = selected.indexOf("]");
            if (debut <= 0 || fin <= debut) return;
            
            int id = Integer.parseInt(selected.substring(debut, fin));
            
            for (Professeur prof : Admin.getProfesseurs()) {
                if (prof.getId().get() == id) {
                    if (!selectedProfs.contains(prof)) {
                        selectedProfs.add(prof);
                        editString();
                    }
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur lors du parsing de l'ID du professeur");
        }
    }
    
    @FXML
    void supprimerProf(ActionEvent event) {
        String selected = listeProfs.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            int debut = selected.indexOf("[") + 1;
            int fin = selected.indexOf("]");
            if (debut <= 0 || fin <= debut) return;
            
            int id = Integer.parseInt(selected.substring(debut, fin));
            
            for (Professeur prof : Admin.getProfesseurs()) {
                if (prof.getId().get() == id) {
                    selectedProfs.remove(prof);
                    editString();
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur lors du parsing de l'ID du professeur");
        }
    }
    
    
    void editString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selectedProfs.size(); i++) {
            Professeur prof = selectedProfs.get(i);
            builder.append(prof.getNom().get()).append(" ").append(prof.getPrenom().get());
            if (i < selectedProfs.size() - 1) {
                builder.append(", ");
            }
        }
        
        String result = builder.toString();
        lesProfs.set(result.isEmpty() ? "Aucun Prof" : result);
    }
    
    @FXML
    void fermerFen(ActionEvent event) {
        Main.fermerFenNouvCours();
    }
    
    public void initialize() throws Exception {
        // Ajouter des professeurs de test seulement s'ils n'existent pas déjà
        if (Admin.getProfesseurs().isEmpty()) {
            Admin.ajouterProfesseur(new Professeur(
                new SimpleStringProperty("Dupont"), 
                new SimpleStringProperty("Jean"), 
                new SimpleStringProperty("Danse classique")
            ));
            Admin.ajouterProfesseur(new Professeur(
                new SimpleStringProperty("Martin"), 
                new SimpleStringProperty("Sophie"), 
                new SimpleStringProperty("Jazz")
            ));
        }
        
        // Remplir la liste des professeurs
        listeProfs.getItems().clear();
        for (Professeur prof : Admin.getProfesseurs()) {
            String result = "[" + prof.getId().get() + "] " + prof.getNom().get() + 
                           " " + prof.getPrenom().get() + " - " + prof.getSpecialite().get();
            this.listeProfs.getItems().add(result);
        }
        
        String jourParDefaut = "Lundi";
        String niveauParDefaut = "Débutant";

        // Initialisation des variables
        jour = jourParDefaut;
        niveau = niveauParDefaut;

        // Initialisation des MenuButtons
        menuJour.setText(jourParDefaut);
        menuNiveau.setText(niveauParDefaut);
        
        // AMÉLIORATION: Ajouter des suggestions d'heures dans un tooltip ou placeholder
        heure.setPromptText("Format: HH:MM (ex: 09:30)");
        
        // AMÉLIORATION: Ajouter une validation en temps réel pour l'heure
        heure.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!newValue.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                    heure.setStyle("-fx-border-color: red;");
                } else if (!isHeureValide(newValue)) {
                    heure.setStyle("-fx-border-color: orange;");
                } else {
                    heure.setStyle("-fx-border-color: green;");
                }
            } else {
                heure.setStyle(""); // Reset style
            }
        });
        
        // AMÉLIORATION: Validation de la durée
        duree.setPromptText("Durée en heures (ex: 1, 2, 3)");
        duree.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    int dur = Integer.parseInt(newValue);
                    if (dur <= 0 || dur > 6) { // Limite raisonnable
                        duree.setStyle("-fx-border-color: red;");
                    } else {
                        duree.setStyle("-fx-border-color: green;");
                    }
                } catch (NumberFormatException e) {
                    duree.setStyle("-fx-border-color: red;");
                }
            } else {
                duree.setStyle("");
            }
        });
        
        // Configuration des listeners pour les menus
        for (MenuItem item : menuJour.getItems()) {
            item.setOnAction(e -> {
                String jourSelectionne = item.getText();
                
                // Normaliser le jour
                jourSelectionne = jourSelectionne.substring(0, 1).toUpperCase() + 
                                 jourSelectionne.substring(1).toLowerCase();
                
                menuJour.setText(jourSelectionne);
                jour = jourSelectionne;
                System.out.println("Jour sélectionné : " + jour);
            });
        }
        
        for (MenuItem item : menuNiveau.getItems()) {
            item.setOnAction(e -> {
                menuNiveau.setText(item.getText());
                niveau = item.getText();
                System.out.println("Niveau sélectionné : " + niveau);
            });
        }
        
        // Bindings pour activer/désactiver les boutons
        BooleanBinding isValid = Bindings.createBooleanBinding(() -> {
            boolean champsRemplis = typeCours.getText() != null && !typeCours.getText().trim().isEmpty()
                && leLieu.getText() != null && !leLieu.getText().trim().isEmpty()
                && heure.getText() != null && !heure.getText().trim().isEmpty()
                && duree.getText() != null && !duree.getText().trim().isEmpty();
            
            boolean profsSelectionnes = !selectedProfs.isEmpty();
            
            return champsRemplis && profsSelectionnes;
        }, 
        typeCours.textProperty(),
        leLieu.textProperty(),
        heure.textProperty(),
        duree.textProperty(),
        selectedProfs
        );
        
        bnValider.disableProperty().bind(isValid.not());
        
        // Bindings pour les boutons d'ajout/suppression de professeurs
        BooleanBinding profSelectionne = Bindings.createBooleanBinding(() ->
            listeProfs.getSelectionModel().getSelectedIndex() >= 0,
            listeProfs.getSelectionModel().selectedIndexProperty()
        );
        
        bnAjouter.disableProperty().bind(profSelectionne.not());
        bnSupprimer.disableProperty().bind(profSelectionne.not());
        
        // Binding pour l'affichage des professeurs sélectionnés
        listeProfAdd.textProperty().bind(lesProfs);
        
        isInitialized = true;
    }
}
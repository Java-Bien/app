package modele;

import cod.Admin;
import cod.Professeur;
import controleur.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class CtrlGererProf {

    @FXML
    private Button bnModifier;

    @FXML
    private Button bnNouveau;

    @FXML
    private Button bnSupprimer;
    
    @FXML
    private Button bnAnnuler;

    @FXML
    private ListView<Professeur> listeProf;

    @FXML
    void modifierProf(ActionEvent event) {
    Professeur profSel = listeProf.getSelectionModel().getSelectedItem();
        if (profSel != null) {
            // Passer le professeur sélectionné à la méthode d'ouverture
            Main.ouvrirModifierProf(profSel);
        }
    }

    @FXML
    void nouveauProf(ActionEvent event) {
    Main.ouvrirFenNouvProf();
    }

    @FXML
    void supprimerProf(ActionEvent event) {
    Professeur profSel = listeProf.getSelectionModel().getSelectedItem();
        if (profSel != null) {
            try {
                Admin.retirerProf(profSel);
            } catch (Exception e) {
                e.printStackTrace(); // ou afficher une alerte
            }
        }
    }
    
    @FXML
    void fermerFen(ActionEvent event) {
    Main.fermerFenGestionProf();
    }
    
    public void initialize() {
        listeProf.setItems(Admin.getProfesseurs());
        
        listeProf.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Professeur prof, boolean empty) {
                super.updateItem(prof, empty);
                if (empty || prof == null) {
                    setText(null);
                } else {
                    setText("[" + prof.getId().get() + "] " + prof.getNom().get() + " " + prof.getPrenom().get() + " - " + prof.getSpecialite().get());
                }
            }
        });
        
        bnSupprimer.disableProperty().bind(
            listeProf.getSelectionModel().selectedItemProperty().isNull()
        );
        
        bnModifier.disableProperty().bind(
            listeProf.getSelectionModel().selectedItemProperty().isNull()
        );
        
        bnAnnuler.setCancelButton(true);

    }

}
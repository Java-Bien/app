package modele;

import controleur.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CtrlGererCours {

    @FXML
    private Button bnAnnuler;

    @FXML
    private Button bnModifier;

    @FXML
    private Button bnSupprimer;

    @FXML
    private ListView<?> listeCours;

    @FXML
    void fermerFen(ActionEvent event) {
    	Main.fermerFenCours();
    }

    @FXML
    void modifCours(ActionEvent event) {

    }

    @FXML
    void supprimerCours(ActionEvent event) {

    }
    
    public void initialize() {
    	
    }

}

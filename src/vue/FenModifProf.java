package vue;

import java.io.IOException;

import cod.Professeur;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import modele.CtrlModifProf;
import javafx.fxml.FXMLLoader;

public class FenModifProf extends Stage {

private CtrlModifProf controleur;

public FenModifProf(Professeur professeur) throws IOException {
this.setTitle("Fenêtre de modification de professeur");
this.setResizable(false);
Scene laScene = new Scene(creerSceneGraph());
this.setScene(laScene);

// Passer le professeur au contrôleur après la création de la scène
        if (controleur != null) {
            controleur.setProfesseurAModifier(professeur);
        }
}

private TitledPane creerSceneGraph() throws IOException {
     FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/modifierProf.fxml"));
        TitledPane root = loader.load();
        
     // Récupérer le contrôleur pour pouvoir lui passer le professeur
        controleur = loader.getController();
        
     return root;
}
}
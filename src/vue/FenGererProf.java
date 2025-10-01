package vue;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FenGererProf extends Stage {
	public FenGererProf() throws IOException {
		this.setTitle("Fenêtre de gestion de professeur");
		this.setResizable(false);
		Scene laScene = new Scene(creerSceneGraph());
		this.setScene(laScene);
	}

	private TitledPane creerSceneGraph() throws IOException {
     	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gererProf.fxml"));
        TitledPane root = loader.load();
     	return root;
	}	
}

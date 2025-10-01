package vue;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FenGererCours extends Stage {
	public FenGererCours() throws IOException {
		this.setTitle("Fenêtre de gestion de cours");
		this.setResizable(false);
		Scene laScene = new Scene(creerSceneGraph());
		this.setScene(laScene);
	}

	private TitledPane creerSceneGraph() throws IOException {
     	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gererCours.fxml"));
        TitledPane root = loader.load();
     	return root;
	}	
}

package vue;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FenEdt extends Stage {
	public FenEdt() throws IOException {
		this.setTitle("Emploi du temps");
		this.setResizable(false);
		Scene laScene = new Scene(creerSceneGraph());
		this.setScene(laScene);
	}

	private Pane creerSceneGraph() throws IOException {
     	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/edt.fxml"));
        Pane root = loader.load();
     	return root;
	}	
}

package cod;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Admin {
    static ObservableList<Professeur> professeurs = FXCollections.observableArrayList();

    public static ObservableList<Professeur> getProfesseurs() {
        return professeurs;
    }

    public static void ajouterProfesseur(Professeur professeur) throws Exception {
        if (professeur != null) {
            if (!Admin.professeurs.contains(professeur)) {
                Admin.professeurs.add(professeur);
            } else throw new Exception("Le professeur existe déjà.");
        } else throw new Exception("Le professeur est invalide.");
    }

    public static void modifierProf(Professeur professeur, int index) throws Exception {
        if (index >= 0){
            if (Admin.professeurs.contains(professeur)){
                Admin.professeurs.set(index, professeur);
            } else throw new Exception("Le professeur n'existe pas");
        } else throw new Exception("Erreur l'index ne peut pas être inférieur à 0.");
    }

    public static void retirerProf(Professeur professeur) throws Exception {
        if (Admin.professeurs.contains(professeur)) {
            Admin.professeurs.remove(professeur);
            professeur = null;
            System.gc();
        } else throw new Exception("Le professeur n'existe pas.");
    }
}


package modele;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import controleur.Main;
import cod.Cours;
import cod.Professeur;
import cod.Seance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CtrlEdt {
    private Seance seanceEtendue = null;
    private Pane paneEtendu = null;
    private String professeurFiltre = "Tous";
    
    @FXML
    private MenuButton menuFiltre;
    @FXML
    private Button bnGerer;
    @FXML
    private Button bnNouvCours;
    @FXML
    private Button bnProf;
    @FXML
    private Button bnQuitter;
    @FXML
    private GridPane gridPaneEdt;
    
    private static CtrlEdt instance;
    
    public List<Seance> seances = new ArrayList<>();
    
    // Map pour convertir les jours en indices de colonnes
    private Map<String, Integer> jourEnColonne = new HashMap<>();
    
    // Map pour convertir les heures en indices de lignes
    private Map<String, Integer> heureEnIndice = new HashMap<>();

    public CtrlEdt() {
        // Initialisation des jours, avec leur numéro correspondant
        jourEnColonne.put("lundi", 1);
        jourEnColonne.put("mardi", 2);
        jourEnColonne.put("mercredi", 3);
        jourEnColonne.put("jeudi", 4);
        jourEnColonne.put("vendredi", 5);
        jourEnColonne.put("samedi", 6);
        jourEnColonne.put("dimanche", 7);
        
        // Heures de base de l'EDT
        heureEnIndice.put("08:00", 0);
        heureEnIndice.put("08:30", 0);
        heureEnIndice.put("09:00", 1);
        heureEnIndice.put("09:30", 1);
        heureEnIndice.put("10:00", 2);
        heureEnIndice.put("10:30", 2);
        heureEnIndice.put("11:00", 3);
        heureEnIndice.put("11:30", 3);
        heureEnIndice.put("12:00", 4);
        heureEnIndice.put("12:30", 4);
        heureEnIndice.put("13:00", 5);
        heureEnIndice.put("13:30", 5);
        heureEnIndice.put("14:00", 6);
        heureEnIndice.put("14:30", 6);
        heureEnIndice.put("15:00", 7);
        heureEnIndice.put("15:30", 7);
        heureEnIndice.put("16:00", 8);
        heureEnIndice.put("16:30", 8);
        heureEnIndice.put("17:00", 9);
        heureEnIndice.put("17:30", 9);
        heureEnIndice.put("18:00", 10);
        heureEnIndice.put("18:30", 10);
    }

    @FXML
    private void creaNouvCours(ActionEvent event) {
        Main.ouvrirFenNouvCours();
    }

    @FXML
    private void gestionCours(ActionEvent event) {
        Main.ouvrirFenGestionCours();
    }

    @FXML
    private void gestionProf(ActionEvent event) {
        Main.ouvrirFenGestionProf();
    }

    @FXML
    private void quitterEdt(ActionEvent event) {
        Main.fermerAppli();
    }

    @FXML
    private void initialize() throws Exception {
        instance = this;
        
        // Création des professeurs de test
        var p1 = new Professeur(
            new SimpleStringProperty("Vialat"), 
            new SimpleStringProperty("JC"), 
            new SimpleStringProperty("Tango Mexicain")
        );

        var p2 = new Professeur(
            new SimpleStringProperty("Peppa"), 
            new SimpleStringProperty("Pig"), 
            new SimpleStringProperty("Galipette")
        );
        
        var p3 = new Professeur(
            new SimpleStringProperty("Exempleaaa"), 
            new SimpleStringProperty("92i"), 
            new SimpleStringProperty("Paquetta")
        );
        
        // Création des séances de test
        var s1 = creerSeanceTest(p1, "mardi", "13:30", "Chez moi", 3);
        var s2 = creerSeanceTest(p2, "lundi", "10:30", "Chez lui", 1);
        var s4 = creerSeanceTest(p3, "mardi", "13:30", "Salle de danse(1)", 1);

        // Ajouter les séances en vérifiant les conflits
        ajouterSeanceAvecVerification(s1);
        ajouterSeanceAvecVerification(s2);
        ajouterSeanceAvecVerification(s4);
        
        actualiserEdt();
        remplirMenuFiltre();
        
        // Configuration du menu filtre
        for (MenuItem item : menuFiltre.getItems()) {
            item.setOnAction(e -> {
                String texteProfesseur = item.getText();
                menuFiltre.setText(texteProfesseur);
                
                if ("Tous".equals(texteProfesseur)) {
                    professeurFiltre = "Tous";
                } else {
                    professeurFiltre = texteProfesseur;
                }
                System.out.println("Filtre sélectionné : " + texteProfesseur);
                actualiserEdt();
            });
        }
    }
    
    private Seance creerSeanceTest(Professeur prof, String jour, String heure, String lieu, int duree) throws Exception {
        String specialite = prof.getSpecialite().get();
        String niveau = duree > 2 ? "Expert" : (duree > 1 ? "Intermédiaire" : "Débutant");
        
        var cours = new Cours(
            new SimpleStringProperty(specialite),
            new SimpleIntegerProperty(duree),
            new SimpleStringProperty(niveau)
        );
        
        var seance = new Seance(
            new SimpleStringProperty(jour),
            new SimpleStringProperty(heure),
            new SimpleStringProperty(lieu),
            cours
        );
        
        seance.ajouterProfesseur(prof);
        return seance;
    }
    
    private void ajouterSeanceAvecVerification(Seance seance) {
        if (detectionConflit(seance)) {
            afficherAlerte(seance);
        } else {
            seances.add(seance);
        }
    }
    
    public void actualiserEdt() {
        nettoyerSeances();
        
        List<Seance> seancesFiltrees = obtenirSeancesFiltrees();
        
        System.out.println("=== ACTUALISATION EDT ===");
        System.out.println("Nombre de séances filtrées: " + seancesFiltrees.size());
        
        // Grouper les séances par créneaux (jour + heure)
        Map<String, List<Seance>> seancesParCreneaux = new HashMap<>();
        
        for (Seance seance : seancesFiltrees) {
            String jour = seance.getJour().get().toLowerCase();
            String heure = seance.getHeure().get();
            String cle = jour + "_" + heure;
            
            System.out.println("Traitement séance: " + seance.getCours().getType().get() + 
                             " - " + jour + " " + heure + " (Clé: " + cle + ")");
            
            seancesParCreneaux.computeIfAbsent(cle, k -> new ArrayList<>()).add(seance);
        }
        
        System.out.println("Nombre de créneaux: " + seancesParCreneaux.size());
        
        // Affichage des séances groupées
        for (List<Seance> seancesDuCreneau : seancesParCreneaux.values()) {
            afficherSeancesCoteACote(seancesDuCreneau);
        }
        
        System.out.println("=== FIN ACTUALISATION ===");
    }
    
    public void ajouterNouvelleSeance(Seance seance) {
        System.out.println("Ajout nouvelle séance: " + seance.getCours().getType().get());
        seances.add(seance);
        actualiserEdt();
        remplirMenuFiltre();
    }
    
    private List<Seance> obtenirSeancesFiltrees() {
        if ("Tous".equals(professeurFiltre)) {
            return new ArrayList<>(seances);
        }
        
        // Filtrer par professeur
        List<Seance> seancesFiltrees = new ArrayList<>();
        for (Seance seance : seances) {
            for (Professeur prof : seance.getProf()) {
                String nomComplet = prof.getNom().get() + " " + prof.getPrenom().get();
                if (nomComplet.equals(professeurFiltre)) {
                    seancesFiltrees.add(seance);
                    break;
                }
            }
        }
        
        return seancesFiltrees;
    }
    
    public void remplirMenuFiltre() {
        // Vider le menu existant (sauf "Tous" s'il existe)
        menuFiltre.getItems().clear();
        
        MenuItem tousItem = new MenuItem("Tous");
        menuFiltre.getItems().add(tousItem);
        
        // Récupérer tous les professeurs uniques
        Set<String> nomsProfs = new HashSet<>();
        for (Seance seance : seances) {
            for (Professeur prof : seance.getProf()) {
                String nomComplet = prof.getNom().get() + " " + prof.getPrenom().get();
                nomsProfs.add(nomComplet);
            }
        }
        
        // Ajouter les professeurs au menu
        for (String nomProf : nomsProfs) {
            MenuItem item = new MenuItem(nomProf);
            menuFiltre.getItems().add(item);
        }
        
        // Reconfigurer les actions des items
        for (MenuItem item : menuFiltre.getItems()) {
            item.setOnAction(e -> {
                String texteProfesseur = item.getText();
                menuFiltre.setText(texteProfesseur);
                
                if ("Tous".equals(texteProfesseur)) {
                    professeurFiltre = "Tous";
                } else {
                    professeurFiltre = texteProfesseur;
                }
                System.out.println("Filtre sélectionné : " + texteProfesseur);
                actualiserEdt();
            });
        }
        
        // Texte par défaut
        if (menuFiltre.getText() == null || menuFiltre.getText().isEmpty()) {
            menuFiltre.setText("Tous");
        }
    }

    private void afficherSeancesCoteACote(List<Seance> seancesDuCreneau) {
        if (seancesDuCreneau.isEmpty()) return;
        
        Seance premiereSeance = seancesDuCreneau.get(0);
        String jour = premiereSeance.getJour().get().toLowerCase();
        String heure = premiereSeance.getHeure().get();
        
        Integer colonne = jourEnColonne.get(jour);
        Integer ligne = heureEnIndice.get(heure);
        
        System.out.println("Affichage séance(s) - Jour: " + jour + " (" + colonne + "), Heure: " + heure + " (" + ligne + ")");
        
        if (colonne != null && ligne != null) {
            boolean contientSeanceEtendue = seancesDuCreneau.contains(seanceEtendue);
            
            if (seancesDuCreneau.size() == 1) {
                // Une seule séance --> affichage normal
                afficherSeanceUnique(seancesDuCreneau.get(0), colonne, ligne);
            } else if (contientSeanceEtendue) {
                // Plusieurs séances avec une étendue --> afficher seulement la séance étendue
                afficherSeanceUnique(seanceEtendue, colonne, ligne);
            } else {
                // Plusieurs séances sans extension --> affichage côte à côte
                afficherSeancesMultiples(seancesDuCreneau, colonne, ligne);
            }
        } else {
            System.err.println("ERREUR: Impossible de placer la séance - Colonne: " + colonne + ", Ligne: " + ligne);
            System.err.println("Jour: '" + jour + "', Heure: '" + heure + "'");
        }
    }

    private void afficherSeanceUnique(Seance seance, int colonne, int ligne) {
        try {
            Pane paneSeance = creerPaneSeance(seance, 1.0);
            
            int duree = seance.getCours().getDuree().get();
            
            // Si c'est la séance étendue, afficher sur 2 lignes minimum
            if (seance == seanceEtendue) {
                duree = Math.max(duree, 2);
                paneEtendu = paneSeance;
            }
            
            if (duree > 1) {
                gridPaneEdt.add(paneSeance, colonne, ligne, 1, duree);
            } else {
                gridPaneEdt.add(paneSeance, colonne, ligne);
            }
            
            System.out.println("✓ Séance ajoutée avec succès: " + seance.getCours().getType().get() + 
                             " le " + seance.getJour().get() + " " + seance.getHeure().get() +
                             " (Col: " + colonne + ", Ligne: " + ligne + ")" +
                             (seance == seanceEtendue ? " (ÉTENDUE)" : ""));
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage de la séance unique: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherSeancesMultiples(List<Seance> seances, int colonne, int ligne) {
        try {
            boolean contientSeanceEtendue = seances.contains(seanceEtendue);
            
            javafx.scene.layout.HBox laHbox = new javafx.scene.layout.HBox();
            laHbox.setSpacing(2);
            laHbox.setPrefWidth(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
            laHbox.setMaxWidth(Double.MAX_VALUE);
            laHbox.setFillHeight(true);
            
            double largeurParSeance = 1.0 / seances.size();
            
            for (Seance seance : seances) {
                Pane paneSeance = creerPaneSeance(seance, largeurParSeance);
                javafx.scene.layout.HBox.setHgrow(paneSeance, javafx.scene.layout.Priority.ALWAYS);
                paneSeance.setMaxWidth(Double.MAX_VALUE);
                laHbox.getChildren().add(paneSeance);
            }
            
            int dureeMax = seances.stream()
                                .mapToInt(s -> s.getCours().getDuree().get())
                                .max()
                                .orElse(1);
            
            // Si une séance est étendue, augmenter la taille
            if (contientSeanceEtendue) {
                dureeMax = Math.max(dureeMax, 2);
            }
            
            if (dureeMax > 1) {
                gridPaneEdt.add(laHbox, colonne, ligne, 1, dureeMax);
            } else {
                gridPaneEdt.add(laHbox, colonne, ligne);
            }
            
            System.out.println("✓ Séances multiples ajoutées avec succès: " + seances.size() + 
                             " cours le " + seances.get(0).getJour().get() + 
                             " " + seances.get(0).getHeure().get() +
                             " (Col: " + colonne + ", Ligne: " + ligne + ")" +
                             (contientSeanceEtendue ? " (CONTIENT SÉANCE ÉTENDUE)" : ""));
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des séances multiples: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Pane creerPaneSeance(Seance seance, double facteurLargeur) {
        Pane paneSeance = new Pane();
        
        String[] couleurs = {"#98FB98", "#FFB6C1", "#87CEEB", "#DDA0DD", "#F0E68C", "#FFA07A"};
        int indexCouleur = Math.abs(seance.getCours().getType().get().hashCode()) % couleurs.length;
        
        paneSeance.setStyle("-fx-background-color: " + couleurs[indexCouleur] + 
                           "; -fx-color: black; -fx-border-color: black; -fx-border-width: 1;-fx-cursor: hand;");
        
        // Label avec les informations
        Label labelCours = new Label();
        String texteCours;
        
        if (seance == seanceEtendue) {
            // Affichage détaillé sur 2 lignes
            texteCours = seance.getCours().getType().get() + "\n" + 
                        seance.getLieu().get() + "\n" + 
                        seance.getCours().getNiveau().get() + "\n" + 
                        seance.obtenirNomsProfs() + "\n" +
                        "Durée: " + seance.getCours().getDuree().get() + "h\n" +
                        "Cliquez pour réduire";
        } else {
            // Affichage compact normal
            texteCours = seance.getCours().getType().get() + " - " + seance.getCours().getNiveau().get() + "\n" + 
                        seance.getLieu().get() + " - " + seance.obtenirNomsProfs();
        }
        
        labelCours.setText(texteCours);
        labelCours.setStyle("-fx-text-fill: darkblue; -fx-font-size: " + 
                           (facteurLargeur < 0.7 ? "8" : "10") + "px; -fx-padding: 2;");
        labelCours.setWrapText(true);
        
        if (facteurLargeur < 1.0) {
            paneSeance.setMinWidth(25); // Largeur minimale
            paneSeance.setPrefWidth(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
            paneSeance.setMaxWidth(Double.MAX_VALUE);
        }
        
        paneSeance.getChildren().add(labelCours);
        
        paneSeance.setOnMouseClicked(event -> {
            gererClicSeance(seance);
        });
        
        return paneSeance;
    }
    
    private void gererClicSeance(Seance seance) {
        if (seanceEtendue == seance) {
            // Si c'est la séance étendue, la réduire
            seanceEtendue = null;
            paneEtendu = null;
        } else {
            // Sinon, l'étendre
            seanceEtendue = seance;
        }
        
        actualiserEdt();
    }
    
    public static boolean ontProfCommun(Seance seance1, Seance seance2) {
        ArrayList<Professeur> lesProfs1 = seance1.getProf();
        ArrayList<Professeur> lesProfs2 = seance2.getProf();
        
        for (Professeur prof : lesProfs1) {
            if (lesProfs2.contains(prof)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean ontHoraireCommun(Seance seance1, Seance seance2) {
        return seance1.getHeure().get().equals(seance2.getHeure().get());
    }
    
    public static boolean ontJourCommun(Seance seance1, Seance seance2) {
        return seance1.getJour().get().equals(seance2.getJour().get());
    }
    
    public boolean detectionConflit(Seance nouvelleSeance) {
        for (Seance laSeance : seances) {
            if (ontHoraireCommun(laSeance, nouvelleSeance) && 
                ontJourCommun(laSeance, nouvelleSeance) && 
                ontProfCommun(laSeance, nouvelleSeance)) {
                return true;
            }
        }
        return false;
    }
    
    public void afficherAlerte(Seance seance) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Conflit d'horaire détecté");
        alert.setHeaderText("Conflit d'horaire");
        
        // Construire la liste des noms des professeurs
        StringBuilder nomsProfs = new StringBuilder();
        for (int i = 0; i < seance.getProf().size(); i++) {
            Professeur prof = seance.getProf().get(i);
            nomsProfs.append(prof.getNom().get()).append(" ").append(prof.getPrenom().get());
            if (i < seance.getProf().size() - 1) {
                nomsProfs.append(", ");
            }
        }
        
        String message = String.format(
            "Erreur : Le(s) professeur(s) '%s' ont déjà un cours programmé le %s à %s.\n" +
            "Impossible d'ajouter cette séance.",
            nomsProfs.toString(),
            seance.getJour().get(),
            seance.getHeure().get()
        );
        
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Méthode pour nettoyer toutes les séances existantes de l'EDT en gardant les jours et les heures
    private void nettoyerSeances() {
        // Liste des éléments à supprimer
        var elementsASupprimer = new ArrayList<javafx.scene.Node>();
        
        for (javafx.scene.Node node : gridPaneEdt.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            
            // Condition permettant de différencier les entêtes/heures des séances
            if (node instanceof Pane && colIndex != null && rowIndex != null && 
                colIndex > 0 && rowIndex > 0) {
                elementsASupprimer.add(node);
            } else if (node instanceof javafx.scene.layout.HBox && colIndex != null && rowIndex != null && 
                      colIndex > 0 && rowIndex > 0) {
                elementsASupprimer.add(node);
            }
        }
        
        // Suppression des éléments
        gridPaneEdt.getChildren().removeAll(elementsASupprimer);
        System.out.println("Nettoyage effectué: " + elementsASupprimer.size() + " éléments supprimés");
    }
    
    public void ajouterSeance(Seance seance) {
        this.seances.add(seance);
        actualiserEdt();
    }
    
    // Méthode pour récupérer l'instance
    public static CtrlEdt getInstance() {
        return instance;
    }
    
    public void rafraichirEdt() {
        actualiserEdt();
    }
    
    // Méthode pour supprimer une séance
    public void supprimerSeance(Seance seance) {
        seances.remove(seance);
        
        // Si la séance supprimée était étendue, réinitialiser
        if (seanceEtendue == seance) {
            seanceEtendue = null;
            paneEtendu = null;
        }
        
        actualiserEdt();
        remplirMenuFiltre(); // Mettre à jour le filtre au cas où un prof n'ait plus de cours
    }
    
    // Getter pour accéder aux séances (utile pour d'autres contrôleurs)
    public List<Seance> getSeances() {
        return new ArrayList<>(seances);
    }
}
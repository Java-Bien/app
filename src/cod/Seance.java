package cod;

import java.util.ArrayList;

import javafx.beans.property.StringProperty;

public class Seance {
    private static final String[] JOURS = {"lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche"};

    private StringProperty jour;
    private StringProperty heure;
    private StringProperty lieu;
    private Cours cours;
    private ArrayList<Professeur> professeurs;

    public Seance(StringProperty jour, StringProperty heure, StringProperty lieu) throws Exception {
        if (this.isJour(jour.get().toLowerCase())) {
            this.jour = jour;
        } else throw new Exception("Le jour est invalide.");

        if (this.isHeure(heure)) {
            this.heure = heure;
        } else throw new Exception("L'heure est invalide.");

        this.lieu = lieu;

        this.professeurs = new ArrayList<>();
    }
    
    public Seance(StringProperty jour, StringProperty heure, StringProperty lieu, Cours cours) throws Exception {
        if (this.isJour(jour.get().toLowerCase())) {
            this.jour = jour;
        } else throw new Exception("Le jour est invalide.");

        if (this.isHeure(heure)) {
            this.heure = heure;
        } else throw new Exception("L'heure est invalide.");

        this.lieu = lieu;
        this.cours = cours;

        this.professeurs = new ArrayList<>();
    }
    
    

    public Cours getCours() {
        return this.cours;
    }
    public StringProperty getJour() {
        return this.jour;
    }

    public StringProperty getHeure() {
        return this.heure;
    }

    public StringProperty getLieu() {
        return this.lieu;
    }

    public ArrayList<Professeur> getProf() {
        return this.professeurs;
    }

    public void setJour(StringProperty jour) throws Exception {
        if (this.isJour(jour.get().trim().toLowerCase())) {
            this.jour = jour;
        } else throw new Exception("Le jour est invalide.");
    }

    public void setHeure(StringProperty heure) throws Exception {
        if (this.isHeure(heure)) {
            this.heure = heure;
        }  else throw new Exception("L'heure est invalide.");
    }

    public void setLieu(StringProperty lieu) {
        this.lieu = lieu;
    }

    public void setProfesseurs(ArrayList<Professeur> professeurs) {
        this.professeurs = professeurs;
    }

    public void setCours(Cours cours) throws Exception {
        boolean ok = false;
        for (Professeur professeur : this.professeurs) {  // <-- Cette liste doit être remplie !
            if (cours.getType().get().equals(professeur.getSpecialite().get())) {
                ok = true;
                break;
            }
        }
        if (ok) {
            this.cours = cours;
        } else throw new Exception("Le professur n'est pas adapté au cours.");
    }

    public boolean isJour(String jour) {
        boolean result = false;
        for (String JOUR : JOURS) {
            if  (jour.equals(JOUR)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    public String obtenirNomsProfs() {
        String resultat="";
        for(Professeur leProf:professeurs) {
            resultat += leProf.getNom().get() + " ";
        }
        return resultat;
    }

    public boolean isHeure(StringProperty heure) {
        String heureStr = heure.get();
        
        // Check if format is HH:MM (like "08:30", "13:30")
        if (heureStr.length() == 5 && heureStr.contains(":")) {
            String[] parts = heureStr.split(":");
            if (parts.length == 2) {
                try {
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);
                    
                    // Valid hour range: 8-18, valid minute range: 0-59
                    return (hour >= 8 && hour <= 18) && (minute >= 0 && minute <= 59);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        
        // Check if format is just HH (like "08", "13") - keeping backward compatibility
        if (heureStr.length() == 2) {
            try {
                int hour = Integer.parseInt(heureStr);
                return hour >= 8 && hour <= 18;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return false;
    }

    public void ajouterProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
    }

    public void supprimerProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof Seance seance) {
            if (seance.jour.equals(this.jour) && seance.heure.equals(this.heure) && seance.lieu.equals(this.lieu)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Seance[jour=" + this.jour + ", heure=" + this.heure + ", lieu=" + this.lieu + "]";
    }
}

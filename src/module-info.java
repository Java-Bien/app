/**
 * 
 */
/**
 * 
 */
module Appli201 {
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	
	opens controleur to javafx.graphics, javafx.fxml, javafx.base;
	opens modele to javafx.graphics, javafx.fxml, javafx.base;
	opens vue to javafx.graphics, javafx.fxml, javafx.base;
}
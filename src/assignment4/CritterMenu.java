/*
 * EE 422C Fall 2016, Quiz 7
 * Name: Grant Guglielmo
 * UT EID: gg25488
 * Unique: 16470
 */
package assignment4;

import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



@SuppressWarnings("unused")
public class CritterMenu extends Application{
	public static void menu(){
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Critters");
		ChoiceBox<String> box = new ChoiceBox<String>();
		box.getItems().addAll("Alage","Craig","Critter1","Critter2","Critter3","Critter4");
		GridPane root = new GridPane();
		root.getColumnConstraints().add(new ColumnConstraints(100));
	    root.getRowConstraints().add(new RowConstraints(50));
		box.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
                try {
					Critter.makeCritter(box.getValue());
				} catch (InvalidCritterException e) {
					e.printStackTrace();
				}
                
            }
        });
		root.add(box, 1, 1);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}
	
}

package ui.view.universe;

import engine.universe.Country;
import engine.universe.SolarSystem;
import engine.universe.Universe;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import ui.view.solarsystem.SolarSystemThread;

import javax.swing.*;
import java.net.URL;
import java.util.*;

/**
 * Created by bob on 5/7/2016.
 *
 */
public class UniverseController implements Initializable {

	@FXML
	Accordion accordion;
	@FXML
	MenuItem  about;
	@FXML
	MenuItem closeButton;
	@FXML
	SwingNode swingNode;
	@FXML
	AnchorPane anchorPane;
	public static AnchorPane pane;
	@FXML
	AnchorPane threeDPane;
	/**
	 * Called to initialize a controller after its root element has been
	 * completely processed.
	 *
	 * @param location  The location used to resolve relative paths for the root object, or
	 *                  <tt>null</tt> if the location is not known.
	 * @param resources The resources used to localize the root object, or <tt>null</tt> if
	 */
	PerspectiveCamera camera;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		swingNode.setContent(new UniverseJPanel(Universe.universe));
		for(SolarSystem s: Universe.universe.getSolarSystems()) {
			VBox pane = new VBox();
			pane.getChildren().add(new Text(s.toString()));
			Button button = new SolarSystemButton(s, "Go To SolarSystem", Universe.playersCountry);
			pane.getChildren().add(button);
			accordion.getPanes().add(
					new TitledPane(s.name, pane));
			Sphere solarSystemSphere = new Sphere(100);
			solarSystemSphere.setTranslateX(0);
			solarSystemSphere.setTranslateY(0);
			solarSystemSphere.setTranslateZ(0);
			threeDPane.getChildren().add(solarSystemSphere);
			camera = new PerspectiveCamera();
		}
	}

	public class SolarSystemButton extends Button
	{
		private SolarSystem solarSystem;
		private Country playersCountry;

		public SolarSystemButton(SolarSystem s, String string, Country playersCountry)
		{
			super(string);
			solarSystem = s;
			this.playersCountry = playersCountry;
			super.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					new SolarSystemThread(solarSystem, SolarSystemButton.this.playersCountry).run();
				}
			});
		}
	}


	@FXML
	public void onClose()
	{
		// TODO: 5/8/2016 implement global close
		System.exit(0);
	}
}

package ui.view;

import engine.cities.City;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by bob on 5/15/2016.
 */
class CityButton extends Button {
	private City city;
	private Controller controller;
	public CityButton(City c, String s,Controller controller) {
		super(s);
		city = c;
		this.controller = controller;
		super.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.switchTo(city);
			}
		});
	}
}
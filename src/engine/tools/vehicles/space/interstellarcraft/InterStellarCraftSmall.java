package engine.tools.vehicles.space.interstellarcraft;

import engine.planets.LocationPlanet;
import engine.tools.AttackableConstants;
import engine.tools.vehicles.VehicleInitialConstants;
import engine.universe.ResourceDemand;

//import static engine.planets.Road.resistance;

/**
 * Created by bob on 4/7/2016.
 */
public class InterStellarCraftSmall extends InterStellarCraft
{
	public static int maxPassengersInitial;
	public static double maxWeightInitial;
	public static double startHealthInitial;
	public static double resistanceInitial;

	protected InterStellarCraftSmall(LocationPlanet locationPlanet) {
		super(new VehicleInitialConstants(new AttackableConstants(startHealthInitial,resistanceInitial,locationPlanet),maxPassengersInitial,maxWeightInitial));
	}

	@Override
	public ResourceDemand requiredResourcesForConstruction() {
		return null;// TODO: 4/7/2016
	}

	@Override
	public long getManDaysForConstruction() {
		return 0;// TODO: 4/7/2016
	}

	@Override
	public double getWeight() {
		return 0;// TODO: 4/7/2016
	}
}

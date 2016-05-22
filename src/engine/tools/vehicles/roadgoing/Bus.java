package engine.tools.vehicles.roadgoing;

import engine.tools.vehicles.VehicleInitialConstants;
import engine.universe.ResourceDemand;

/**
 * Created by bob on 4/3/2016.
 */
public class Bus extends RoadGoing {
	public static int maxPassengersInitial;
	public static double maxWeightInitial;
	public static double startHealthInitial;
	public static double resistanceInitial;

	protected Bus(VehicleInitialConstants vehicleInitialConstants, int numToolsConstructor) {
		super(vehicleInitialConstants, numToolsConstructor);
	}

	@Override
	public double getSpeed() {
		return 0;
	}

	@Override
	public ResourceDemand requiredResourcesForConstruction() {
		return null;
	}

	@Override
	public double getconstructionManDays() {
		return 0;
	}
}

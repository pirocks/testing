package engine.buildings.housing;

import engine.cities.CityBlock;
import engine.people.cityworkers.CityWorker;

import java.util.ArrayList;

//deprecate??
public class WealthyWorkersHouseBlock extends Housing
{
	public static int maximumOccupancyInitial = 500;
	public static double costInitial;
	public static double resistanceInitial;

	public WealthyWorkersHouseBlock(ArrayList<CityWorker> residents, CityBlock parentBlock) {
		super(residents, parentBlock);
	}
//these classes do't really do anything
}
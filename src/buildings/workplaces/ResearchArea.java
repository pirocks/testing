





























package buildings.workplaces;

import buildings.Workplace;
import cities.CityBlock;
import people.CityWorker;
import science.Discovery;
import universe.MoneySource;

import java.util.ArrayList;

public class ResearchArea extends Workplace
{
	public static int maximumOccupancyInitial = -1;
	public static double costInitial;
	public static double resistanceInitial;
	private Discovery discovery;

	public ResearchArea(Type type, ArrayList<CityWorker> workers, CityBlock parentBlock, MoneySource owner) {
		super(type, workers, parentBlock, owner);
	}
}
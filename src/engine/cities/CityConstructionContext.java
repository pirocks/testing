package engine.cities;

import engine.buildings.housing.ApartmentBlock;
import engine.buildings.housing.WorkersHouseBlock;
import engine.planets.Grid;
import engine.planets.GridConstructionContext;
import engine.planets.LocationPlanet;
import engine.planets.TerrainType;
import engine.universe.Country;
import engine.universe.MoneySource;
import engine.universe.UniversalConstants;
import engine.universe.utils;

import java.util.ArrayList;

/**
 * Created by bob on 4/10/2016.
 *
 */
public class CityConstructionContext {
	ArrayList<LocationPlanet> buildingLocations;//this is an arraylist which makes the entire city attackable and allows for the building sites to be pre-determined
	int population;
	MoneySource moneySourceForBuildings;//the money ource used by buildings to pay staff
	private TerrainType terrainType;

	public TerrainType getTerrainType() {
		return terrainType;
	}

	//two basic types for now
	//industrial cities are porer than scientific and havve obvious differences in buildings
	enum Type
	{
		Industrial,Scientific
	}
	Type type;
	@Deprecated  ArrayList<CityConstructionContext> roadsTo;// I wn't use this, I'll have the AI catch up afterwards
	Country parentCountry;
	Grid parentGrid;
	public CityConstructionContext(GridConstructionContext gridConstructionContext, TerrainType terrainType, Grid parentGrid)
	{
		this.terrainType = terrainType;
		this.parentGrid = parentGrid;
		double industryProb = gridConstructionContext.industryProb;
		double rand = Math.random();
		if(industryProb < rand)
			type = Type.Industrial;
		else
			type = Type.Scientific;
		parentCountry = parentGrid.getParentCountry();
		population = (int) (UniversalConstants.peoplePerCity* Math.sqrt(Math.random()));
		int numBuildings;
		if(type == Type.Industrial)
			numBuildings = (int) (3.5*population/ ApartmentBlock.maximumOccupancyInitial);
		else if(type == Type.Scientific)
			numBuildings = (int) (3.5*population/ WorkersHouseBlock.maximumOccupancyInitial);
		else
			throw new UnsupportedOperationException();
		int centerx = utils.getRandomInt(0,100);
		int centery = utils.getRandomInt(0,100);
		buildingLocations = new ArrayList<>();
		buildingLocations.add(new LocationPlanet(parentGrid,centerx,centery));
		for(int count = 0; count < numBuildings;count++)
		{
			buildingLocations.add(getSuitableLocation(buildingLocations,parentGrid,centerx,centery,1));
		}
	}
	public LocationPlanet getSuitableLocation(ArrayList<LocationPlanet> usedLocations,Grid grid,int centerx,int centery,int searchDistance)
	{
		int dx = utils.getRandomInt(-1*searchDistance,searchDistance);
		int dy = utils.getRandomInt(-1*searchDistance,searchDistance);
		int finalx = centerx + dx;
		int finaly = centery + dy;
		if (finalx < 100)
			if (finaly < 100)
				if (finalx > 0)
					if (finaly > 0) {
						if (usedLocations.contains(new LocationPlanet(grid, finalx, finaly)))
							return getSuitableLocation(usedLocations, grid, centerx, centery, searchDistance + 1);
						else
							return new LocationPlanet(grid, finalx, finaly);
					}
		if(searchDistance < 100) {
			return getSuitableLocation(usedLocations, grid, centerx, centery, searchDistance + 1);
		}
		else {
			System.out.println(usedLocations.size());
			return getSuitableLocation(usedLocations, grid, centerx, centery, searchDistance);
		}
	}
}

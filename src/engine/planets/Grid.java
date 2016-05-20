package engine.planets;

import engine.cities.*;
import engine.planets.hazards.*;
import engine.universe.Country;
import engine.universe.CountryContainer;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by bob on 3/5/2016.
 * 
 * grids contain 100 cityblocks. Not in an array though
 */

public class Grid implements Serializable,PlanetContainer,CountryContainer, CityContainer
{
    private int x,y;
    private Planet parentPlanet;
    private Country parentCountry;

    private ArrayList<City> citys;
	private ArrayList<NaturalResource> naturalResources;
    private TerrainType terrainType;
    private ArrayList<NaturalHazard> hazards;
    private FarmLand farmLand;
   /* public Grid(int x, int y,Country parentCountry,Planet parentPlanet)
    {
	    registerCountryContainer();
	    registerPlanetContainer();
	    registerCityContainer();
        this.x = x;
        this.y = y;
        this.parentCountry = parentCountry;
        this.parentPlanet = parentPlanet;
    }*/

    public Grid(GridConstructionContext gridConstructionContext,Planet parentPlanet){
        registerPlanetContainer();// TODO: 5/10/2016 go through and check for thsese in all of the construction context  constructors
        registerCountryContainer();
	    registerCityContainer();
	    this.parentPlanet = parentPlanet;
	    x = gridConstructionContext.x;
	    y = gridConstructionContext.y;
	    naturalResources = gridConstructionContext.naturalResources;
	    terrainType = gridConstructionContext.getSuitableTerrainType();
	    hazards = new ArrayList<>();
	    double hazardProb = Math.random();
	    if(hazardProb < gridConstructionContext.hazardAbundance)
	    {
		    hazards.add(getRandomHazard());
		    hazardProb = Math.random();
		    if(hazardProb < gridConstructionContext.hazardAbundance)
		    {
			    hazards.add(getRandomHazard());
			    hazardProb = Math.random();
			    if(hazardProb < gridConstructionContext.hazardAbundance)
			    {
				    hazards.add(getRandomHazard());
			    }//up to three natural hazards
		    }
	    }
	    farmLand = new FarmLand(this);
	    //nowwe deal with  countries
	    parentCountry = gridConstructionContext.country;
	    //now we deal with the citys
	    if(gridConstructionContext.country == null)
	    {
		    //unclaimed land
		    citys = new ArrayList<>();
	    }
	    else {
		    gridConstructionContext.country.getGrids().add(this);
		    citys = new ArrayList<>();
		    double cityProb = gridConstructionContext.citiesPerGrid;
		    double rand = Math.random();
		    if(rand < cityProb)
		    {
			    try {
				    City city = new City(new CityConstructionContext(gridConstructionContext, terrainType, this));
				    citys.add(city);
			    } catch (ToManyPeopleException e) {
				    e.printStackTrace();
////			    assert (false);//who cares
//			    throw new UnsupportedOperationException(e);
			    }
		    }
	    }
	    // TODO: 5/10/2016
    }

	private NaturalHazard getRandomHazard() {
		int type = (int) (Math.random()*5);
		switch (type)
		{
			case 0:
				return new Disease(this);
			case 1:
				return new Drought(this);
			case 2:
				return new Volcano(this);
			case 3:
				return new Weather(this);
			case 4:
				return new Earthquake(this);
			default:
				assert(false);
				throw new IllegalStateException();
		}
	}

	public Country getParentCountry()
    {
        return parentCountry;
    }
    public void registerHazard(NaturalHazard hazard)//natural hzard v regular hazzrad
    {
        //TODO:figure this one out
    }
    public ArrayList<NaturalHazard> getHazards()
    {
        return hazards;
    }
    public ArrayList<NaturalResource> getNaturalResources()
    {
        return naturalResources;
    }
    public boolean cityBlockLocationExists(int x, int y)
    {
        ArrayList<CityBlock> blocks = new ArrayList<>();
        for(City city:citys)
            blocks.addAll(city.getCityBlocks());
        for(CityBlock cityBlock:blocks)
            if(cityBlock.getXInGrid() == x && cityBlock.getYInGrid() == y)
                return true;
        return false;
    }
    //location stuff
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public double getXInPlanet()
    {
        return (double)x;
    }
    public double getYInPlanet()
    {
        return (double)y;
    }
    public Grid getGrid()
    {
        return this;
    }
    public double getXInGrid()
    {
        return 0;
    }
    public double getYInGrid()
    {
        return 0;//maybe I should return the center
    }
    public double getZInUniverse()//top left corner of grid not actual center
    {
        double planetZ = parentPlanet.getZInUniverse();
        double planetHeight = parentPlanet.getplanetRadius();
        double startHeight = planetZ + planetHeight;
        double gridHeight = planetHeight*2/parentPlanet.getGridCountHeight();///should be built in as a constant and final. Planets should not be created that don't have the appropriate height
        return startHeight - y*gridHeight;
    }
    public double getXInUniverse()
    {
        double xAngle = ((double)x/(double)parentPlanet.getGridCountLength())*360.0;
        double planetX = parentPlanet.getXInUniverse();
        return planetX + (parentPlanet.getplanetRadius()*Math.cos(xAngle));
    }
    public double getYInUniverse()
    {
        double yAngle = ((double)y/(double)parentPlanet.getGridCountLength())*360.0;
        double planetY = parentPlanet.getYInUniverse();
        return planetY + (parentPlanet.getplanetRadius()*Math.sin(yAngle));
    }
    public Planet getParentPlanet()
    {
        return parentPlanet;
    }
	@Override
	public void remove(City city) {
		citys.remove(city);
	}

	@Override
	public void remove(Country country,Country conqueror) {
		if(parentCountry == country) {
			parentCountry = conqueror;
			assert(false);// TODO: 4/10/2016 get rid of this
        }
	}

	@Override
	public void remove(Planet planet) {
		if(parentPlanet == planet)
		{
			parentPlanet = null;
			assert(false);
		}
	}

	@Override
	public String toString() {
		return "x:" + x + "y:" + y;// + "types:" + terrainType.toString();
	}

	public ArrayList<City> getCitys() {
        return citys;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public FarmLand getFarmLand() {
        return farmLand;
    }
}

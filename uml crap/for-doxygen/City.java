package engine.cities;

import engine.buildings.Building;
import engine.buildings.BuildingContainer;
import engine.buildings.housing.Housing;
import engine.buildings.housing.RulersHouse;
import engine.buildings.workplaces.Hospital;
import engine.buildings.workplaces.Workplace;
import engine.people.AbstractPerson;
import engine.people.PersonContainer;
import engine.people.cityworkers.Bureaucrat;
import engine.people.cityworkers.CityWorker;
import engine.universe.Country;
import engine.universe.CountryContainer;
import engine.planets.Grid;
import engine.planets.LocationPlanet;
import engine.tools.AttackableConstants;
import engine.tools.weapons.Attackable;
import engine.universe.MoneySource;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bob on 3/5/2016.
 *
 */

public class City  extends MoneySource implements Serializable,Attackable ,BuildingContainer, CountryContainer, PersonContainer
{
	//extejnds moneysource so serializable not necesary
	public static double resistanceInitial;
	public static double healthInitial;
	//remeber to add stuff to thhe unique id if I add more member vars
    //read the above comment
//    private MoneySource moneySource;
    private boolean isCapital;
    private int x,y;//center of city in grid//will be townhall locatiuon
    private Grid parentGrid;//can be used to find location//engine.cities limited to one grid
    // private ArrayList<Grid> grids;//not yet
    private ArrayList<CityBlock> cityBlocks;
    private ArrayList<Hospital> hospitals;
    public ArrayList<CityWorker> residents;
//    public ArrayList<CityWorker> unemployedResidents;
    private Country parentCountry;//make sutre to change when cuity is captured.
	private AttackableConstants attackableConstants;
    public City(boolean isCapital,Grid parentGrid,Country parentCountry,double wealth, int x, int y) {
        super(wealth);
        if(x > 100 || x < 0)
            throw new IllegalArgumentException();
        if(y > 100 || y < 0)
            throw new IllegalArgumentException();
		attackableConstants = new AttackableConstants(healthInitial,resistanceInitial);
        registerBuildingContainer();
	    registerCountryContainer();
	    registerPersonContainer();
	    this.isCapital = isCapital;
        this.x = x;
        this.y = y;
        this.parentGrid = parentGrid;
        this.parentCountry = parentCountry;
    }
    public ArrayList<CityBlock> getCityBlocks()
    {
        return cityBlocks;
    }
    public Building getCapitalBuilding() {
        assert(isCapital);
        for(CityBlock cityBlock:cityBlocks)
            if(cityBlock.getBuilding() instanceof RulersHouse)
                return cityBlock.getBuilding();
        assert(false);
        return null;
    }
    public double getXInPlanet() {
        double GridX = (double)parentGrid.getX();
        return GridX + x/100.0;
    }
    public double getYInPlanet() {
        double GridY = (double)parentGrid.getY();
        return GridY + y/100.0;
    }
    public Country getParentCountry()
    {
        return parentCountry;
    }
    public Grid getGrid()
    {
        return parentGrid;
    }
    public double getXInGrid()
    {
        return x;
    }
    public double getYInGrid()
    {
        return y;
    }
    public Hospital getLeastLoadedHosital() {
        Hospital leastLoadedHospital = hospitals.get(0);
        for(Hospital hospital:hospitals)
            if(leastLoadedHospital.getWorkLoad() > hospital.getWorkLoad())
                leastLoadedHospital = hospital;
        return leastLoadedHospital;
    }
    public void leavePerson(AbstractPerson person) {
	    for(Hospital hospital:hospitals)
	    {
		    hospital.leavePerson(person);
	    }
	    assert(residents.contains(person));
	    residents.remove(person);

    }
    //all people that are homeless are also jobless
	public ArrayList<CityWorker> getHomeless(){
	    ArrayList<CityWorker> out = new ArrayList<>();
	    for(CityWorker worker:residents)
	    {
		    if(worker.getHome() == null)
			    out.add(worker);
	    }
	    return out;
    }
	public ArrayList<CityWorker> getJobLess(){
		ArrayList<CityWorker> out = new ArrayList<>();
		for(CityWorker worker:residents) {
			worker.doLife(0);//garuntees that everything in the class is in order
			if (worker.getWorkBuilding() == null)
				out.add(worker);
		}
		return out;
	}
	public Housing findEmptyHousing(int minimumFree){
		for(CityBlock block:cityBlocks){
			if(block.getBuilding() instanceof Housing)
				if(((Housing) block.getBuilding()).getFreeSpace() >= minimumFree)
					return (Housing) block.getBuilding();
		}
		return null;
	}

	public void doLife(long time){
		for(CityWorker worker: residents)
		{
			worker.doLife(time);//// TODO: 4/13/2016 bureucrats and rulers should run the city
		}
	}

	@Override
    public void remove(Building building) {
        for(CityBlock cityBlock:cityBlocks)
            cityBlock.remove(building);
        hospitals.remove(building);
    }
    @Override
    public void remove(AbstractPerson person) {
		residents.remove(person);
    }
    @Override
    public void remove(Country country,Country conqueror) {
		if(parentCountry == country) {
			parentCountry = conqueror;
			assert (false);// TODO: 4/10/2016 get rid of this
			//die();//??
		}
    }
	//TODO: if damage is greater than a certain number pass to city otherwise go to random cityblock
	@Override
	public boolean receiveDamage(double damage) {
		return attackableConstants.receiveDamage(damage,this);
	}

	@Override
    public void die() {
        CityContainers.remove(this);// TODO: 4/9/2016 make sure that this kills everything

    }

    @Override
    public LocationPlanet getLocationPlanet() {
        return new LocationPlanet(this);
    }
}
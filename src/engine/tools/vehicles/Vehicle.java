package engine.tools.vehicles;

import engine.cities.Container;
import engine.people.AbstractPerson;
import engine.planets.Grid;
import engine.planets.LocationPlanet;
import engine.planets.Planet;
import engine.planets.TerrainType;
import engine.tools.Tool;
import engine.tools.vehicles.air.Aircraft;
import engine.tools.vehicles.sea.SeaCraft;
import engine.tools.vehicles.space.SpaceCraft;
import engine.tools.weapons.Attackable;
import engine.tools.weapons.Weapon;
import engine.universe.MoneySource;
import engine.universe.Resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Vehicle extends Tool implements Liver,Container
{
	private Set<AbstractPerson> passengers;
	private Set<Resource> cargo;
	private Set<Weapon> weapons;
	private Set<Vehicle> vehicles;
	private int maxPassengers;
	private double maxWeight;
	protected Vehicle(VehicleInitialConstants vehicleInitialConstants, int numToolsConstructor) {
		super(vehicleInitialConstants.attackableConstants, numToolsConstructor);
		registerLiver();
		maxPassengers = vehicleInitialConstants.maxPassengers;
		maxWeight = vehicleInitialConstants.maxWeight;
		passengers = new HashSet<>();
		cargo = new HashSet<>();
		weapons = new HashSet<>();
		vehicles = new HashSet<>();
		registerContainer(passengers);
		registerContainer(weapons);
		registerContainer(vehicles);
		for (LocationPlanet locationPlanet : vehicleInitialConstants.attackableConstants.locationPlanet) {
			Planet p = locationPlanet.getPlanet();
			if(p != null) {
				Grid grid = p.getGrids()[locationPlanet.getGridy()][locationPlanet.getGridx()];
				grid.vehicleArrives(this);
			}//hacky
			// TODO: 5/30/2016 fix this
		}
	}
	public abstract boolean inSpaceQ();
	public abstract boolean inWaterQ();
	public abstract double getSpeed();
	public boolean weaponQ() {
		return false;
	}
	public boolean vehicleQ()
	{
		return true;
	}
	public void loadObject(Weighable weighable) throws ToHeavyException {
		if(canAddObject(weighable)) {
			if (weighable instanceof Vehicle) {
				vehicles.add((Vehicle) weighable);
				registerContainer((Attackable) weighable);
			}
			else if(weighable instanceof Weapon) {
				weapons.add((Weapon) weighable);
				registerContainer((Attackable) weighable);

			}
			else if(weighable instanceof Resource) {
				cargo.add((Resource) weighable);
			}
			else if(weighable instanceof AbstractPerson) {
				if(passengers.size() < maxPassengers) {
					passengers.add((AbstractPerson) weighable);
					registerContainer((Attackable) weighable);
				}
			}
			else
				throw new IllegalStateException();
		}
		else
			throw new Weighable.ToHeavyException(weighable);
	}
	public Vehicle unloadVehicle(Vehicle vehicle){
		if(vehicles.contains(vehicle))
		{
			vehicles.remove(vehicle);
			deregisterContainer(vehicle);
		}
		else
		{
			throw  new IllegalStateException();
		}
		return vehicle;
	}
	public Resource unloadResource(Resource resource){
		if(cargo.contains(resource))
		{
			cargo.remove(resource);
		}
		else
		{
			throw  new IllegalStateException();
		}
		return resource;
	}
	public AbstractPerson unloadPerson(AbstractPerson abstractPerson){
		if(passengers.contains(abstractPerson))
		{
			passengers.remove(abstractPerson);
			deregisterContainer(abstractPerson);
		}
		else
		{
			throw  new IllegalStateException();
		}
		return abstractPerson;
	}
	public Weapon unloadWeapon(Weapon weapon){
		if(weapons.contains(weapon))
		{
			weapons.remove(weapon);
			deregisterContainer(weapon);
		}
		else
		{
			throw  new IllegalStateException();
		}
		return weapon;
	}
	public  boolean canAddObject(Weighable weighable) {
		return canAddWeight(weighable.getWeight());
	}
	private boolean canAddWeight(double weight) {
		return getTotalWeightLoad() + weight < maxWeight;
	}
	private double getTotalWeightLoad() {
		double out = 0;
		for(AbstractPerson abstractPerson:passengers)
			out += abstractPerson.getWeight();
		for(Resource resource: cargo)
			out += resource.getWeight();
		for(Weapon weapon: weapons)
			out += weapon.getWeight();
		for(Vehicle vehicle: vehicles)
			out += vehicle.getWeight();
		return out;
	}
	public void die() {
		Container.kill(new ArrayList<Attackable>(passengers));
		Container.kill(new ArrayList<Attackable>(weapons));
		Container.kill(new ArrayList<Attackable>(vehicles));
	}
	private void remove(AbstractPerson person) {
		passengers.remove(person);
	}
	private void remove(Vehicle vehicle) {
		vehicles.remove(vehicle);
	}
	private void remove(Weapon weapon) {
		weapons.remove(weapon);
	}
	private void remove(MoneySource in) {
		for (AbstractPerson passenger : passengers) {
			if(passenger.moneySource == in)
				passenger.moneySource = null;
		}
	}
	public void setDestination(LocationPlanet destination) {
		path = new Path(this.getLocation().get(0),destination,this instanceof Aircraft || this instanceof SeaCraft|| this instanceof SpaceCraft,!(this instanceof Aircraft));
		System.out.println(destination.toString());
		System.out.println(getLocation().get(0).toString());
		this.destination = destination;
	}

	public class Path{
		ArrayList<LocationPlanet> locationPlanets;
		public Path(ArrayList<LocationPlanet> locationPlanets) {
			if(locationPlanets == null)
				throw new IllegalArgumentException();
			this.locationPlanets = locationPlanets;
		}
		public Path(Path path,LocationPlanet locationPlanet){
			if(path.locationPlanets == null)
				throw new IllegalArgumentException();
			locationPlanets = new ArrayList<>();
			locationPlanets.addAll(path.locationPlanets);
			locationPlanets.add(locationPlanet);
		}
		public Path(LocationPlanet locationPlanet) {
			this(new ArrayList<LocationPlanet>(){{add(locationPlanet);}});
		}
		public Path(LocationPlanet begin,LocationPlanet end,boolean waterOkQ,boolean landOkQ){
			try {
				locationPlanets = getAllPathToDestination(begin,end,waterOkQ,landOkQ).locationPlanets;
			} catch (NullPointerException ignored) {

			}
		}
		public double getLength(){
			double out = 0;
			LocationPlanet prev = locationPlanets.get(0);
			for (LocationPlanet locationPlanet : locationPlanets) {
				out += locationPlanet.distanceBetween(prev);
				prev = locationPlanet;
			}
			return out;
		}
		public ArrayList<Path> getPathsGoingFrom(boolean waterOkQ,boolean landOkQ){
			LocationPlanet start = getLast();
			ArrayList<Path> possiblePaths = new ArrayList<>();
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy());}},waterOkQ,landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy());}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy() + 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy() + 1);}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy() - 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx(),start.getGridy() - 1);}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy());}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy());}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy() + 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy() +  1);}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy() - 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() + 1,start.getGridy() - 1);}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy());}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy());}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy() + 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy() + 1);}}));
			if(validQ(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy() - 1);}},waterOkQ,
					landOkQ))
				possiblePaths.add(new Path(this,new LocationPlanet(start){{moveGrid(start.getGridx() - 1,start.getGridy() - 1);}}));
			return possiblePaths;
		}
		private boolean validQ(Path previous,LocationPlanet locationPlanet, boolean waterOkQ,boolean landOkQ) {
			if(locationPlanet.equals(previous.getLast()))
				return false;
			Grid grid;
			try{
				grid = locationPlanet.getGrid();
			}catch (ArrayIndexOutOfBoundsException e){
				return false;
			}
			if(grid.getTerrainType() == TerrainType.Sea){
				if(waterOkQ){
					return true;
				}
			}else{
				if(landOkQ){
					return true;
				}
			}
			return false;
		}
		public LocationPlanet getLast() {
			return locationPlanets.get(locationPlanets.size() - 1);
		}
		public ArrayList<Path> getAllPathToDestination(Path begin, LocationPlanet end, int depth, boolean waterOkQ, boolean landOkQ){
			if(end == begin.getLast())
				return new ArrayList<Path>(){{add(begin);}};
			if(depth == 0){
				return new ArrayList<>();
			}
			ArrayList<Path> paths = new ArrayList<>();
			for (Path path : begin.getPathsGoingFrom(waterOkQ, landOkQ)) {
				ArrayList<Path> allPathToDestination = getAllPathToDestination(path, end, depth - 1, waterOkQ, landOkQ);
				if(allPathToDestination.size() > 0)
					return allPathToDestination;
			}
			return paths;
		}
		public Path getAllPathToDestination(LocationPlanet begin,LocationPlanet end,boolean waterOkQ,boolean landOkQ){
			ArrayList<Path> allPathToDestination = getAllPathToDestination(new Path(new LocationPlanet(begin)), end, 10, waterOkQ, landOkQ);
			if(allPathToDestination.size() == 0)
				return null;
			return allPathToDestination.get(0);
		}
	}

	private Path path;
	private LocationPlanet destination;
	public LocationPlanet getDestination() {
		return destination;
	}
	@Override
	public boolean sanityCheck() {
		if(passengers == null)
			throw new IllegalStateException();
		if(cargo == null)
			throw new IllegalStateException();
		if(weapons == null)
			throw new IllegalStateException();
		if(maxPassengers < 0)
			throw new IllegalStateException();
		double weight = 0;
		for (AbstractPerson passenger : passengers) {
			weight += passenger.getWeight();
		}
		for (Resource resource : cargo) {
			weight += resource.getWeight();
		}
		for (Weapon weapon : weapons) {
			weight += weapon.getWeight();
		}
		if(weight > getWeight())
			throw new IllegalStateException();
		if(numTools  <= 0){
			Liver.deregister(this);
		}
		return true;
	}
	@Override
	public void doLife(double time) {
		if(path != null && path.locationPlanets != null && path.locationPlanets.size() != 0) {
			assert (getLocation().size() == 1);
			Grid initialGrid = getLocation().get(0).getGrid();
			try {

				for (LocationPlanet locationPlanet : getLocation()) {
						locationPlanet.goTowards(path.locationPlanets.get(0), (getSpeed() * time) / (12 * 60 * 60), false,this instanceof SeaCraft || this instanceof Aircraft || this instanceof SpaceCraft,this instanceof SpaceCraft);
				}
			} catch (LocationPlanet.InTheOceanException e) {
				throw new IllegalStateException();
			}

			Grid finalGrid = getLocation().get(0).getGrid();
			if(finalGrid != initialGrid) {
				initialGrid.vehicleLeaves(this);
				finalGrid.vehicleArrives(this);
			}
			for (AbstractPerson passenger : passengers) {
				passenger.setLocation(this.location);
			}
			for (Resource resource : cargo) {}
			for (Weapon weapon : weapons) {
				weapon.setLocation(this.location);
			}
			for (Vehicle vehicle : vehicles) {
				vehicle.setLocation(this.location);
			}
		}
	}
	@Override
	public void remove(Attackable attackable) {
		if(attackable instanceof Weapon) {
			remove((Weapon) attackable);
		}
		else if(attackable instanceof Vehicle) {
			remove((Vehicle)attackable);
		}
		else if(attackable instanceof AbstractPerson){
			remove((AbstractPerson)attackable);
		}

	}
	public Set<AbstractPerson> getPassengers() {
		return passengers;
	}
	public Set<Resource> getCargo() {
		return cargo;
	}
	public Set<Vehicle> getVehicles() {
		return vehicles;
	}
	public Set<Weapon> getWeapons() {
		return weapons;
	}
	//color adjust is what I should se with javafx
}
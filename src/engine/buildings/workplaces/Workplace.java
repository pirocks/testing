package engine.buildings.workplaces;

//TODO figure out what  the worplace classes actually do.

import engine.buildings.Building;
import engine.cities.CityBlock;
import engine.people.AbstractPerson;
import engine.people.PersonContainer;
import engine.people.cityworkers.CityWorker;
import engine.universe.MoneySource;
import engine.universe.MoneySourceContainer;

import java.util.ArrayList;
public abstract class Workplace extends Building  implements PersonContainer, MoneySourceContainer
{
	// MoneySource owner;//for salaries//actually not needed
	private ArrayList<CityWorker> workers;
	private int maxWorkers;
	private MoneySource owner;
	public Workplace(Type type,ArrayList<CityWorker> workers,CityBlock parentBlock,MoneySource owner)
	{
		super(type,parentBlock,false);
		this.workers = workers;
		this.owner = owner;
	}
	public boolean isEmployee(CityWorker worker)
	{
		for(CityWorker c:workers)
			if(c == worker)
				return true;
		return false;
	}
	public boolean canAddWorker(CityWorker worker)
	{
		return worker.getPopulation() + workerCount() < maxWorkers;
	}
	public void addWorker(CityWorker worker) throws IllegalStateException
	{
		if(canAddWorker(worker))
			workers.add(worker);
		else
			throw new IllegalStateException();
	}
	public int workerCount()
	{
		int sum = 0;
		for(CityWorker worker:workers)
			sum += worker.getPopulation();
		return sum;
	}
	public void leavePerson(AbstractPerson person)
	{
		assert(workers.contains(person));
		workers.remove(person);
	}
	public void remove(AbstractPerson abstractPerson)
	{
		workers.remove(abstractPerson);
	}
	public void remove(MoneySource moneySource)
	{
		if(owner == moneySource)
		{
			owner = null;
		}
	}
}
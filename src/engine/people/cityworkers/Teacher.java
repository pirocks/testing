package engine.people.cityworkers;

import engine.cities.City;
import engine.people.AbstractPerson;
import engine.planets.LocationPlanet;
import engine.universe.UniversalConstants;

public class Teacher<Type extends AbstractPerson> extends CityWorker implements Cloneable
{
    public double progress = 0.0;//from 0 to 1

    public Teacher(Teacher<Type> typeTeacher) {
        super(typeTeacher);
        //two teachers cannot work on same student
	    //in the conext of this cnstructor studdent i most likely nullanyway
	    student = null;
	    progress = 0;
    }

    public Type getStudent() {
        return student;
    }

    private Type student;

    public static int populationInitial = 250;
    public static double foodUsePerPersonInitial = UniversalConstants.normalFoodUsePerPerson;
    public static double crimeRiskInitial = UniversalConstants.normalCrimeRisk;
    public static double crimeImpactInitial = UniversalConstants.normalPersonCrimeImpact;
    public static double salaryInitial = UniversalConstants.normalPersonSalary;

    public Teacher(City parentCity,LocationPlanet location) {
        super(new PeopleInitialConstants(populationInitial,
                foodUsePerPersonInitial,
                crimeRiskInitial,
                crimeImpactInitial,
                salaryInitial,
                parentCity.getParentCountry(),location),parentCity);
    }

    public void doSkill(double time) {
        progress += population*time/(AbstractPerson.timeToTrain*student.getPopulation());
	    if(progress > 1.0)
		    studentCompletedHandler(student);
    }

	private void studentCompletedHandler(Type student) {
		// TODO: 5/29/2016
	}

	@Override
    protected CityWorker splitInternal() {
        return new Teacher<>(this);
    }

}







































package engine.people.cityworkers;

import engine.buildings.housing.Housing;
import engine.buildings.workplaces.Hospital;
import engine.cities.City;
import engine.people.AbstractPerson;

//import engine.buildings.workplace.*;


public class Doctor extends CityWorker
{
    private static long timeToHealOnePerson = 3600*24;
    private Hospital workplace;
    public Hospital getWorkBuilding()
    {
        return workplace;
    }

    @Override
    public void setWorkPlaceToNull() {
        workplace = null;
    }

    public Doctor(City parentCity, Housing home)
    {
        super(AbstractPerson.Type.Doctor,parentCity,home);
    }
    public void doSkill(long time)//time is in seconds
    {
        //use productivity;
        //not sure if this is good yet
        CityWorker target = workplace.getNextPatient();//mybe make this an array or city
        double workDone = productivity*time/timeToHealOnePerson;
        int pop = target.getPopulation();
        double healthIncrease = workDone/((double)pop);
        if(1.0 - target.getHealth() < healthIncrease)
            healthIncrease = 1.0 - target.getHealth();
        if(salaryGiver.canPay(salary*healthIncrease))
        {    
            target.increaseHealth(healthIncrease);
            salaryGiver.pay(this,salary*healthIncrease);
            if(target.getHealth() > 0.99)
            {
                target.leaveHospital();
                if(!workplace.releasePatient(target))
                    assert(false);
            }
        }
        else
        {
            salaryGiver.outOfMoneyHandler(salary*healthIncrease);
        }
    }

    @Override
    public double getWeight() {
        return 0;// TODO: 4/9/2016
    }
}
package engine.universe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by bob on 3/5/2016.
 * Created by bob on 3/5/2016.
 * todo serializable won't work on container classes need to fix
 */
 

public class Universe implements Serializable, CountryContainer
{
	public transient static Universe universe;// TODO: 5/8/2016 make this non tranient//  figure out how this will load
	private ArrayList<SolarSystem> solarSystems;
	private ArrayList<Country> countries;
    private Universe(UniverseConstructionContext u,int numSolarSystems,double size)//size is not related to engine.universe units
    {
        registerCountryContainer();
        solarSystems = new ArrayList<>();
	    countries = new ArrayList<>();
        for(int i = 0; i < numSolarSystems;i++)
        {
            //location of solar systems
            BigDecimal largeNumber = new BigDecimal(1000000000);
            double x = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            double y = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            double z = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            solarSystems.add(new SolarSystem(new SolarSystemConstructionContext(u)));
        }
	    universe = this;
    }
	@Deprecated public Universe(UniverseConstructionContext universeConstructionContext)
	{
		this(universeConstructionContext,universeConstructionContext.numSolarSystems,universeConstructionContext.universeRadius);
	}
    @Override
    public void remove(Country country,Country conqueror) {
        countries.remove(country);
    }

	public ArrayList<SolarSystem> getSolarSystems() {
		return solarSystems;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

}

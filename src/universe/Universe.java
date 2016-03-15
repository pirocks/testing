package universe;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.io.Serializable;
/**
 * Created by bob on 3/5/2016.
 * Created by bob on 3/5/2016.
 */
 

public class Universe extends UniqueId implements Serializable
{
    private ArrayList<SolarSystem> solarSystems;
    private ArrayList<Country> countries;
    public Universe(int numSolarSystems,double size)
    {
        solarSystems = new ArrayList<SolarSystem>();
        for(int i = 0; i < numSolarSystems;i++)
        {
            //location of solar systems
            final double centerX = 0;
            final double centerY = 0;
            final double centerZ = 0;
            double x = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            double y = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            double z = ThreadLocalRandom.current().nextDouble(-size/2, size/2);
            solarSystems.add(new SolarSystem(new BigDecimal(x),new BigDecimal(y),new BigDecimal(z)));
        }
    }
}

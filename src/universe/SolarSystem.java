package universe;

import planets.Planet;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Created by bob on 3/5/2016.
 */
public class SolarSystem
{
    BigDecimal x,y,z;
    private ArrayList<Planet> planets = new ArrayList<Planet>();
    private ArrayList<Double> radii = new ArrayList<>();
    public SolarSystem(BigDecimal x,BigDecimal y, BigDecimal z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public BigDecimal getXInUniverse()
    {
        return x;
    }
    public BigDecimal getYInUniverse()
    {
        return y;
    }
    public BigDecimal getZInUniverse()
    {
        return z;
    }


}
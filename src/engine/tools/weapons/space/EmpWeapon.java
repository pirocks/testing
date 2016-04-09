package engine.tools.weapons.space;

import engine.tools.ToolInitialConstants;
import engine.universe.ResourceDemand;

import java.math.BigDecimal;

/**
 * Created by bob on 4/6/2016.
 *
 */
public class EmpWeapon extends SpaceWeapon {
	public static double startHealthInitial;
	public static double resistanceInitial;
	public static double damageInitial;
	public static BigDecimal rangeInitial;

	public EmpWeapon() {
		super(new ToolInitialConstants(startHealthInitial,resistanceInitial), damageInitial,rangeInitial);
	}

	@Override
	public ResourceDemand requiredResourcesForConstruction() {
		return null;// TODO: 4/8/2016
	}

	@Override
	public long constructionManHours() {
		return 0;// TODO: 4/8/2016
	}

	@Override
	public double getWeight() {
		return 0;// TODO: 4/8/2016
	}
}

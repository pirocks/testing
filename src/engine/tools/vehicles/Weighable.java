package engine.tools.vehicles;

/**
 * Created by bob on 4/7/2016.
 *
 */
public interface Weighable
{
	double getWeight();
	int getCount();
	default double getWeightActual()
	{
		return getCount()*getWeight();
	}
	class ToHeavyException extends Exception
	{
		public Weighable weighable;
		public ToHeavyException(Weighable weighable)
		{
			super();
			this.weighable = weighable;
		}
	}
}

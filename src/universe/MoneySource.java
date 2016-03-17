package universe;

public class MoneySource
{
    private double wealth;
    public MoneySource(double wealth)
    {
        this.wealth = wealth;
    }
    public void recieve(double amount)
    {
        wealth += amount;
    }
    public void pay(MoneySource target,double amount)
    {
        assert(canPay(amount));
        target.recieve(amount);
        wealth -= amount;
    }
    public double getWealth()
    {
        return wealth;
    }
    public boolean canPay(double amount)
    {
        if(amount > wealth)
            return false;
        return true;
    }

}
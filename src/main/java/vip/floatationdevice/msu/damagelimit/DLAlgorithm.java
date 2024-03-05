package vip.floatationdevice.msu.damagelimit;

public class DLAlgorithm
{
    enum Type
    {
        LINEAR,
        LOGARITHMIC
    }

    /**
     * Logarithmic function with the custom base.
     * @param b The value of base.
     * @param x The input variable.
     */
    public static double logb(double b, double x)
    {
        if(b <= 0.0 || b == 1.0)
            throw new ArithmeticException("Argument \"b\" expected to be in (0.0, 1.0)∪(1.0, inf) but found " + b);
        if(x <= 0.0)
            throw new ArithmeticException("Argument \"x\" expected to be in (0.0, inf) but found " + b);

        return Math.log(x) / Math.log(b);
    }

    /**
     * Limit the value linearly after a given threshold.
     * @param x The input variable.
     * @param T The value of threshold.
     * @param F The slope, must be in [0.0, 1.0].
     */
    public static double lmtLinear(double x, double T, double F)
    {
        if(T < 0.0)
            throw new IllegalArgumentException("Argument \"T\" expected to be in [0.0, inf) but found " + T);
        if(F < 0.0 || F > 1.0)
            throw new IllegalArgumentException("Argument \"F\" expected to be in [0.0, 1.0] but found" + F);

        if(x <= T)
            return x;
        else
            return F * (x - T) + T;
    }

    /**
     * Limit the value logarithmically after a given threshold.
     * @param x The input variable.
     * @param T The value of threshold.
     * @param F The base, must be greater than 1.0.
     */
    public static double lmtLogarithmic(double x, double T, double F)
    {
        if(T < 0.0)
            throw new IllegalArgumentException("Argument \"T\" expected to be in [0.0, inf) but found " + T);
        if(F <= 1.0)
            throw new IllegalArgumentException("Argument \"F\" expected to be in (1.0, inf) but found" + F);

        if(x <= T)
            return x;
        else
            return logb(F, x - T + 1 / Math.log(F)) + T - logb(F, 1 / Math.log(F));
    }
}

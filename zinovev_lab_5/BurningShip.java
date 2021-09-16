package zinovev_lab_5;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{

    //максимальное число итераций
    public static final int MAX_ITERATIONS = 2000;

    //метод подсчета количества итераций для точки
    public int numIterations(double x, double y) {
        Complex z = new Complex(0, 0);
        Complex c = new Complex(x, y);

        int iterations = MAX_ITERATIONS;
        //количество итераций для точки по формуле z(n)=(|RE(z(n-1)| + i*|Im(Zn-1)|)^2 + c
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Complex newZ = z.abs().multiply(z.abs()).add(c);
            //условие выхода при |z| > 2
            if (newZ.absSqr() > 4) {
                iterations = i;
                break;
            }
            z = newZ;
        }

        //возвращается -1, чтобы показать точка не выходит за границы
        //иначе возвращается количество итераций для точки
        if (iterations == MAX_ITERATIONS) {
            return -1;
        } else {
            return iterations;
        }
    }

    //метод устанавливает начальный диапазон в (-2 - 2.5i) (2 + 1.5i)
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    public String toString() {
        return "zinovev_lab_5.BurningShip";
    }
}

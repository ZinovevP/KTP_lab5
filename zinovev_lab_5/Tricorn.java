package zinovev_lab_5;

import zinovev_lab_5.Complex;
import zinovev_lab_5.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    //максимальное число итераций
    public static final int MAX_ITERATIONS = 2000;

    //метод подсчета количества итераций для точки
    public int numIterations(double x, double y) {
        Complex z = new Complex(0, 0);
        Complex c = new Complex(x, y);

        int iterations = MAX_ITERATIONS;
        //количество итераций для точки по формуле z(n)=zi(n-1)^2 + c
        //zi - комплексное сопряжение z
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Complex newZ = z.inverseI().multiply(z.inverseI()).add(c);
            //условие выхода при |z| > 2
            if (newZ.absSqr() > 4) {
                iterations = i;
                break;
            }
            z = newZ;
        }

        //возвращается -1, чтобы показать  точка не выходит за границы
        //иначе возвращается количество итераций для точки
        if (iterations == MAX_ITERATIONS) {
            return -1;
        } else {
            return iterations;
        }
    }

    //метод устанавливает начальный диапазон в (-2 - 2i)  (2 + 2i)
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    public String toString() {
        return "zinovev_lab_5.Tricorn";
    }
}

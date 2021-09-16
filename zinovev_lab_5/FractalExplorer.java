package zinovev_lab_5;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class FractalExplorer {
    //ширина и высота отображения в пикселях
    private int displaySize;
    //отображение
    private JImageDisplay imageDisplay;
    //базовый лкасс фракталов (для использования разных)
    private FractalGenerator gen;
    //диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double rec;

    //конструктор. size - размер отображения
    public FractalExplorer (int size) {
        displaySize = size;
        gen = new Mandelbrot();
        imageDisplay = new JImageDisplay(size, size);
        rec = new Rectangle2D.Double();
        gen.getInitialRange(rec);
    }

    // создание и показ интерфейса
    private void createAndShowGUI() {
        //создание основного окна с заголовком
        JFrame frame = new JFrame();
        frame.setTitle("Fractal explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //создание панели для кнопок Save и Reset и добавление их
        JPanel buttonPanel = new JPanel();
        JButton reset = new JButton("Reset display");
        JButton save = new JButton("Save image");
        buttonPanel.add(reset, BorderLayout.WEST);
        buttonPanel.add(save, BorderLayout.EAST);

        //создание панели для списка фракталов и надписи и добавление их
        JPanel fractalPanel = new JPanel();
        JLabel fractalLabel = new JLabel("Select fractal: ");
        JComboBox fractalComboBox = new JComboBox();
        //добавление фракталов в список
        fractalComboBox.addItem(new Mandelbrot());
        fractalComboBox.addItem(new Tricorn());
        fractalComboBox.addItem(new BurningShip());

        fractalPanel.add(fractalLabel);
        fractalPanel.add(fractalComboBox);

        //размещение элементов в основном окне
        frame.getContentPane().add(fractalPanel, BorderLayout.NORTH);
        frame.getContentPane().add(imageDisplay, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        //добавление обработчиков событий для элементов управления
        reset.addActionListener(new ResetButtonListener());
        save.addActionListener(new SaveButtonListener());
        imageDisplay.addMouseListener(new ImageDisplayListener());
        fractalComboBox.addActionListener(new ComboBoxListener());

        frame.pack ();
        frame.setVisible (true);
        frame.setResizable (false);
    }

    //отрисовка фрактала
    private void drawFractal () {
        for (int x=0; x< displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                //перевод из пиксельный координат в координаты в пространстве фрактала
                double xCoord = FractalGenerator.getCoord(rec.x, rec.x + rec.width,
                        displaySize, x);
                double yCoord = FractalGenerator.getCoord(rec.y, rec.y + rec.height,
                        displaySize, y);

                //количество итерацийдля текущего пикселя
                int numIters = gen.numIterations(xCoord, yCoord);

                //если точка не выходит за границы - окрашивание пикселя в черный цвет
                //Иначе значение цвета пикселя выбирается на основе количества итераций
                if (numIters == -1) {
                    imageDisplay.drawPixel(x, y, 0);
                } else {
                    //цветовое пространство HSV
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    imageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        // обновление отображения
        imageDisplay.repaint();
    }

    // запуск оконного приложения
    public static void main(String args[]) {
        // инициализация фрактала (ссылка на базовый класс для отображения разных видов фракталов)
        FractalExplorer fractalExplorer = new FractalExplorer(500);

        // создание и показ интерфейса
        fractalExplorer.createAndShowGUI();
        // отрисовка фрактала
        fractalExplorer.drawFractal();
    }

    //класс для обработки событий java.awt.event.ActionListener от кнопки reset
    public class ResetButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            //сброс диапазона к начальному, определенному генератором
            gen.getInitialRange(rec);
            //перерисовка фрактала
            drawFractal();
        }
    }

    //класс для обработки событий java.awt.event.ActionListener от кнопки save
    public class SaveButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            //если пользователь не закрыл окно выбора файла - продолжается работа
            if (chooser.showSaveDialog(imageDisplay) == chooser.APPROVE_OPTION)
            {
                File f = chooser.getSelectedFile();
                try {
                    //запись в выбранный файл
                    ImageIO.write(imageDisplay.getImage(), "png", f);
                }
                catch (Exception ex){
                    //Вывод ошибки в случае ее возникновения
                    JOptionPane.showMessageDialog(imageDisplay, ex.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    //класс для обработки событий  java.awt.event.MouseListener с дисплея
    public class ImageDisplayListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            //получение координат клика
            int x = e.getX();
            int y = e.getY();

            //перевод из пиксельный координат в координаты в пространстве фрактала
            double xCoord = FractalGenerator.getCoord (rec.x, rec.x + rec.width,
                    displaySize, x);
            double yCoord = FractalGenerator.getCoord (rec.y, rec.y + rec.height,
                    displaySize, y);

            gen.recenterAndZoomRange(rec, xCoord, yCoord, 0.5);
            //перерисовка фрактала
            drawFractal();
        }
    }

    //класс для обработки событий java.awt.event.ActionListener от выпадающего списка фракталов
    public class ComboBoxListener implements ActionListener {
        //метод извлекает выбранный элемент из выпадающего списка фракталов
        //и установливает его в качестве текущего генератора фракталов и перерисовывает
        public void actionPerformed(ActionEvent arg0) {
            FractalExplorer.this.gen = (FractalGenerator) ((JComboBox) arg0.getSource()).getSelectedItem();
            FractalExplorer.this.drawFractal();
        }
    }
}

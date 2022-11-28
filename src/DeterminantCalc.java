import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class DeterminantCalc
{

    private static int max = 1000;     //максимальне значення N
    private static int decimals = 3;    //кількість знаків після коми

                                 //візуальні компоненти
    private JLabel statusBar;
    private JTextArea A;
    private JLabel res;
    private int n = 1;      //допоміжна змінна для створення матриці
    private static NumberFormat nf; //формат вводу


    public Component createComponents()
    {

        A = new JTextArea();
        res = new JLabel("Dеterminant: ");

        JPanel paneMs = new JPanel();
        paneMs.setLayout(new BoxLayout(paneMs, BoxLayout.X_AXIS));
        paneMs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paneMs.add(MatrixPane("Еnter Matrix", A));
        paneMs.add(Box.createRigidArea(new Dimension(10, 0)));
        paneMs.add(res);



        JPanel paneBtn = new JPanel();
        JButton btnDet = new JButton("Calculate determinant");
        paneBtn.add(btnDet);

        /*
         * Опис події натискання на кнопку
         * "Calculate Determinant"
         * */

        btnDet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                try { res.setText("Determinant A: " + nf.format(Determinant(ReadMatrix(A)))); }
                catch(Exception e) { System.err.println("Error: " + e); }
            }
        });



        /*Роміщення елементів на головній панелі*/
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(paneMs);
        pane.add(paneBtn);

        JPanel fpane = new JPanel();
        fpane.setLayout(new BorderLayout());
        fpane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        fpane.add("Center", pane);
        statusBar = new JLabel("Ready");
        fpane.add("South", statusBar);

        return fpane;
    }

    /*Поле для вводу матриці*/
    private JPanel MatrixPane(String str, JTextArea ta)
    {
        JScrollPane scrollPane= new JScrollPane(ta);
        int size = 200;

        scrollPane.setPreferredSize(new Dimension(size, size));
        JLabel label = new JLabel(str);
        label.setLabelFor(scrollPane);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(5,5, 5, 5));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(scrollPane);

        return pane;
    }
/*
    ReadInMatrix - зчитування матриці з компоненти JTextArea у масив
*/
    public float[][] ReadMatrix(JTextArea text) throws Exception {
       /*перетворення тексту з JTextArea*/
        String rawtext = text.getText();
        String val = "";
        int i = 0;
        int j = 0;
        int[] rsize = new int[max];

        /*
         * Визначення розмірності матриці
         * */
        StringTokenizer ts = new StringTokenizer(rawtext, "\n");
        while (ts.hasMoreTokens()) {
            StringTokenizer ts2 = new StringTokenizer(ts.nextToken());
            while (ts2.hasMoreTokens()) {
                ts2.nextToken();
                j++;
            }
            rsize[i] = j;
            i++;
            j = 0;
        }
        if (i == 0) {
            statusBar.setText("Matrix is empty");
        } else {
            statusBar.setText("Matrix Size: " + i + "*" + i);

            for (int c = 0; c < i; c++) {
                if (rsize[c] != i) {
                    statusBar.setText("Invalid Matrix Entered. Size Mismatch.");
                    throw new Exception("Invalid Matrix Entered. Size Mismatch.");
                }
            }
        /*
        встановлення розмірності робочого масиву
        * */
            n = i;

            float[][] matrix = new float[n][n];
            i = j = 0;
            val = "";

            /*Отримання та перевірка тексту*/

            StringTokenizer st = new StringTokenizer(rawtext, "\n");
            while (st.hasMoreTokens()) {
                StringTokenizer st2 = new StringTokenizer(st.nextToken());
                while (st2.hasMoreTokens()) {
                    val = st2.nextToken();
                    try {
                        matrix[i][j] = Float.parseFloat(val);
                    } catch (Exception exception) {
                        statusBar.setText("Invalid Number Format");
                    }
                    j++;
                }
                i++;
                j = 0;
            }


            return matrix;
        }
        return null;
    }

    /*
     * Методи для обрахунку визначника матриці:
     *   swap() допоміжний метод для перестановки елементів
     *   Determinant() для обрахунку визначника
     * */
    public float[][] swap(float[][] arr, int i1, int j1, int i2,
                          int j2) {
        float temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
        return arr;
    }
    public float Determinant(float[][] matrix)
    {
        float det=1;
        float num1, num2, total = 1;
        int index;

        float[] temp = new float[n + 1];
        // обхід діагональних елементів
        for (int i = 0; i < n; i++) {
            index = i;
            while (index < n && matrix[index][i] == 0) {
                index++;
            }
            if (index == n)
            {
                // детермінант матриці 0
                continue;
            }
            if (index != i) {
                // обмін діагонального та index-ного елементу
                for (int j = 0; j < n; j++) {
                    swap(matrix, index, j, i, j);
                }

                det = (float) (det * Math.pow(-1, index - i));
            }

            // збереження діагональних елементів
            for (int j = 0; j < n; j++) {
                temp[j] = matrix[i][j];
            }

            // обхід кожного рядка під діагоналлю
            for (int j = i + 1; j < n; j++) {
                num1 = temp[i]; //елемент діагоналі
                num2 = matrix[j][i];//наступний елемент рядка


                for (int k = 0; k < n; k++) {
                    matrix[j][k] = (num1 * matrix[j][k])
                            - (num2 * temp[k]);
                }
                total = total * num1; // Det(kA)=kDet(A);
            }
        }

        // множення елементів діагоналі
        for (int i = 0; i < n; i++) {
            det = det * matrix[i][i];
        }
        float result =  (det / total); // Det(kA)/k=Det(A);
        if(result == -0.0) result = 0;
        return result;
    }


    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Matrix Calculator");
        frame.setSize(new Dimension(725,200));
        DeterminantCalc app = new DeterminantCalc();

        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);

        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(decimals);

    }
}

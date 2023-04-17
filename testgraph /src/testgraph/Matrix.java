/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testgraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;



public class Matrix implements ActionListener 
{
     private static int col, row;  //dimentions
     private static double myMatrix [][];
      private static double tempMatrix [][]; 
     private static JTextField inputField [][];
     private static int result;
     private static JButton 
             multiplyB, nMultiplyB, 
              showMatrix, newMatrix;
     private static JPanel choosePanel [] = new JPanel[12];
     private static int lastCol , lastRow ;
     
     Matrix ()
     {
         col = row = 0;
         myMatrix = new double [0][0];
         // create a frame and add an animation component to it.
        
         ChooseOperation();
     }
     
     
     public interface Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber);
    }

    // returns the current time as a double
    public static double now() {
        long t = System.currentTimeMillis();

        return (double)t / 1000.0f;
    }

    public static class AnimationComponent extends Component {
        Animation animation;
        // keeps track of the time and frame
        int frame = 0;
        double startTime = now();
        double lastTime = now();

        // use this timer to run the animation
        java.util.Timer timer = new java.util.Timer();

        AnimationComponent(Animation animation) {
            this.animation = animation;

            // start the timer.
            // Its bets to do this only when the component is shown, but I don't
            // remember how to do this.  For now just start the animation after one second.
            // The 1000 / 30 will be 30 frames per second.
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        public void run() {
                            repaint();
                        }
                    }
                    , 1000, 1000 / 30);
        }

        // override the component paint method to update the time and call the animation
        public void paint(Graphics g) {
            double nowTime = now();

            frame += 1;

            g.clearRect(0, 0, getWidth(), getHeight());
            animation.drawFrame(g, getSize(), nowTime - startTime, nowTime - lastTime, frame);

            lastTime = nowTime;
        }

        // return the size we want the window to be 640x480
        public Dimension getPreferredSize() {
            return new Dimension(400, 200);
        }
    }

    // test animation to draw a sine wave
    public static class SineWave implements Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber) {
            // set the point in the window halfway down for the "zero" point.
            double zero = size.getHeight() / 2.0;

            // 100 amplitude, 2hz
            double y = zero + 100 * Math.sin(2 * frameTime);

            // scale x to make the window 5 seconds wide
            double tx = frameTime / 5 - (int)(frameTime / 5);
            double x = tx * size.getWidth();

         
       
            // draw a line and a circle on the sine wave
           g.drawLine(0, (int)zero, (int)size.getWidth(), (int)zero);
            g.setColor(Color.GREEN);
            
            g.drawOval((int)x, (int)y, 20, 30);
            
            
        }
    }
     //prompting for matrix's dimensions
     private static void getDimension() 
    {
      JTextField lField = new JTextField(5); //lenght field 
      JTextField wField = new JTextField(5); //col field
      
      //design input line
      JPanel choosePanel [] = new JPanel [2];
       choosePanel [0] = new JPanel();
       choosePanel [1] = new JPanel();
      choosePanel[0].add(new JLabel("Enter Dimensitions") );
      choosePanel[1].add(new JLabel("Rows:"));
      choosePanel[1].add(lField);
      choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
      choosePanel[1].add(new JLabel("Cols:"));
      choosePanel[1].add(wField);
        
      result = JOptionPane.showConfirmDialog(null, choosePanel, 
               null,JOptionPane.OK_CANCEL_OPTION, 
               JOptionPane.PLAIN_MESSAGE);
        
      //save last dimensions
      lastCol = col;
      lastRow = row;
      
      //ok option
       if(result == 0)
       {
         
         if(wField.getText().equals(""))
             col = 0;
         else
         {
             if(isInt(wField.getText()))
             {
                 col = Integer.parseInt(wField.getText());
             }
             else
             {
                 JOptionPane.showMessageDialog(null, "Wrong Dimensions");
                 col = lastCol;
                 row = lastRow;
                 return;
             }
            
             if(isInt(lField.getText()))
             {
                 row = Integer.parseInt(lField.getText());
             }
             else
             {
                 JOptionPane.showMessageDialog(null, "Wrong Dimensions");
                 col = lastCol;
                 row = lastRow;
                 return;
             }
          
         }
       if(col < 1 || row < 1)
       {
           JOptionPane.showConfirmDialog(null, "You entered wrong dimensions", 
                   "Error",JOptionPane.PLAIN_MESSAGE);
           col  = lastCol;
           row = lastRow;
          
       }
       else
       {
           tempMatrix = myMatrix;
           myMatrix = new double [row][col];
            if(!setElements(myMatrix, "Fill your new matrix")) //filling the new matrix
            {
                //backup
                
                myMatrix = tempMatrix;
            }
       }
       }
       else if(result == 1)
       {
           col = lastCol;
           row = lastRow;
       }
     }//end get Dimension
    
     //setting a matrix's elementis
    private static boolean setElements(double matrix [][], String title )
    {
        int temp, temp1;             //temprature variable
        String tempString;
        
       JPanel choosePanel [] = new JPanel [row+2];
       choosePanel[0] = new JPanel();
       choosePanel[0].add(new Label(title ));
       choosePanel[choosePanel.length-1] = new JPanel();
       choosePanel[choosePanel.length-1].add(new Label("consider space field as zeros"));
       inputField  = new JTextField [matrix.length][matrix[0].length];
        
       
       //lenght loop
       for(temp = 1; temp <= matrix.length; temp++)
       {
           choosePanel[temp] = new JPanel();
           
           
           for(temp1 = 0; temp1 < matrix[0].length; temp1++)
           {
               inputField [temp-1][temp1] = new JTextField(3);
               choosePanel[temp].add(inputField [temp-1][temp1]);
               
               if(temp1 < matrix[0].length -1)
               {
               choosePanel[temp].add(Box.createHorizontalStrut(15)); // a spacer
               }
               
           }//end col loop
           
       }//end row loop
       
       result = JOptionPane.showConfirmDialog(null, choosePanel, 
               null, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
     
      
      if(result == 0)
      {
          checkTextField(inputField);
       for(temp = 0; temp < matrix.length; temp++)
       {
        for(temp1 = 0; temp1 < matrix[0].length; temp1++)
            {
                tempString = inputField[temp][temp1].getText();
                
                
                 if(isDouble(tempString))
                {
                matrix [temp][temp1] = Double.parseDouble(inputField[temp][temp1].getText());
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "You entered wrong elements");
                    
                    //backup
                    col = lastCol;
                    row = lastRow;
                    
                    return false;
                }                      
            }
       }
       return true;
    }
      else
          return false;
    
      
    }//end get Inputs
    
    //for setting spaced fields as zeros
     private static void checkTextField (JTextField field [][] )
     {
         for(int temp = 0; temp < field.length; temp++)
         {
             for(int temp1 = 0; temp1 < field[0].length; temp1++)
             {
                 if(field[temp][temp1].getText().equals(""))
                 field[temp][temp1].setText("0");
             }
         }
     }//end reset
     
    private void ChooseOperation ()
    {
        JPanel contentPane;
    JLabel imageLabel = new JLabel();
    JLabel headerLabel = new JLabel();
        int temp;
        JFrame frame = new JFrame();
        
        for(temp = 0; temp < choosePanel.length; temp++)
        {
            choosePanel [temp] = new JPanel ();
        }
        
        ImageIcon chooseImage = new ImageIcon(getClass().getResource
                ("choose-button.png")) ;
        JLabel chooseLabel = new JLabel (chooseImage);
        choosePanel[0].add(chooseLabel);
        
        choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
        
        choosePanel[6].add(Box.createHorizontalStrut(15)); // a spacer
        
        

       // frame.add(new AnimationComponent(new SineWave()));

        
        ImageIcon logoImage = new ImageIcon(getClass().getResource("java.png")) ;
        JLabel logoLabel = new JLabel (logoImage);
        choosePanel[7].add(new AnimationComponent(new SineWave()));
        
        
        
        showMatrix = new JButton ("Show Matrix");
        showMatrix.setPreferredSize(new Dimension(175,35));
        showMatrix.addActionListener(this);
        choosePanel[2].add(showMatrix);
        
       
        
        multiplyB = new JButton ("The naive algorithm");
        multiplyB.setPreferredSize(new Dimension(175,35));
        multiplyB.addActionListener(this);
        choosePanel[3].add(multiplyB);
        
        nMultiplyB = new JButton ("Multiplying by Strassen’s Algorithm ");
        nMultiplyB.setPreferredSize(new Dimension(175,35));
        nMultiplyB.addActionListener(this);
        choosePanel[3].add(nMultiplyB);
     
        
        newMatrix = new JButton("New Matrix");
        newMatrix.setPreferredSize(new Dimension(275,35));
        newMatrix.addActionListener(this);
        choosePanel[5].add(newMatrix);
        
        JOptionPane.showConfirmDialog(null, choosePanel, null,
               JOptionPane.CLOSED_OPTION , JOptionPane.PLAIN_MESSAGE);
         
    }
   
   // Method 1
    // Function to multiply matrices
  public double[][] multiply(double[][] A, double[][] B)
    {
        // Order of matrix
        int n = A.length;
  
        // Creating a 2D square matrix with size n
        // n is input from the user
        double [][] R = new double[n][n];
  
        // Base case
        // If there is only single element
        if (n == 1)
  
            // Returnng the simple multiplication of
            // two elements in matrices
            R[0][0] = A[0][0] * B[0][0];
  
        // Matix
        else {
            // Step 1: Dividing Matrix into parts
            // by storing sub-parts to variables
             double[][] A11 = new  double[n / 2][n / 2];
             double[][] A12 = new  double[n / 2][n / 2];
             double[][] A21 = new  double[n / 2][n / 2];
             double[][] A22 = new  double[n / 2][n / 2];
             double[][] B11 = new  double[n / 2][n / 2];
             double[][] B12 = new  double[n / 2][n / 2];
             double[][] B21 = new  double[n / 2][n / 2];
             double[][] B22 = new  double[n / 2][n / 2];
  
            // Step 2: Dividing matrix A into 4 halves
            split(A, A11, 0, 0);
            split(A, A12, 0, n / 2);
            split(A, A21, n / 2, 0);
            split(A, A22, n / 2, n / 2);
  
            // Step 2: Dividing matrix B into 4 halves
            split(B, B11, 0, 0);
            split(B, B12, 0, n / 2);
            split(B, B21, n / 2, 0);
            split(B, B22, n / 2, n / 2);
  
            // Using Formulas as described in algorithm
  
            // M1:=(A1+A3)×(B1+B2)
            double[][] M1= multiply(add(A11, A22), add(B11, B22));
            
            // M2:=(A2+A4)×(B3+B4)
           double[][] M2 = multiply(add(A21, A22), B11);
            
            // M3:=(A1−A4)×(B1+A4)
            double[][] M3 = multiply(A11, sub(B12, B22));
            
            // M4:=A1×(B2−B4)
            double[][] M4 = multiply(A22, sub(B21, B11));
            
            // M5:=(A3+A4)×(B1)
            double[][] M5 = multiply(add(A11, A12), B22);
            
            // M6:=(A1+A2)×(B4)
            double[][] M6
                = multiply(sub(A21, A11), add(B11, B12));
            
            // M7:=A4×(B3−B1)
            double[][] M7
                = multiply(sub(A12, A22), add(B21, B22));
  
            // P:=M2+M3−M6−M7
            double[][] C11 = add(sub(add(M1, M4), M5), M7);
            
            // Q:=M4+M6
            double[][] C12 = add(M3, M5);
            
            // R:=M5+M7
            double[][] C21 = add(M2, M4);
            
            // S:=M1−M3−M4−M5
            double[][] C22 = add(sub(add(M1, M3), M2), M6);
  
            // Step 3: Join 4 halves into one result matrix
            join(C11, R, 0, 0);
            join(C12, R, 0, n / 2);
            join(C21, R, n / 2, 0);
            join(C22, R, n / 2, n / 2);
        }
  
        // Step 4: Return result
        return R;
    }
  
    // Method 2
    // Funtion to substract two matrices
    public double[][] sub(double[][] A, double[][] B)
    {
        //
        int n = A.length;
  
        //
        double[][] C = new double[n][n];
  
        // Iterating over elements of 2D matrix
        // using nested for loops
  
        // Outer loop for rows
        for (int i = 0; i < n; i++)
  
            // Inner loop for columns
            for (int j = 0; j < n; j++)
  
                // Substracting corresponding elements
                // from matrices
                C[i][j] = A[i][j] - B[i][j];
  
        // Returning the resultant matrix
        return C;
    }
  
    // Method 3
    // Funtion to add two matrices
    public double[][] add(double[][] A, double[][] B)
    {
  
        //
        int n = A.length;
  
        // Creating a 2D square matrix
        double[][] C = new double[n][n];
  
        // Iterating over elements of 2D matrix
        // using nested for loops
  
        // Outer loop for rows
        for (int i = 0; i < n; i++)
  
            // Inner loo pfor columns
            for (int j = 0; j < n; j++)
  
                // Adding corresponding elements
                // of matrices
                C[i][j] = A[i][j] + B[i][j];
  
        // Returning the resultant matrix
        return C;
    }
  
    // Method 4
    // Funtion to split parent matrix
    // into child matrices
    public void split(double[][] P, double[][] C, int iB, int jB)
    {
        // Iterating over elements of 2D matrix
        // using nested for loops
  
        // Outer loop for rows
        for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
  
            // Inner loop for columns
            for (int j1 = 0, j2 = jB; j1 < C.length;
                 j1++, j2++)
  
                C[i1][j1] = P[i2][j2];
    }
  
    // Method 5
    // Funtion to join child matrices
    // into (to) parent matrix
    public void join(double[][] C, double[][] P, int iB, int jB)
  
    {
        // Iterating over elements of 2D matrix
        // using nested for loops
  
        // Outer loop for rows
        for (int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
  
            // Inner loop for columns
            for (int j1 = 0, j2 = jB; j1 < C.length;
                 j1++, j2++)
  
                P[i2][j2] = C[i1][j1];
    }
    
    
    // naive algorithm of matrix multiplication 
    
     // code of The naive algorithm of matrix multiplication
    public int [][] naive_algorithm(int [][] matrix1, int [][] matrix2) {
        int length = matrix1.length;
        int [][] result = new int[length][length];
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                for(int k = 0; k < length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }
    
    @Override
    public  void actionPerformed(ActionEvent e) 
    {
        
        if(e.getSource() == showMatrix)
        {
            showMatrix( myMatrix, "Your Matrix");
        }
      
         
        else    if(e.getSource() == multiplyB)
        {
            multiplyByMatrix();
        }
        
        
        else    if(e.getSource() ==  nMultiplyB)
        {
                guiMultliply();
        }
        
        else   if(e.getSource() == newMatrix)
        {
            newMatrix();
        }
    }//end action performed

    
    private static void showMatrix(double [][] matrix, String title )
    {
        int temp, temp1;             //temprature variable
        
       JPanel choosePanel [] = new JPanel [matrix.length+1];
       choosePanel[0] = new JPanel ();
       choosePanel[0].add( new JLabel (title) );
   
       for(temp = 1; temp <= matrix.length; temp++)
       {
           choosePanel[temp] = new JPanel();
           
           
           for(temp1 = 0; temp1 < matrix[0].length; temp1++)
           {
               if(matrix[temp-1][temp1] == -0)
               {
                  matrix[temp-1][temp1] = 0; 
               }
               choosePanel[temp].add(new JLabel(String.format("%.2f", matrix[temp-1][temp1])));
               
               if(temp1 < matrix[0].length -1)
               {
               choosePanel[temp].add(Box.createHorizontalStrut(15)); // a spacer
               }
               
           }//end col loop
           
       }//end row loop
       
    if(col == 0 || row == 0)
    {
        JOptionPane.showMessageDialog(null, "You haven't entered any matrix");
    }
    else
    {
    
    JOptionPane.showMessageDialog(null, choosePanel, null, 
            JOptionPane.PLAIN_MESSAGE, null);
    }  
    }//end show Matrix

private static void multiplyByMatrix ()
{
    
      JTextField wField = new JTextField(5); //col field
      JTextField lField = new JTextField(5); //row field 
      int col2 = 0;
      int row2 =0;
      double m2 [][] , results[][];
      int sum;
      
      if(myMatrix.length < 1)
        {
            JOptionPane.showMessageDialog(null, "You haven't entered any matrix");
        }
        else
        {
      
      //design input line
      JPanel choosePanel [] = new JPanel [2];
       choosePanel [0] = new JPanel();
       choosePanel [1] = new JPanel();
   
       choosePanel[0].add(new JLabel("Enter Dimensitions") );
      choosePanel[1].add(new JLabel("Rows:"));
      choosePanel[1].add(lField);
      choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
      choosePanel[1].add(new JLabel("Cols:"));
      choosePanel[1].add(wField);
      
      result = JOptionPane.showConfirmDialog(null, choosePanel, 
               null,JOptionPane.PLAIN_MESSAGE, 
               JOptionPane.PLAIN_MESSAGE);
      
      if(result == 0)
      {
          if(wField.getText().equals("") &&lField.getText().equals("") )
          {
              col2 = 0;
              row2 =0;
          }
      else
          {
              if(isInt(wField.getText())&& isInt(lField.getText()) )
              {
              col2 = Integer.parseInt(wField.getText());  
               row2 = Integer.parseInt(lField.getText());
              }
          }
          
     
           m2 = new double [row2][col2];
          results = new double [row][col2];
      if(setElements(m2, "Fill multiplying matrix"))
      {
      
      for ( int i = 0 ; i < row ; i++ )
         {
            for ( int j = 0 ; j < col2 ; j++ )
            {   
                sum = 0;
               for ( int k = 0 ; k < col ; k++ )
               {
                  sum +=  myMatrix[i][k]*m2[k][j];
               }
 
               results[i][j] = sum;
             
            }
         }
 
     showMatrix(results, "Mulitiplication Result");
      }//elements checking
      }//dimensions checking
      else
          return;
        }//end else of checking
}//end multiply by matrix method

    void guiMultliply ()
{
    
     JTextField wField = new JTextField(5); //col field
      JTextField lField = new JTextField(5); //row field 
      int col2 = 0;
      int row2 =0;
      double m2 [][] , results[][];
      int sum;
      
      if(myMatrix.length < 1)
        {
            JOptionPane.showMessageDialog(null, "You haven't entered any matrix");
        }
        else
        {
      
      //design input line
      JPanel choosePanel [] = new JPanel [2];
       choosePanel [0] = new JPanel();
       choosePanel [1] = new JPanel();
   
       choosePanel[0].add(new JLabel("Enter Dimensitions") );
      choosePanel[1].add(new JLabel("Rows:"));
      choosePanel[1].add(lField);
      choosePanel[1].add(Box.createHorizontalStrut(15)); // a spacer
      choosePanel[1].add(new JLabel("Cols:"));
      choosePanel[1].add(wField);
      
      result = JOptionPane.showConfirmDialog(null, choosePanel, 
               null,JOptionPane.PLAIN_MESSAGE, 
               JOptionPane.PLAIN_MESSAGE);
      
      if(result == 0)
      {
          if(wField.getText().equals("") &&lField.getText().equals("") )
          {
              col2 = 0;
              row2 =0;
          }
      else
          {
              if(isInt(wField.getText())&& isInt(lField.getText()) )
              {
              col2 = Integer.parseInt(wField.getText());  
               row2 = Integer.parseInt(lField.getText());
              }
          }
          
     
           m2 = new double [row2][col2];
          results = new double [row][col2];
      if(setElements(m2, "Fill multiplying matrix"))
      {
      
  results = multiply(myMatrix,m2 );
     showMatrix(results, "Mulitiplication Result");
      }//elements checking
      }//dimensions checking
      else
          return;
        }//end else of checking
}//end multiply by matrix method


    private static void swap (double [] res1, double [] res2)
    {
        int temp;
        double tempDouble;
        
        for(temp = 0; temp < res1.length;temp++)
        {
            tempDouble = res1[temp];
            res1[temp] = res2[temp];
            res2[temp] = tempDouble;
        }
        
    }
       
     
   private static boolean isInt (String str)
   {
       int temp;
       if (str.length() == '0')
           return false;
       
       for(temp = 0; temp < str.length();temp++)
       {
           if(str.charAt(temp) != '+' && str.charAt(temp) != '-'
                   && !Character.isDigit(str.charAt(temp)))
           {
               return false;
           }
       }
       return true;
   }
   
    private static boolean isDouble (String str)
   {
       int temp;
       if (str.length() == '0')
           return false;
       
       for(temp = 0; temp < str.length();temp++)
       {
           if(str.charAt(temp) != '+' && str.charAt(temp) != '-'
                   && str.charAt(temp) != '.'
                   && !Character.isDigit(str.charAt(temp))
                   )
           {
               return false;
           }
       }
       return true;
   }
   
    private static void newMatrix ()
    {        
        getDimension();
    }
     public static void main (String [] args)
    {
        Matrix m1 = new Matrix ();
        
    }
}//end class


package ticTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class TicTacToe extends JPanel
{

    int turnCount = 0;
    JButton[] buttons = new JButton[9];


    private StringBuilder gameLog = new StringBuilder();    // String records each round of the game

    public TicTacToe()
    {
        setLayout(new GridLayout(3, 3));
        gameLog.append(new Date().toString() + "\n");

        for (int i = 0; i <= 8; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setText("");
            buttons[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {

                    JButton btnClicked = (JButton) e.getSource();


                    if (turnCount % 2 == 0)         // Logs the move
                    {
                        btnClicked.setText("X");
                        gameLog.append("X made a move:\n");
                    } else
                    {
                        btnClicked.setText("O");
                        gameLog.append("O made a move:\n");
                    }


                    for (int i = 0; i < buttons.length; i++)       //Gets a string representation of the board for logging
                    {
                        if (i != 0 && i % 3 == 0)
                        {
                            gameLog.append("\n");
                        }

                        if (buttons[i].getText().isEmpty())
                        {
                            gameLog.append("-");
                        } else
                        {
                            gameLog.append(buttons[i].getText());
                        }
                    }

                    gameLog.append("\n");

                    if (rowCheck() == true)
                    {

                        if (turnCount % 2 == 0)     //Logs the winner
                        {
                            gameLog.append("X wins. Game Over.");
                        } else
                        {
                            gameLog.append("Y wins. Game Over.");
                        }


                        try     // Log to file
                        {
                            PrintWriter outFile = new PrintWriter(new FileWriter("log.dat", true));
                            outFile.println(gameLog.toString() + "\n");
                            outFile.close();
                        } catch (Exception ex)
                        {
                            ex.printStackTrace(System.out);
                        }

                        JOptionPane.showMessageDialog(null, "Game Over.");
                        resetBoard();
                    } else if (isBoardFull())
                    {

                        gameLog.append("It's a draw. Game Over."); //If board is full, game is draw


                        try     //Log to file
                        {
                            PrintWriter outFile = new PrintWriter(new FileWriter("log.dat", true));
                            outFile.println(gameLog.toString() + "\n");
                            outFile.close();
                        } catch (Exception ex)
                        {
                            ex.printStackTrace(System.out);
                        }

                        JOptionPane.showMessageDialog(null, "Game Over.");
                        resetBoard();
                    }

                    turnCount++;
                }
            });
            add(buttons[i]);
        }
    }

    public void resetBoard()
    {
        gameLog = new StringBuilder();                              // When board has been reset, it basically means a new game
        gameLog.append(new Date().toString() + "\n");               // Each game is logged with time stamp

        for (int i = 0; i <= 8; i++)
        {
            buttons[i].setText("");
        }
    }

    public boolean rowCheck()           //checks rows for matches
    {
        if (equals(0, 1) && equals(1, 2))
        {
            return true;
        }
        else if (equals(3, 4) && equals(4, 5))
        {
            return true;
        }
        else if (equals(6, 7) && equals(7, 8))
        {
            return true;
        }
        else if (equals(0, 3) && equals(3, 6))
        {
            return true;
        }
        else if (equals(1, 4) && equals(4, 7))
        {
            return true;
        }
        else if (equals(2, 5) && equals(5, 8))
        {
            return true;
        }
        else if (equals(0, 4) && equals(4, 8))
        {
            return true;
        }
        else if (equals(2, 4) && equals(4, 6))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean equals(int i, int j)
    {
        if (buttons[i].getText().equals(buttons[j].getText()) && !buttons[i].getText().equals(""))
        {
            return true;
        } else {
            return false;
        }
    }


    public boolean isBoardFull()            //Checks if board is full
    {
        for (JButton button : buttons)
        {
            if (button.getText().isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args)
    {
        final JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.CENTER, new TicTacToe());

        JButton logsButton = new JButton("Game Logs");              //Adds game logs button
        frame.getContentPane().add(BorderLayout.SOUTH, logsButton);

        logsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTextArea logsField = new JTextArea();              //Displays dialog for game logs
                logsField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                JDialog dialog = new JDialog(frame, true);
                dialog.setTitle("Game Logs");
                dialog.setLayout(new BorderLayout());
                dialog.setSize(500, 500);
                dialog.setLocationRelativeTo(frame);

                dialog.add(BorderLayout.CENTER, new JScrollPane(logsField));

                try
                {
                    Scanner inFile = new Scanner(new File("log.dat"));

                    while (inFile.hasNextLine())
                    {
                        logsField.append(inFile.nextLine() + "\n");
                    }

                    inFile.close();
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(dialog, "There are no available game logs.");
                    return;
                }

                dialog.setVisible(true);
            }
        });

        frame.setBounds(0, 0, 400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab7_hash;

import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Nathan
 */

public class MainGUI extends JFrame {

    private PSNUsers system;

    public MainGUI() throws Exception {
        system = new PSNUsers();

        setTitle("PSN Manager");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addUser = new JButton("Agregar Usuario");
        JButton delUser = new JButton("Desactivar Usuario");
        JButton addTrophy = new JButton("Agregar Trofeo");
        JButton playerInfo = new JButton("Mostrar Info");
        JButton salir = new JButton("Salir");

        Dimension btnSize = new Dimension(180, 35);
        int esp = 8;

        addUser.setMaximumSize(btnSize);
        delUser.setMaximumSize(btnSize);
        addTrophy.setMaximumSize(btnSize);
        playerInfo.setMaximumSize(btnSize);
        salir.setMaximumSize(btnSize);

        addUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        delUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTrophy.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(addUser);
        panel.add(Box.createVerticalStrut(esp));

        panel.add(delUser);
        panel.add(Box.createVerticalStrut(esp));

        panel.add(addTrophy);
        panel.add(Box.createVerticalStrut(esp));

        panel.add(playerInfo);
        panel.add(Box.createVerticalStrut(esp));

        panel.add(salir);

        add(panel);

        addUser.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Ingrese username:");
            try {
                system.addUser(user);
            } catch (Exception ex) {
            }
        });

        delUser.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuario a desactivar:");
            try {
                system.deactivateUser(user);
            } catch (Exception ex) {
            }
        });

        addTrophy.addActionListener(e -> {
            try {
                String user = JOptionPane.showInputDialog("Usuario:");
                String game = JOptionPane.showInputDialog("Juego:");
                String desc = JOptionPane.showInputDialog("Descripción del trofeo:");

                String[] opciones = {"PLATINO", "ORO", "PLATA", "BRONCE"};
                String tipo = (String) JOptionPane.showInputDialog(
                        null, "Seleccione el tipo de trofeo:",
                        "Tipo", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
                );

                JOptionPane.showMessageDialog(null,
                        "Seleccione una imagen para el trofeo (PNG/JPG)");

                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);

                if (result != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "No seleccionó imagen.");
                    return;
                }

                File file = fc.getSelectedFile();
                byte[] img = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(img);
                fis.close();

                system.addTrophieTo(user, game, desc, Trophy.valueOf(tipo), img);

                JOptionPane.showMessageDialog(null, "Trofeo agregado con éxito.");

            } catch (Exception ex) {
            }
        });

        playerInfo.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuario:");
            try {
                mostrarInfo(user);
            } catch (Exception ex) {
            }
        });

        salir.addActionListener(e -> dispose());
    }

    private void mostrarInfo(String username) throws Exception {
        String texto = system.playerInfo(username);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea(texto);
        textArea.setEditable(false);
        panel.add(textArea);

        RandomAccessFile trophiesFile = new RandomAccessFile("trophies.psn", "r");
        trophiesFile.seek(0);

        while (trophiesFile.getFilePointer() < trophiesFile.length()) {
            String u = trophiesFile.readUTF();
            String tipo = trophiesFile.readUTF();
            String game = trophiesFile.readUTF();
            String desc = trophiesFile.readUTF();
            String fecha = trophiesFile.readUTF();

            int imglen = trophiesFile.readInt();
            byte[] img = new byte[imglen];
            trophiesFile.read(img);

            if (u.equals(username)) {
                JLabel lbl = new JLabel(
                        fecha + " - " + tipo + " - " + game + " - " + desc
                );
                panel.add(lbl);

                ImageIcon icon = new ImageIcon(img);
                Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                panel.add(imgLabel);

                panel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(450, 450));

        JOptionPane.showMessageDialog(null, scroll, "Información del jugador",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws Exception {
        new MainGUI().setVisible(true);
    }
}


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
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addUser = new JButton("Agregar Usuario");
        JButton delUser = new JButton("Desactivar Usuario");
        JButton addTrophy = new JButton("Agregar Trofeo");
        JButton playerInfo = new JButton("Mostrar Info");
        JButton salir = new JButton("Salir");

        Dimension btnSize = new Dimension(200, 40);
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
        panel.add(Box.createVerticalStrut(15));
        panel.add(delUser);
        panel.add(Box.createVerticalStrut(15));
        panel.add(addTrophy);
        panel.add(Box.createVerticalStrut(15));
        panel.add(playerInfo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(salir);

        add(panel);

        addUser.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Ingrese username:");
            if (user == null || user.isEmpty()) {
                return;
            }
            try {
                if (system.userExists(user)) {
                    mostrarMensaje("El usuario ya existe.", false);
                } else {
                    system.addUser(user);
                    mostrarMensaje("Usuario agregado con éxito.", true);
                }
            } catch (Exception ex) {
                mostrarMensaje("Error al agregar usuario.", false);
            }
        });

        delUser.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuario a desactivar:");
            if (user == null || user.isEmpty()) {
                return;
            }
            try {
                if (!system.userExists(user)) {
                    mostrarMensaje("Usuario no encontrado.", false);
                } else {
                    system.deactivateUser(user);
                    mostrarMensaje("Usuario desactivado con éxito.", true);
                }
            } catch (Exception ex) {
                mostrarMensaje("Error al desactivar usuario.", false);
            }

        });

        addTrophy.addActionListener(e -> {
            try {
                String user = JOptionPane.showInputDialog("Usuario:");
                
                if (user == null || user.trim().isEmpty()) {
                    return;
                }
                
                int estado = system.precheckUser(user);
                if (estado == -2) {
                    mostrarMensaje("Error leyendo users.psn. Intenta de nuevo.", false);
                    return;
                }
                if (estado == -1) {
                    JOptionPane.showMessageDialog(this, "El usuario no existe");
                    return;
                }
                if (estado == 0) {
                    JOptionPane.showMessageDialog(this, "La cuenta esta inactiva. no se pueden añadir trofeos");
                    return;
                }
                
                String game = JOptionPane.showInputDialog("Juego:");
                String desc = JOptionPane.showInputDialog("Descripción del trofeo:");
                if (game == null || desc == null) {
                    return;
                }

                String[] opciones = {"PLATINO", "ORO", "PLATA", "BRONCE"};
                String tipo = (String) JOptionPane.showInputDialog(
                        null, "Seleccione el tipo de trofeo:",
                        "Tipo", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
                );
                if (tipo == null) {
                    return;
                }

                JOptionPane.showMessageDialog(null,
                        "Seleccione una imagen para el trofeo (PNG/JPG)");

                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);
                if (result != JFileChooser.APPROVE_OPTION) {
                    mostrarMensaje("No se seleccionó imagen.", false);
                    return;
                }

                File file = fc.getSelectedFile();
                byte[] img = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(img);
                fis.close();

                system.addTrophieTo(user, game, desc, Trophy.valueOf(tipo), img);
                mostrarMensaje("Trofeo agregado con éxito.", true);

            } catch (Exception ex) {
                mostrarMensaje("Error al agregar trofeo.", false);
            }
        });

        playerInfo.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuario:");
            if (user == null || user.isEmpty()) {
                return;
            }
            try {
                mostrarInfo(user);
            } catch (Exception ex) {
                mostrarMensaje("Error al mostrar información.", false);
            }
        });

        salir.addActionListener(e -> dispose());
    }

    private void mostrarMensaje(String mensaje, boolean exito) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(mensaje));
        if (exito) {
            panel.setBackground(new Color(198, 239, 206));
        } else {
            panel.setBackground(new Color(255, 199, 206));
        }
        JOptionPane.showMessageDialog(null, panel, "Resultado", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void mostrarInfo(String username) throws Exception {
        String texto = system.playerInfo(username);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] lineas = texto.split("\n");
        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].startsWith("Activo:")) {
                lineas[i] = "Estado:" + lineas[i].substring(6);
            }
            JLabel lbl = new JLabel(lineas[i]);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lbl);
        }


        RandomAccessFile trophiesFile = new RandomAccessFile("trophies.psn", "r");
        trophiesFile.seek(0);
        int trophyCount = 0;
        while (trophiesFile.getFilePointer() < trophiesFile.length()) {
            String u = trophiesFile.readUTF();
            trophiesFile.readUTF(); 
            trophiesFile.readUTF(); 
            trophiesFile.readUTF(); 
            trophiesFile.readUTF(); 
            int imglen = trophiesFile.readInt();
            trophiesFile.skipBytes(imglen);
            if (u.equals(username)) {
                trophyCount++;
            }
        }

        JLabel cantidadTrofeos = new JLabel("Cantidad de trofeos: " + trophyCount);
        cantidadTrofeos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cantidadTrofeos);

        panel.add(Box.createVerticalStrut(10));
        JLabel separador = new JLabel("TROFEOS:");
        separador.setAlignmentX(Component.LEFT_ALIGNMENT);
        separador.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(separador);
        panel.add(new JSeparator(SwingConstants.HORIZONTAL));
        panel.add(Box.createVerticalStrut(5));

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
                JLabel infoTrofeo = new JLabel(fecha + " - " + tipo + " - " + game + " - " + desc);
                infoTrofeo.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(Box.createVerticalStrut(5));
                panel.add(infoTrofeo);

                ImageIcon icon = new ImageIcon(img);
                Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(imgLabel);

                panel.add(Box.createVerticalStrut(5));
                panel.add(new JSeparator(SwingConstants.HORIZONTAL));
                panel.add(Box.createVerticalStrut(10));
            }
        }
        trophiesFile.close();

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(450, 430));
        JOptionPane.showMessageDialog(null, scroll, "Información del jugador", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws Exception {
        new MainGUI().setVisible(true);
    }
}

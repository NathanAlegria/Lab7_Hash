/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_hash;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author andre
 */
<<<<<<< HEAD
=======
import java.io.*;
import java.util.Date;
import javax.swing.JOptionPane;
>>>>>>> 723a4039da90e30b0eef281e1953cd5fd6ffa2d6

public class PSNUsers {

    private RandomAccessFile usersFile;
    private RandomAccessFile trophiesFile;
    private HashTable users;

    public PSNUsers() throws Exception {
        usersFile = new RandomAccessFile("users.psn", "rw");
        trophiesFile = new RandomAccessFile("trophies.psn", "rw");
        users = new HashTable();
        reloadHashTable();
    }
<<<<<<< HEAD
=======

    private void reloadHashTable() throws IOException {
        if (usersFile.length() == 0) {
            return;
        }

        usersFile.seek(0);
>>>>>>> 723a4039da90e30b0eef281e1953cd5fd6ffa2d6

    private void reloadHashTable() {
        try {
            usersFile.seek(0);
            while (usersFile.getFilePointer() < usersFile.length()) {
                long pos = usersFile.getFilePointer();
                String username = usersFile.readUTF();
                usersFile.readInt();
                usersFile.readInt();
                boolean activo = usersFile.readBoolean();
                if (activo) {
                    users.add(username, pos);
                }
            }
        } catch (Exception e) {
        }
    }

<<<<<<< HEAD
    public void addUser(String username) throws Exception {
        if (users.search(username) != -1) {
            return;
        }
        usersFile.seek(usersFile.length());
        long pos = usersFile.getFilePointer();
=======
  
    public void addUser(String username) throws IOException {
        long pos = users.search(username);

        if (pos != 0) {
            return;
        }

        usersFile.seek(usersFile.length());
        long posescribir = usersFile.getFilePointer();

>>>>>>> 723a4039da90e30b0eef281e1953cd5fd6ffa2d6
        usersFile.writeUTF(username);
        usersFile.writeInt(0);
        usersFile.writeInt(0);
        usersFile.writeBoolean(true);
<<<<<<< HEAD
        users.add(username, pos);
    }

    public void deactivateUser(String username) throws Exception {
        long pos = users.search(username);
        if (pos == -1) {
            return;
        }
        usersFile.seek(pos);
        usersFile.readUTF();
        usersFile.readInt();
        usersFile.readInt();
        usersFile.writeBoolean(false);
        users.remove(username);
    }

    public void addTrophieTo(String username, String game, String trophyName, Trophy type, byte[] img) throws Exception {
        long pos = -1;
        usersFile.seek(0);
        while (usersFile.getFilePointer() < usersFile.length()) {
            long currentPos = usersFile.getFilePointer();
            String u = usersFile.readUTF();
            if (u.equals(username)) {
                pos = currentPos;
                break;
            }
            usersFile.readInt();
            usersFile.readInt();
            usersFile.readBoolean();
        }
        if (pos == -1) {
            return;
        }

        trophiesFile.seek(trophiesFile.length());
        trophiesFile.writeUTF(username);
        trophiesFile.writeUTF(type.name());
        trophiesFile.writeUTF(game);
        trophiesFile.writeUTF(trophyName);
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        trophiesFile.writeUTF(fecha);
        trophiesFile.writeInt(img.length);
        trophiesFile.write(img);

        usersFile.seek(pos);
        usersFile.readUTF();
        long pointsPos = usersFile.getFilePointer();
        int currentPoints = usersFile.readInt();
        int currentTrophies = usersFile.readInt();
        boolean active = usersFile.readBoolean();

        usersFile.seek(pointsPos);
        usersFile.writeInt(currentPoints + type.points);
        usersFile.writeInt(currentTrophies + 1);
    }

    public String playerInfo(String username) throws Exception {
        long pos = -1;
        usersFile.seek(0);
        while (usersFile.getFilePointer() < usersFile.length()) {
            long currentPos = usersFile.getFilePointer();
            String u = usersFile.readUTF();
            if (u.equals(username)) {
                pos = currentPos;
                break;
            }
            usersFile.readInt();
            usersFile.readInt();
            usersFile.readBoolean();
        }
        if (pos == -1) {
            return "No existe";
        }

        usersFile.seek(pos);
        String usuario = usersFile.readUTF();
        int points = usersFile.readInt();
        int trophiesCount = usersFile.readInt();
        boolean active = usersFile.readBoolean();

        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(usuario).append("\n");
        sb.append("Puntos: ").append(points).append("\n");
        sb.append("Trofeos: ").append(trophiesCount).append("\n");
        sb.append("Activo: ").append(active).append("\n\n");

        trophiesFile.seek(0);
        sb.append("TROFEOS:\n");
        while (trophiesFile.getFilePointer() < trophiesFile.length()) {
            String u = trophiesFile.readUTF();
            String tipo = trophiesFile.readUTF();
            String game = trophiesFile.readUTF();
            String desc = trophiesFile.readUTF();
            String fecha = trophiesFile.readUTF();
            int imglen = trophiesFile.readInt();
            trophiesFile.skipBytes(imglen);
            if (u.equals(username)) {
                sb.append(fecha).append(" - ").append(tipo)
                        .append(" - ").append(game)
                        .append(" - ").append(desc).append("\n");
            }
        }
        return sb.toString();
=======

        users.add(username, posescribir);
    }

    public void deactivateUser(String username) throws IOException {
        long pos = users.search(username);

        if (pos == -1) {
            return;
        }

        usersFile.seek(pos);

        usersFile.readUTF();
        usersFile.readInt();
        usersFile.readInt();

        long posactivo = usersFile.getFilePointer();

        usersFile.writeBoolean(false);

        users.remove(username);
    }

    public void addTrophieTo(String username, String trophyGame, String trophyName,
            Trophy type, byte[] trophyImageBytes) throws IOException {

        if (username == null || trophyGame == null || trophyName == null
                || trophyImageBytes == null) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            return;
        }

        long position = users.search(username);

        if (position == -1) {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            return;
        }

        usersFile.seek(position);
        String user = usersFile.readUTF();
        int currentPoints = usersFile.readInt();
        int currentTrophyCount = usersFile.readInt();
        boolean active = usersFile.readBoolean();

        int newPoints = currentPoints + type.points;
        int newTrophyCount = currentTrophyCount + 1;

        usersFile.seek(position);
        usersFile.writeUTF(user);
        usersFile.writeInt(newPoints);
        usersFile.writeInt(newTrophyCount);
        usersFile.writeBoolean(active);

        RandomAccessFile trophiesFile = new RandomAccessFile(TROPHIES_FILE, "rw");
        trophiesFile.seek(trophiesFile.length());

        trophiesFile.writeUTF(username);
        trophiesFile.writeUTF(type.name());
        trophiesFile.writeUTF(trophyGame);
        trophiesFile.writeUTF(trophyName);
        trophiesFile.writeUTF(new Date().toString());
        trophiesFile.writeInt(trophyImageBytes.length);
        trophiesFile.write(trophyImageBytes);

        trophiesFile.close();

        JOptionPane.showMessageDialog(null,
                "Trofeo agregado. Puntos ganados: " + type.points);
    }

    
    public String playerInfo(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            return "El username no puede estar vacío";
        }

        long position = users.search(username);

        if (position == -1) {
            return "Usuario no encontrado o desactivado";
        }

        usersFile.seek(position);
        String user = usersFile.readUTF();
        int points = usersFile.readInt();
        int trophyCount = usersFile.readInt();
        boolean active = usersFile.readBoolean();

        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL JUGADOR ===\n");
        info.append("Username: ").append(user).append("\n");
        info.append("Puntos totales: ").append(points).append("\n");
        info.append("Cantidad de trofeos: ").append(trophyCount).append("\n");
        info.append("Estado: ").append(active ? "Activo" : "Inactivo").append("\n");
        info.append("\n=== TROFEOS ===\n\n");

        return info.toString();
    }

    public void close() throws IOException {
        if (usersFile != null) {
            usersFile.close();
        }
>>>>>>> 723a4039da90e30b0eef281e1953cd5fd6ffa2d6
    }
}

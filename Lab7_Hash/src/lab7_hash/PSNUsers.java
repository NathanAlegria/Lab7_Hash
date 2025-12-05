/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_hash;

/**
 *
 * @author andre
 */
import java.io.*;
import java.util.Date;
import javax.swing.JOptionPane;

public class PSNUsers {

    private RandomAccessFile usersFile;
    private HashTable users;
    private static final String USERS_FILE = "users.psn";
    private static final String TROPHIES_FILE = "trophies.psn";

    public PSNUsers() throws IOException {
        users = new HashTable();

        File userFile = new File(USERS_FILE);
        File trophyFile = new File(TROPHIES_FILE);

        if (!userFile.exists()) {
            userFile.createNewFile();
        }
        if (!trophyFile.exists()) {
            trophyFile.createNewFile();
        }

        usersFile = new RandomAccessFile(USERS_FILE, "rw");
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        if (usersFile.length() == 0) {
            return;
        }

        usersFile.seek(0);

        try {
            while (usersFile.getFilePointer() < usersFile.length()) {
                long position = usersFile.getFilePointer();
                String username = usersFile.readUTF();
                int points = usersFile.readInt();
                int trophyCount = usersFile.readInt();
                boolean active = usersFile.readBoolean();

                if (active) {
                    users.add(username, position);
                }
            }
        } catch (EOFException e) {
        }
    }

  
    public void addUser(String username) throws IOException {
        long pos = users.search(username);

        if (pos != 0) {
            return;
        }

        usersFile.seek(usersFile.length());
        long posescribir = usersFile.getFilePointer();

        usersFile.writeUTF(username);
        usersFile.writeInt(0);
        usersFile.writeInt(0);
        usersFile.writeBoolean(true);

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
    }
}

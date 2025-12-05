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

    public void addUser(String username) throws Exception {
        if (users.search(username) != -1) {
            return;
        }
        usersFile.seek(usersFile.length());
        long pos = usersFile.getFilePointer();
        usersFile.writeUTF(username);
        usersFile.writeInt(0);
        usersFile.writeInt(0);
        usersFile.writeBoolean(true);
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
        long pos = users.search(username);
        
        if (pos == -1) {
            return;
        }
        
        usersFile.seek(pos);
        usersFile.readUTF();
        
        long pointsPos = usersFile.getFilePointer();
        int currentPoints = usersFile.readInt();
        int currentTrophies = usersFile.readInt();
        boolean active = usersFile.readBoolean();
        
        if (!active) {
            return;
        }
        
        if (ExisteTrofeo(username, game, trophyName, type)) {
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
        
        usersFile.seek(pointsPos);
        usersFile.writeInt(currentPoints + type.points);
        usersFile.writeInt(currentTrophies + 1);
        
//        while (usersFile.getFilePointer() < usersFile.length()) {
//            long currentPos = usersFile.getFilePointer();
//            String u = usersFile.readUTF();
//            if (u.equals(username)) {
//                pos = currentPos;
//                break;
//            }
//            usersFile.readInt();
//            usersFile.readInt();
//            usersFile.readBoolean();
//        }
//        if (pos == -1) {
//            return;
//        }
//

//
//        usersFile.seek(pos);
//        usersFile.readUTF();

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
//        sb.append("Trofeos: ").append(trophiesCount).append("\n");
        sb.append("Activo: ").append(active ? "ACTIVA" : "INACTIVA").append("\n\n");

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
    }

    public boolean userExists(String username) throws IOException {
        return users.search(username) != -1;
    }

    private static String normalize(String s) {
        if (s == null) {
            return "";
        }
        
        s = s.trim().toLowerCase().replaceAll("\\s+", " ");
        s = s.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
        
        return s;
    }
    
    private boolean ExisteTrofeo(String username, String game,String nombretrofeo, Trophy tipo) throws IOException {
        String objetivo = normalize(tipo.name()) + "|" + normalize(game) + "|" + normalize(nombretrofeo);
        trophiesFile.seek(0);
        
        while(trophiesFile.getFilePointer() < trophiesFile.length()) {
            String u = trophiesFile.readUTF();
            String t = normalize(trophiesFile.readUTF());
            String g = normalize(trophiesFile.readUTF());
            String d = normalize(trophiesFile.readUTF());
            trophiesFile.readUTF();
            
            int imglen = trophiesFile.readInt();
            
            trophiesFile.skipBytes(imglen);
            
            if (u.equals(username)) {
                String llave = t + "|" + g + "|" + d;
                
                if (llave.equals(objetivo)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}

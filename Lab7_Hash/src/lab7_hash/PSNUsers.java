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
            return;         }

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
    
    /*
        Formato:
        username
        anumuldor de puntos por trofeos
        contador de trofeos

    */
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
        
        //Leer las cosas para poder marcar el usuario como no activo
        usersFile.seek(pos);
        
        usersFile.readUTF();
        usersFile.readInt();
        usersFile.readInt();
        
        long posactivo = usersFile.getFilePointer();
        
        usersFile.writeBoolean(false);
        
        users.remove(username);
    }
}

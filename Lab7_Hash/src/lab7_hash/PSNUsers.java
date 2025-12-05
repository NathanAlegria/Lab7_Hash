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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_hash;

/**
 *
 * @author Hp
 */

public class HashTable {
    private Entry head;

    public HashTable() {
        head = null;
    }

    public void add(String username, long pos) {
        Entry nuevo = new Entry(username, pos);
        if (head == null) {
            head = nuevo;
        } else {
            Entry temp = head;
            while (temp.sigte != null) temp = temp.sigte;
            temp.sigte = nuevo;
        }
    }

    public void remove(String username) {
        if (head == null) return;

        if (head.username.equals(username)) {
            head = head.sigte;
            return;
        }

        Entry temp = head;
        while (temp.sigte != null) {
            if (temp.sigte.username.equals(username)) {
                temp.sigte = temp.sigte.sigte;
                return;
            }
            temp = temp.sigte;
        }
    }

    public long search(String username) {
        Entry temp = head;
        while (temp != null) {
            if (temp.username.equals(username)) {
                return temp.pos;
            }
            temp = temp.sigte;
        }
        return -1;
    }
}

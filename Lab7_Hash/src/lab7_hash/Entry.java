/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_hash;

/**
 *
 * @author Nathan
 */
public class Entry {
    public String username;
    public long pos;
    public Entry sigte;

    public Entry(String username, long pos) {
        this.username = username;
        this.pos = pos;
        this.sigte = null;
    } 

    public String getUsername() {
        return username;
    }

    public long getPos() {
        return pos;
    }

    public Entry getSigte() {
        return sigte;
    }
    
    
}

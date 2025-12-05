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
    
    private Entry cabeza;
    private Entry cola;
    private int size;
    
    /*
        Agrega un nuvo elemento al final de la lista
    */
    public void add(String username, long pos) {
        Entry temp = new Entry(username, pos);
        
        if (cabeza == null) {
            cabeza = cola = temp;
        } else {
            cola.sigte = temp;
            cola = temp;
        }
        
        size++;
    }
    
    /*
        Elimina el primer elemento el cual coincida con el username
    */
    public boolean remove(String username) {
        if (cabeza == null) {
            return false;
        }
        
        //Si el primero es el que se busca
        if (cabeza.getUsername().equals(username)) {
            cabeza = cabeza.sigte;
            
            if (cabeza == null) {
                cola = null; //Lista quedo vacia
            }
            
            size--;
            
            return true;
        }
        
        //Busca en el resto
        Entry previo = cabeza;
        Entry actual = cabeza.sigte;
        
        while (actual != null) {
            if (actual.getUsername().equals(username)) {
                previo.sigte = actual.sigte;
                
                if (actual == cola) {
                    cola = previo;
                }
                
                size--;
                return true;
            }
            
            previo = actual;
            actual = actual.sigte;
        }
        
        return false;
    }
    
    /*
        Busca por username, si se encuentra al usuario, entonces retorna la posicion guardada
        Si no, retorna -1
    */
    public long search(String username) {
        Entry temp = cabeza;
        
        while(temp != null) {
            if (temp.getUsername().equals(username)) {
                return temp.getPos();
            }
            
            temp = temp.sigte;
        }
        
        return -1L;
    }
    
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}

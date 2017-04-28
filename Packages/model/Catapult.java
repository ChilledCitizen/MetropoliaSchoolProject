package model;

import ammo.Ammo;

//KAAVA:  range=lähtönopeus^2/painovoima*sin(2*kulma)
// launchAmmo = force^2/9.81*sin(2*angle)
public class Catapult {

    public int position;

    public Catapult(int position) {
        this.position = position;
    }

    public void launchAmmo(double angle, double force) {
        
    }
    
    void update(double time){
        
    }
}

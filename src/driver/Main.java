package driver;

import models.turing.Transition;

public class Main {
    public static void main(String[] args) {
        new Transition("ɑɑββγβɑFKF⊔"); //Correct result = xxxxxβxxxx⊔
        //new Transition("ɑɑβγFKF⊔"); //Correct result = xxxxxxF⊔
        //new Transition("ɑβɑγFNF⊔"); //Correct result = xxxxxxx⊔
        //new Transition("γβɑSK⊔"); //Correct result = xβɑxK⊔
        //new Transition("δNKF⊔"); //Correct result = invalid
        //new Transition("βδNKF⊔"); //Correct result = refund $10
        //new Transition("γN⊔"); //Correct result = βx⊔
    }
}
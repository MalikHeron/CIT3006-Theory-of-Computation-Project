package driver;

import models.turing.Transition;
import models.turing.Turing;

public class Main {
    public static void main(String[] args) {
        //new Turing("ɑɑβββγβɑFKFS⊔"); //Correct result = xxxxxxxxxxx⊔
        //new Turing("ɑɑββγβɑFKF⊔"); //Correct result = xxxxxβxxxx⊔
        //new Turing("ɑɑβγFKF⊔"); //Correct result = xxxxxxF⊔
        new Turing("ɑβɑγFNF⊔"); //Correct result = xxxxxxx⊔
        //new Turing("γβɑSK⊔"); //Correct result = xβɑxK⊔
        //new Turing("δNKF⊔"); //Correct result = invalid
        //new Turing("βδNKF⊔"); //Correct result = refund $10
        //new Turing("γN⊔"); //Correct result = βx⊔
        //new Turing("ɑɑɑɑS⊔"); //Correct result = xxxxx⊔
    }
}
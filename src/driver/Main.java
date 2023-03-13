package driver;

import models.turing.Transition;

public class Main {
    public static void main(String[] args) {
        new Transition("ɑɑββγβɑFKF$"); //Correct result = #####β#xxx$
        //new Transition("ɑɑβγFKF$"); //Correct result = ####xxF$
        //new Transition("ɑβɑγFNF$"); //Correct result = ####xxx$
        //new Transition("γβɑSK$"); //Correct result = #βɑxK$
        //new Transition("δNKF$"); //Correct result = invalid
        //new Transition("γN$"); //Correct result = refund $10
    }
}
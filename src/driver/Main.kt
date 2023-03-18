package driver

import models.turing.Turing

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        //Turing("ɑɑβββγβɑFKFS⊔") //Correct result = xxxxxxxxxxx⊔
        //Turing("ɑɑββγβɑFKF⊔") //Correct result = xxxxxBxxxx⊔
        //Turing("ɑɑβγFKF⊔") //Correct result = xxxxxxF⊔
        //Turing("ɑβɑγFNF⊔") //Correct result = xxxxxxx⊔
        Turing("γβɑSK⊔") //Correct result = xBAxK⊔
        //Turing("βγɑSK⊔") //Correct result = BxAxK⊔
        //Turing("δNKF⊔") //Correct result = invalid
        //Turing("βδNKF⊔") //Correct result = refund $10
        //Turing("γN⊔") //Correct result = Bx⊔
        //Turing("ɑɑɑɑS⊔") //Correct result = xxxxx⊔
        //Turing("NKSFFSKS⊔")
    }
}
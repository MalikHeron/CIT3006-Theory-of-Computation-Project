# CIT3006 Theory of Computation Group Project

Our project uses a 3-tape turing machine to operate a vending machine.

## Project Specifications

- Kotlin programming language
- Maven package repository
- IntelliJ Idea - JDK v1.8

## Project Structure

```bash
├── data/                           # sales and inventory files
├── res/                            # PNGs, JPEGs, GIFs etc
├── src/                            # project source code
    ├─ ui/                          # GUI
        ├─ MainScreen.kt            # main window
        ├─ SalesDialog.kt           # sales report popup
        ├─ TransactionDialog.kt     # results output popup
    ├─ Inventory.kt                 # handle updating inventory
    ├─ Machine.kt                   # tape and input alphabets
    ├─ Main.kt                      # application entry point
    ├─ Register.kt                  # calculations
    ├─ State.kt                     # tapes, states, transitions
    └─ Turing.kt                    # the turing machine
├── test/
    └─ TuringTest.kt                # test the turing machine
└── README.md
└── Project Outline.pdf
```

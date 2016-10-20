# Project-4
Grant Guglielmo gg25488
Mohit Joshi msj696
Critters project

During each step of our program, we call worldTimeStep() in which doTimeStep() is called for every Critter in the population, then all encounters between bugs are resolved with fight() being called for every Critter that encountered another Critter, then all babies are added to the population, then all dead bugs are removed from the population, and finally algae is added to the population.

This program is controlled by a command line interface, with all commands shown below
Commands:
    make <Critter class> [<# to make>]      create Critters of specified class and add them to the population, default to 1
    quit                                    exit program
    show                                    display world grid of all critters
    stats <Critter class>                   display all stats for the specified Critter class
    step [<# of steps>]                     call worldstep specified number of times, default to 1
    seed <Seed number>                      seed random number generator

Our Critter population is stored in a private static List<Critter> within the class Critter, along with our babies popluation.
We created the classes Critter1, Critter2, Critter3, Critter4 and EncounterList.
Critter1-4 all extend Critter and only implement fight(), doTimeStep(), and default constructor.
Criiter1-4 also all have some kind of private field that is used to decide how they will fight or move during any timestep.
EncounterList is a class that holds an arrayList of arrayList<Critter> that is used to store any Critters that happen to land on the same spot during their timesteps. EncunterLsit has a default constructor and an add method to add 2 Critters to the list.

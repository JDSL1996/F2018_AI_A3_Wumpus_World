public class World {
    //DONE: make cave network @4,5,8,10
    private Cave[] network = new Cave[1];

    World(){
        initializeWorld();
    }

    private void initializeWorld(){
        Cave c4 = new Cave(4,4);
        Cave c5 = new Cave(5,5);
        Cave c8 = new Cave(8,8);
        Cave c10 = new Cave(10,10);

        network[0] = c4;
//        network[1] = c5;
//        network[2] = c8;
//        network[3] = c10;
    }

    //Done: take agent send into cave
    //Done: print a report metric for each cave entered
    void ventureForth(Agent fred){
        for (Cave x: network) {
            x.revealCavePretty();
            fred.enterCave(x);
            Report report = fred.whatHappened();
            report.printReport();
        }
    }
    //TODO: possible total report for world (we lost 2 agents but got 2 bars)
}

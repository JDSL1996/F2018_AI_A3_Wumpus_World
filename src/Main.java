public class Main {
    public static void main(String args[]){
        /*
            Done: create and store multiple caves @ 4,5,8,10

            TODO: create ai player

            TODO: get game run report of end, entered cells, performance measure
         */

        // create the ai agent
        Agent fred = new Agent();
        //create the world (cave network)
        World world = new World();

        //send agent to explore caves
        world.ventureForth(fred);

    }
}

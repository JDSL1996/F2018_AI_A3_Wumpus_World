public interface WorldItemInterface {
    //returns type of item (Wumpus, gold, pit)
    String type();
    //place item in cave
    void place(int[] location);
    //set adjacent squares upon placement
    private void fillAdjacent(int[] location){
        //TODO: add adjacency code here based on type and location and cave

        //IDK about this one, want me to implement but this is an interface....
    }
}

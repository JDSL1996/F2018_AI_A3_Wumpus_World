public class Main {
    public static void main(String args[]){
        Cave cave1 = new Cave(4, 4, 0);
        Cave cave2 = new Cave(5, 5, 10);
        Cave cave3 = new Cave(8, 8, 0);
        Cave cave4 = new Cave(10, 10, 0);

        Player player = new Player();
        player.enterCave(cave4);
        cave4.display();
        player.displayLog();
    }
}

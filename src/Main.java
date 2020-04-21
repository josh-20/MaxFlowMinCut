public class Main {
    public static void main(String[] args) {
        Graph graph1 = new Graph();
        Graph graph2 = new Graph();
        Graph graph3 = new Graph();
        Graph graph4 = new Graph();
        Graph graph5 = new Graph();
        graph1.makeGraph("demands1.txt");
        graph1.go();
        graph2.makeGraph("demands2.txt");
        graph2.go();
        graph3.makeGraph("demands3.txt");
        graph3.go();
        graph4.makeGraph("demands4.txt");
        graph4.go();
        graph5.makeGraph("demands5.txt");
        graph5.go();
    }
}

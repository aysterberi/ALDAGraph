package alda.graph;

import java.util.*;

/**
 * Created by Joakim on 2016-02-22.
 */
public class MyUndirectedGraph<T> implements UndirectedGraph {

    private Set<T> nodeSet = new HashSet<>();
    private ArrayList<Edge<T>> edgeList = new ArrayList<>();

    @Override
    public int getNumberOfNodes() {
        return nodeSet.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edgeList.size();
    }

    @Override
    public boolean add(Object newNode) {
        if (nodeSet.contains(newNode)) {
            return false;
        }
        nodeSet.add((T) newNode);
        return true;
    }

    @Override
    public boolean connect(Object node1, Object node2, int cost) {
        Edge<T> edge = new Edge<>((T) node1, (T) node2, cost);
        if (!nodeSet.contains((T) node1) || !nodeSet.contains((T) node2) || !(cost > 0)) {
            return false;
        }
        for (Edge<T> e : edgeList) {
            if ((e.one.equals(edge.one) || e.two.equals(edge.two)) && (e.two.equals(edge.one) || e.one.equals(edge.two))) {
                return false;
            }
        }
        edgeList.add(edge);
        return true;
    }

    @Override
    public boolean isConnected(Object node1, Object node2) {
        for (Edge<T> edge : edgeList) {
            if ((edge.one.equals(node1) || edge.two.equals(node1)) &&
                    (edge.one.equals(node2) || edge.two.equals(node2))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCost(Object node1, Object node2) {
        for (Edge<T> edge : edgeList) {
            if (isConnected(node1, node2) || isConnected(node2, node1)) {
                return edge.weight;
            }
        }
        return -1;
    }

    @Override
    public List depthFirstSearch(Object start, Object end) {
        return null;
    }

    @Override
    public List breadthFirstSearch(Object start, Object end) {
        return null;
    }

    @Override
    public UndirectedGraph minimumSpanningTree() {
        return null;
    }

    class Edge<T> {
        private T one;
        private T two;
        private int weight;

        public Edge(T one, T two, int weight) {
            this.one = one;
            this.two = two;
            this.weight = weight;
        }

        public T getOne() {
            return one;
        }

        public T getTwo() {
            return two;
        }

        public int getWeight() {
            return weight;
        }
    }
}

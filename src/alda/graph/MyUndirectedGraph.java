package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T extends Comparable<? super T>> implements UndirectedGraph<T> {

    private Set<Node<T>> nodeSet = new HashSet<>();
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
    public boolean add(T newNode) {
        for(Node<T> node : nodeSet) {
            if (node.data.equals(newNode)) {
                return false;
            }
        }
        nodeSet.add(new Node<T>(newNode));
        return true;
    }

    @Override
    public boolean connect(T node1, T node2, int weight) {
        if(nodeSet.contains(node1) && nodeSet.contains(node2)) {
            if(weight > 0) {
                if(!isConnected(node1, node2)) {
                    Edge<T> edge = new Edge<>(node1, node2, weight);
                    edgeList.add(edge);
                    return true;
                } else if(isConnected(node1, node2)) {
                    updateCost(node1, node2, weight);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateCost(T node1, T node2, int weight) {
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode == node1 && edge.anotherNode == node2 ||
                    edge.oneNode == node2 && edge.anotherNode == node1) {
                edge.weight = weight;
            }
        }
    }

    @Override
    public boolean isConnected(T node1, T node2) {
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode == node1 && edge.anotherNode == node2 ||
                    edge.oneNode == node2 && edge.anotherNode == node1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCost(T node1, T node2) {
        for (Edge<T> edge : edgeList) {
            if (isConnected(node1, node2)) {
                return edge.weight;
            }
        }
        return -1;
    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        Queue<T> queue = new LinkedList<>();
        Set<T> checked = new HashSet<>();
        queue.add(start);
        return null;
    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        return null;
    }

    @Override
    public UndirectedGraph<T> minimumSpanningTree() {
        return null;
    }

    class Edge<T> {
        private T oneNode;
        private T anotherNode;
        private int weight;

        public Edge(T oneNode, T anotherNode, int weight) {
            this.oneNode = oneNode;
            this.anotherNode = anotherNode;
            this.weight = weight;
        }
    }

    class Node<T> {
        private T data;
        private boolean visited;

        public Node(T data) {
            this.data = data;
            visited = false;
        }
    }

}

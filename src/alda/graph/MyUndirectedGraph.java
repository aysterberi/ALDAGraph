package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

    private Set<Node<T>> nodeSet = new HashSet<>();
    private ArrayList<Edge<T>> edgeList = new ArrayList<>();

    public MyUndirectedGraph() {}

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
        if(nodeExist(node1) && nodeExist(node2)) {
            if(weight > 0) {
                if(!edgeExist(node1, node2)) {
                    Edge<T> edge = new Edge<>(new Node<T>(node1), new Node<>(node2), weight);
                    edgeList.add(edge);
                    return true;
                } else if(edgeExist(node1, node2)) {
                    updateCost(node1, node2, weight);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean nodeExist(T oneNode) {
        for(Node<T> node : nodeSet) {
            if(node.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    public boolean edgeExist(T oneNode, T anotherNode) {
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isConnected(T oneNode, T anotherNode) {
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode.data.equals(oneNode) && edge.anotherNode.equals(anotherNode) ||
                    edge.oneNode.equals(anotherNode) && edge.anotherNode.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    private void updateCost(T oneNode, T anotherNode, int weight) {
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                edge.weight = weight;
            }
        }
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

    class Edge<T> implements Comparable<Edge<T>> {
        private Node<T> oneNode;
        private Node<T> anotherNode;
        private int weight;

        public Edge(Node<T> oneNode, Node<T> anotherNode, int weight) {
            this.oneNode = oneNode;
            this.anotherNode = anotherNode;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge<T> other) {
            if(other.weight < weight) {
                return 1;
            } else if(other.weight > weight) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    class Node<T> implements Comparable<Node<T>> {
        private T data;
        private boolean visited;

        public Node(T data) {
            this.data = data;
            visited = false;
        }

        @Override
        public int compareTo(Node<T> other) {
            if(other.data.equals(data)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}

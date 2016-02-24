package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

    private Set<Node<T>> nodeSet = new HashSet<>();
    private ArrayList<Edge<T>> edgeList = new ArrayList<>();
    private Map<T, Node<T>> nodeMap = new HashMap<>();

    public MyUndirectedGraph() {
    }

    @Override
    public int getNumberOfNodes() {
        return nodeSet.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edgeList.size();
    }

    @Override
    public boolean add(T newNodeData) {
        for (Node<T> node : nodeSet) {
            if (node.data.equals(newNodeData)) {
                return false;
            }
        }
        Node<T> aNode = new Node<>(newNodeData);
        nodeSet.add(aNode);
        nodeMap.put(newNodeData, aNode);
        return true;
    }

    @Override
    public boolean connect(T node1, T node2, int weight) {
        if (weight <= 0) {
            return false;
        }
        if (!nodeMap.containsKey(node1) || !nodeMap.containsKey(node2)) {
            return false;
        }
        //undvik multigraf, dvs, flera kanter med samma
        //noder
        for (Edge<T> e : edgeList) {
            if (e.oneNode.data.equals(node1) && e.anotherNode.data.equals((node2))) {
                if (e.weight != weight) {
                    e.weight = weight; //uppdatera vikt ifall redan finns
                    return true;
                }
                return false; //if weight unchanged
            }
        }

        Node<T> firstnode = nodeMap.get(node1);
        Node<T> secondnode = nodeMap.get(node2);
        Edge<T> tEdge = new Edge<>(firstnode, secondnode, weight);
        edgeList.add(tEdge);
        return true;
    }

    public boolean nodeExist(T oneNode) {
        for (Node<T> node : nodeSet) {
            if (node.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    public boolean edgeExist(T oneNode, T anotherNode) {

        for (Edge<T> edge : edgeList) {
            if (edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isConnected(T oneNode, T anotherNode) {
        //TODO: no idea why it doesn't work
        for (Edge<T> edge : edgeList) {
            if (edge.oneNode.data.equals(oneNode) && edge.anotherNode.equals(anotherNode) ||
                    edge.oneNode.equals(anotherNode) && edge.anotherNode.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    private void updateCost(T oneNode, T anotherNode, int weight) {
        for (Edge<T> edge : edgeList) {
            if (edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
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
            if (other.weight < weight) {
                return 1;
            } else if (other.weight > weight) {
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
            if (other.data.equals(data)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}

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
            if (e.oneNode.data.equals(node1) && e.anotherNode.data.equals((node2)) || e.oneNode.data.equals(node2) && e.anotherNode.data.equals(node1)) {
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
        secondnode.neighbors.add(node1);
        firstnode.neighbors.add(node2);
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
        boolean nodeConnected = false;
        //TODO: no idea why it doesn't work
        for (Edge<T> edge : edgeList) {
            if (edgeContains(edge, oneNode, anotherNode)) {
                nodeConnected = true;
                break;
            } else {
                nodeConnected = false;
                break;
            }
        }
        return nodeConnected;
    }

    private void updateCost(T oneNode, T anotherNode, int weight) {
        for (Edge<T> edge : edgeList) {
            if (edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                edge.weight = weight;
            }
        }
    }

    private boolean edgeContains(Edge<T> edge, T data, T data2) {
        if (edge.oneNode.data.equals(data) && edge.anotherNode.data.equals(data2)) {
            return true;
        } else if (edge.oneNode.data.equals(data2) && edge.anotherNode.data.equals(data)) {
            return true;
        }
        return false;
    }

    @Override
    public int getCost(T node1, T node2) {
        for (Edge<T> edge : edgeList) {
            if (edgeContains(edge, node1, node2)) {
                return edge.weight;
            }
        }
        return -1;
    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        Set<Node<T>> visited = new HashSet<>();
        depthFirstSearch(nodeMap.get(start), visited);
        List<T> results = new ArrayList<>();
        for (Node<T> n : visited) {
            results.add(n.data);
        }
        return results;
    }

    private void depthFirstSearch(Node<T> from, Set<Node<T>> visited) {
        visited.add(from);
        for (Edge<T> e : edgeList) {
            Node<T> tmp = null;

            if (e.oneNode.equals(from) || e.anotherNode.equals(from)) {
                tmp = e.oneNode.equals(from) ? e.anotherNode : e.oneNode;
            }
            if (!visited.contains(tmp)) {
                depthFirstSearch(tmp, visited);
            }
        }
    }

    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        // TODO: Jag är något på spåren här
        if (!nodeMap.containsKey(start) && !nodeMap.containsKey(end)) {
            return null;
        }

        LinkedList<T> queue = new LinkedList<>();
        LinkedList<T> result = new LinkedList<>();
        queue.addLast(start);
        nodeMap.get(start).visited = true;

        if (start.equals(end)) {
            return queue;
        }

        while(!queue.isEmpty()) {
            T data = queue.removeFirst();
        }

//            for(T t : nodeMap.get(data).neighbors) { //TODO: Denna metod var närmast. Men väljer fel previous
//                if(!t.equals(start)) {
//                    nodeMap.get(t).previous = data;
//                }
//                if(!nodeMap.get(t).visited) {
//                    nodeMap.get(t).visited = true;
//                    queue.addLast(t);
//                }
//                if(t.equals(end)) {
//                    result.addFirst(end);
//                    while(nodeMap.get(t).previous != null) {
//                        result.addFirst(nodeMap.get(t).previous);
//                        t = nodeMap.get(t).previous;
//                    }
//                    return result;
//                }
//            }
//        }

//        nodeMap.get(start).visited = true;
//        while(!queue.isEmpty()) {
//            T data = queue.removeFirst();
//            for(T t : nodeMap.get(data).neighbors) {
//                node.previous = new Node<>(data);
//                queue.addLast(node.data);
//                if(!node.visited) {
//                    node.visited = true;
//                } else if(node.data.equals(end)) {
//                    while(node.previous != null) {
//                        result.addFirst(node.previous.data);
//                        node = node.previous;
//                    }
//                }
//            }
//        }

//        nodeMap.get(start).visited = true;
//
//        while (!queue.isEmpty()) {
//            T data = queue.removeFirst();
//            for (Node<T> node : nodeMap.get(data).neighbors) { //TODO: something something dark side
//                if (!node.visited && !node.neighbors.contains(getNodeDataFromNeighbor(node, end))) {
//                    node.visited = true;
//                    queue.addLast(node.data);
//                } else if (node.neighbors.contains(getNodeDataFromNeighbor(node, end))) {
//                    break;
//                }
//            }
//        }
        return result;
    }

    // TODO: Den här metoden kan vara mycket enklare
    private boolean getNodeDataFromNeighbor(Node<T> node, T end) {
        for (T n : node.neighbors) {
            if (n.equals(end)) {
                return true;
            }
        }
        return false;
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
        private Set<T> neighbors;
        private T previous;
        private boolean visited;

        public Node(T data) {
            this.data = data;
            visited = false;
            neighbors = new HashSet<>();
            previous = null;
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

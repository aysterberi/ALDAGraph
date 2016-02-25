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
        if(!nodeMap.containsKey(oneNode) || !nodeMap.containsKey(anotherNode)) {
            return false;
        }
        for(Edge<T> edge : edgeList) {
            if(edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
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
        T temp = end;
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
            for(T t : nodeMap.get(data).neighbors) {
                if(nodeMap.get(t).previous == null) {
                    nodeMap.get(t).previous = data;
                }
                if(!nodeMap.get(t).visited) {
                    queue.addLast(t);
                }
                nodeMap.get(t).visited = true;
            }
        }
        while(!nodeMap.get(temp).previous.equals(start)) {
                result.addFirst(nodeMap.get(temp).previous);
                temp = nodeMap.get(temp).previous;
        }
        result.addLast(end);
        result.addFirst(start);
        return result;
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

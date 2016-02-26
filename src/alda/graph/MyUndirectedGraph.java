/**
 * Billy G. J. Beltran(bibe1744) & Joakim Berglund(jobe7147)
 * Contact details: billy@caudimordax.org, joakimberglund@live.se
 */

package alda.graph;

import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {
    private ArrayList<Edge<T>> edgeList = new ArrayList<>();
    private Map<T, Node<T>> nodeMap = new HashMap<>();

    public MyUndirectedGraph() {
    }

    @Override
    public int getNumberOfNodes() {
        return nodeMap.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edgeList.size();
    }

    @Override
    public boolean add(T newNodeData) {
        if (nodeMap.containsKey(newNodeData)) {
            return false;
        }
        Node<T> aNode = new Node<>(newNodeData);
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
        if (edgeExist(node1, node2)) {
            Edge<T> edge = getEdge(node1, node2);
            if (edge != null) {
                edge.weight = weight;
            }
            return true;
        }
        if (!edgeExist(node1, node2)) {
            Node<T> firstNode = nodeMap.get(node1);
            Node<T> secondNode = nodeMap.get(node2);
            Edge<T> tEdge = new Edge<>(firstNode, secondNode, weight);
            edgeList.add(tEdge);
            secondNode.neighbors.add(node1);
            firstNode.neighbors.add(node2);
            return true;
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

    private Edge<T> getEdge(T node1, T node2) {
        for (Edge<T> e : edgeList) {
            if (e.oneNode.data.equals(node1) && e.anotherNode.data.equals(node2) ||
                    e.oneNode.data.equals(node2) && e.anotherNode.data.equals(node1)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean isConnected(T oneNode, T anotherNode) {
        if (!nodeMap.containsKey(oneNode) || !nodeMap.containsKey(anotherNode)) {
            return false;
        }
        for (Edge<T> edge : edgeList) {
            if (edge.oneNode.data.equals(oneNode) && edge.anotherNode.data.equals(anotherNode) ||
                    edge.oneNode.data.equals(anotherNode) && edge.anotherNode.data.equals(oneNode)) {
                return true;
            }
        }
        return false;
    }

    public boolean edgeContains(Edge<T> edge, T data, T data2) {
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

    private boolean allNeighborsVisited(T data) {
        for (T t : nodeMap.get(data).neighbors) {
            if (!nodeMap.get(t).visited) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<T> depthFirstSearch(T start, T end) {
        for (T t : nodeMap.keySet()) {
            nodeMap.get(t).visited = false;
        }
        if (start == null) {
            return null;
        }
        T data = start;
        if (!nodeMap.containsKey(start) && !nodeMap.containsKey(end)) {
            return null;
        }

        Stack<T> stack = new Stack<>();
        LinkedList<T> result = new LinkedList<>();
        stack.push(start);
        nodeMap.get(start).visited = true;

        if (start.equals(end)) {
            return stack;
        }
        while (!stack.isEmpty()) {
            if (allNeighborsVisited(stack.peek()) && !stack.peek().equals(end)) {
                stack.pop();
            } else {
                for(T t : nodeMap.get(data).neighbors) {
                    if(!nodeMap.get(t).visited && isConnected(nodeMap.get(t).data, nodeMap.get(data).data)) {
                        nodeMap.get(t).visited = true;
                        stack.push(t);
                    }
                    data = stack.peek();
                }
            }
            if (stack.peek().equals(end)) {
                break;
            }
        }
        while (!stack.isEmpty()) {
            result.addFirst(stack.pop());
        }
        return result;
    }


    @Override
    public List<T> breadthFirstSearch(T start, T end) {
        if (start == null) {
            return null;
        }
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

        while (!queue.isEmpty()) {
            T data = queue.removeFirst();
            for (T t : nodeMap.get(data).neighbors) {
                if (nodeMap.get(t).previous == null) {
                    nodeMap.get(t).previous = data;
                }
                if (!nodeMap.get(t).visited) {
                    queue.addLast(t);
                }
                nodeMap.get(t).visited = true;
            }
        }
        while (!nodeMap.get(temp).previous.equals(start)) {
            result.addFirst(nodeMap.get(temp).previous);
            temp = nodeMap.get(temp).previous;
        }
        result.addLast(end);
        result.addFirst(start);
        return result;
    }

    @Override
    public UndirectedGraph<T> minimumSpanningTree() {
        //Kruskals algoritm
        ArrayList<Edge<T>> sortedEdges = new ArrayList<>(); //vi ska inte mecka med gamla grafen
        Collections.sort(edgeList);
        sortedEdges.addAll(edgeList); //var PQ
        HashMap<Node<T>, Set<Node<T>>> forest = new HashMap<>();
        //skapa en skog for varje nod
        for (T t : nodeMap.keySet()) {
            Set<Node<T>> ns = new HashSet<>();
            ns.add(nodeMap.get(t));
            forest.put(nodeMap.get(t), ns);
        }
        List<Edge<T>> minSpanTree = new ArrayList<>();
        while (true) {
            Edge<T> check = sortedEdges.remove(0); //anvand som en priority queue

            //besok noderna
            Set<Node<T>> visited1 = forest.get(check.anotherNode);
            Set<Node<T>> visited2 = forest.get(check.oneNode);
            if (visited1.equals(visited2))
                continue;
            minSpanTree.add(check);
            visited1.addAll(visited2);
            for (Node<T> t : visited1) {
                forest.put(t, visited1);
            }
            if (visited1.size() == nodeMap.size()) //vi har alla noder
                break;
        }
        MyUndirectedGraph minSpanTreeGraph = new MyUndirectedGraph(); //bygg upp ny graf
        for (Edge<T> e : minSpanTree) {
            T ta = e.oneNode.data;
            T tb = e.anotherNode.data;
            minSpanTreeGraph.add(ta);
            minSpanTreeGraph.add(tb);
            minSpanTreeGraph.connect(ta, tb, e.weight);
        }
        return minSpanTreeGraph;
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

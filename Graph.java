/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fooddelivery2;

/**
 *
 * @author ASUS
 */
// Graph.java


public class Graph {
    private CustomHashMap<String, CustomArrayList<Edge>> adjacencyList;
    private CustomArrayList<String> nodes;
    
    public Graph() {
        adjacencyList = new CustomHashMap<>(50);
        nodes = new CustomArrayList<>(50);
    }
    
    private static class Edge {
        String destination;
        int distance;
        
        Edge(String destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
        
        @Override
        public String toString() {
            return destination + "(" + distance + ")";
        }
    }
    
    public void addNode(String location) {
        if (!adjacencyList.containsKey(location)) {
            adjacencyList.put(location, new CustomArrayList<>(10));
            nodes.add(location);
        }
    }
    
    public void addEdge(String source, String destination, int distance) {
        addNode(source);
        addNode(destination);
        
        CustomArrayList<Edge> edges = adjacencyList.get(source);
        if (edges != null) {
            edges.add(new Edge(destination, distance));
        }
        
        // For undirected graph, add reverse edge
        CustomArrayList<Edge> reverseEdges = adjacencyList.get(destination);
        if (reverseEdges != null) {
            reverseEdges.add(new Edge(source, distance));
        }
    }
    
    public CustomArrayList<String> findShortestPath(String start, String end) {
        // BFS for unweighted graph (minimum number of edges)
        CustomHashMap<String, String> previous = new CustomHashMap<>(50);
        CustomHashMap<String, Boolean> visited = new CustomHashMap<>(50);
        CustomQueue<String> queue = new CustomQueue<>(50);
        
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return new CustomArrayList<>(0);
        }
        
        queue.enqueue(start);
        visited.put(start, true);
        
        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            
            if (current != null && current.equals(end)) {
                break;
            }
            
            CustomArrayList<Edge> neighbors = adjacencyList.get(current);
            if (neighbors != null) {
                for (int i = 0; i < neighbors.size(); i++) {
                    Edge edge = neighbors.get(i);
                    if (edge != null && !visited.containsKey(edge.destination)) {
                        visited.put(edge.destination, true);
                        previous.put(edge.destination, current);
                        queue.enqueue(edge.destination);
                    }
                }
            }
        }
        
        // Reconstruct path
        CustomArrayList<String> path = new CustomArrayList<>(20);
        String current = end;
        
        while (current != null) {
            path.insertAt(0, current); // Add to beginning
            current = previous.get(current);
        }
        
        // Check if path was found
        if (path.size() == 1 && !path.get(0).equals(start)) {
            return new CustomArrayList<>(0); // Empty path - no connection
        }
        
        return path;
    }
    
    public int getNodeCount() {
        return nodes.size();
    }
    
    
    public void displayGraph() {
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(i);
            System.out.print(node + " -> ");
            CustomArrayList<Edge> edges = adjacencyList.get(node);
            if (edges != null) {
                for (int j = 0; j < edges.size(); j++) {
                    Edge edge = edges.get(j);
                    if (edge != null) {
                        System.out.print(edge + " ");
                    }
                }
            }
            System.out.println();
        }
    }
    
    // Add this method to Graph class
public int getEdgeCount() {
    int count = 0;
    for (int i = 0; i < nodes.size(); i++) {
        String node = nodes.get(i);
        CustomArrayList<Edge> edges = adjacencyList.get(node);
        if (edges != null) {
            count += edges.size();
        }
    }
    return count / 2; // Divide by 2 for undirected graph
}

public String getGraphInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("CAMPUS MAP - ADJACENCY LIST\n");
    sb.append("══════════════════════════════════════════════════\n\n");
    
    for (int i = 0; i < nodes.size(); i++) {
        String node = nodes.get(i);
        sb.append(node).append(" → ");
        CustomArrayList<Edge> edges = adjacencyList.get(node);
        if (edges != null && edges.size() > 0) {
            for (int j = 0; j < edges.size(); j++) {
                Edge edge = edges.get(j);
                if (edge != null) {
                    sb.append(edge.destination).append("(").append(edge.distance).append(")");
                    if (j < edges.size() - 1) sb.append(", ");
                }
            }
        } else {
            sb.append("(no connections)");
        }
        sb.append("\n");
    }
    
    sb.append("\n══════════════════════════════════════════════════\n");
    sb.append("Total Nodes: ").append(nodes.size()).append("\n");
    sb.append("Total Edges: ").append(getEdgeCount()).append("\n");
    
    return sb.toString();
}    
    
}
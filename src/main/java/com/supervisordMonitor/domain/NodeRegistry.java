package com.supervisordMonitor.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NodeRegistry {

    private List<Node> nodes = new ArrayList<>();

    public void add(String nodeName) throws Exception {
        if (has(nodeName)) {
            throw new Exception("Node with name \"" + nodeName + "\" is already registered");
        }

        Node node = new Node(nodeName);
        nodes.add(node);
    }

    public Boolean has(String nodeName) {
        return get(nodeName) != null;
    }

    public void remove(String nodeName) {
        int size = nodes.size();
        for (int i = 0; i < size; ++i) {
            if (nodes.get(i).getName().equals(nodeName)) {
                nodes.remove(i);

                return;
            }
        }
    }

    public List<Node> all() {
        return nodes;
    }

    public Node get(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }

        return null;
    }
}

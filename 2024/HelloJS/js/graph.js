import { Node } from "./node.js"

/**
 * Models the entire Graph
 */
export class Graph {
    /*
    Class members:
        graphics  - the graphics engine
        nodes     - Map of <label, Node> entries
        nextLabel - the next label to be used when adding a new Node
    */

    constructor(graphics) {
        this.graphics = graphics;
        this.nextLabel = 'A';
        this.nodes = new Map();
    }

    repaint() {
        this.traverse((node)=>{
            node.repaint();
        });
    }

    traverse(lambda) {
        // reset all the node markers
        for(const[label, node] of this.nodes) {
            node.marker = 0;
        }
        // repeteadly ...
        let done = false;
        while (!done) {
            done = true;
            // look for an un-marked node
            for(const[label,node] of this.nodes) {
                // and if found, traverse the node and do it all over again
                if (node.marker == 0) {
                    node.traverse(lambda);
                    done = false;
                    break;
                }
            }
        }
    }

    getNode(x, y) {
        for(const[label, node] of this.nodes.entries()) {
            if (node.isTarget(x, y)) {
                return node;
            }
        }
        return null;
    }

    addNode(x, y) {
        let node = new Node(this.graphics, this.nextLabel, x, y);
        this.nodes.set(node.label, node);
        // increment label
        this.nextLabel = String.fromCharCode(this.nextLabel.charCodeAt(0) + 1);
    }

    removeNode(node) {
        for(const [label, otherNode] of this.nodes) {
            if (otherNode.hasEdge(node)) {
                otherNode.removeEdge(node);
            }
        }
        this.nodes.delete(node.label);
    }

    resetEdge(fromNode, toNode) {
        if (fromNode.hasEdge(toNode)) {
            fromNode.removeEdge(toNode);
            //toNode.removeEdge(fromNode);
        } else {
            fromNode.addEdge(toNode);
            //toNode.addEdge(fromNode);
        }
    }
}

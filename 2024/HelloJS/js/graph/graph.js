import { Node } from "./node.js"

const ALL_LABELS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';

/**
 * Models the entire Graph
 */
export class Graph {
    /*
    Class members:
        graphics  - the graphics engine
        nodes     - Map of <label, Node> entries
    */

    constructor(graphics) {
        this.graphics = graphics;
        this.nodes = new Map();
    }

    nextLabel() {
        for(const label of ALL_LABELS) {
            if (!this.nodes.has(label)) {
                return label;
            }
        }
        return null;
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
        let nextLabel = this.nextLabel();
        if (nextLabel != null) {
            let node = new Node(this.graphics, nextLabel, x, y);
            this.nodes.set(node.label, node);
            // increment label
            //this.nextLabel = String.fromCharCode(this.nextLabel.charCodeAt(0) + 1);
        }
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
        } else {
            fromNode.addEdge(toNode);
        }
    }
}

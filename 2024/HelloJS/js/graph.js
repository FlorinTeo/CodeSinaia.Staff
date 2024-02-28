import { Node } from "./node.js"

/**
 * Models the entire Graph
 */
export class Graph {
    /*
    Class members:
        nodes     - Map of <label, Node> entries
        hCanvas   - the html canvas element hosting the node
        nextLabel - the next label to be used when adding a new Node
    */

    constructor(hCanvas, width, height) {
        this.hCanvas = hCanvas;
        this.nextLabel = 'A';
        this.nodes = new Map();
    }

    repaint() {
        let context = this.hCanvas.getContext("2d");
        for(const[label, node] of this.nodes) {
            node.repaint();
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
        let node = new Node(this.hCanvas, this.nextLabel, x, y);
        this.nodes.set(node.label, node);
        // increment label
        this.nextLabel = String.fromCharCode(this.nextLabel.charCodeAt(0) + 1);
    }

    removeNode(label) {
        this.nodes.delete(label);
    }
}

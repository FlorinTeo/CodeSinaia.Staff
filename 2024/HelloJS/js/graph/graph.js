import { Node } from "./node.js"

export let LABELS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789'.split("").sort();

function nextLabel(label, deltaIndex) {
    let nextIndex = 0;
    if (label != undefined) {
        LABELS.push(label);
        LABELS.sort();
        nextIndex = (LABELS.indexOf(label) + deltaIndex) % LABELS.length;
    } else {
        while(nextIndex < LABELS.length) {
            if (LABELS[nextIndex] > '9') {
                break;
            }
            nextIndex++;
        }
        nextIndex = nextIndex % LABELS.length;
    }
    return LABELS.splice(nextIndex, 1)[0];
}

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
        this.clear();
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

    reLabel(node, deltaY) {
        let label = nextLabel(node.label, Math.sign(deltaY));
        for(const[l, n] of this.nodes) {
            if (n.neighbors.delete(node.label)) {
                n.neighbors.set(label, node);
            }
        }
        this.nodes.delete(node.label);
        this.nodes.set(label, node);
        node.label = label;
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
        if (LABELS.length > 0) {
            let node = new Node(this.graphics, nextLabel(), x, y);
            this.nodes.set(node.label, node);
        }
    }

    removeNode(node) {
        for(const [label, otherNode] of this.nodes) {
            if (otherNode.hasEdge(node)) {
                otherNode.removeEdge(node);
            }
        }
        this.nodes.delete(node.label);
        LABELS.push(node.label);
        LABELS.sort();
    }

    resetEdge(fromNode, toNode) {
        if (fromNode.hasEdge(toNode)) {
            fromNode.removeEdge(toNode);
        } else {
            fromNode.addEdge(toNode);
        }
    }

    matchAll(fMatch) {
        for(const node of this.nodes.values()) {
            if (!fMatch(node)) {
                return false;
            }
        }
        return true;
    }

    clear() {
        this.nodes = new Map();
        LABELS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789'.split("").sort();
    }
}

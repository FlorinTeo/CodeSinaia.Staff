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
        this.resize();
    }

    resize(width, height) {
        this.hCanvas.width = width;
        this.hCanvas.height = height;
    }

    repaint() {
        let context = this.hCanvas.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, this.hCanvas.width, this.hCanvas.height);
        for(const[label, node] of this.nodes) {
            node.repaint();
        }
    }

    onDragStart(fromX, fromY) {
        for(const[label, node] of this.nodes.entries()) {
            if (node.isTarget(fromX, fromY)) {
                this.draggedNode = node;
                return;
            }
        }
    }

    onDrag(x, y) {
        if (this.draggedNode != undefined) {
            this.draggedNode.x = x;
            this.draggedNode.y = y;
            this.repaint();
        }
    }

    onDragEnd(toX, toY) {
        this.onDrag(toX, toY);
        this.draggedNode = undefined;
    }

    onClick(x, y) {
        for(const[label, node] of this.nodes.entries()) {
            if (node.isTarget(x, y)) {
                this.nodes.delete(label);
                this.repaint();
                return;
            }
        }
        // create a new node for that location and paint it on the canvas
        let node = new Node(this.hCanvas, this.nextLabel, x, y);
        this.nodes.set(node.label, node);
        node.repaint();
        // increment label
        this.nextLabel = String.fromCharCode(this.nextLabel.charCodeAt(0) + 1);
    }
}

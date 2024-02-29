/**
 * Models a node in the Graph
 */
export const RADIUS = 16;

export class Node {
    /*
    Class members:
        x, y    - coordinates of the center of this node
        label   - text to be printed inside the node
        hCanvas - the html canvas element hosting the node
    */

    constructor(hCanvas, label, x, y) {
        this.hCanvas = hCanvas;
        this.x = x;
        this.y = y;
        this.label = label;
        this.marker = 0;
        this.neighbors = new Map();
    }

    repaint() {
        let context = this.hCanvas.getContext("2d");
        for(const [label, neighbor] of this.neighbors) {
            if (neighbor.marker == 0) {
                context.beginPath();
                context.strokeStyle = 'black';
                context.lineWidth = 2;
                context.moveTo(this.x, this.y);
                context.lineTo(neighbor.x, neighbor.y);
                context.stroke();
            }
        }
        context.beginPath();
        context.arc(this.x, this.y, RADIUS, 0, 2 * Math.PI, false);
        context.fillStyle = 'lightgray';
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = 'black';
        context.stroke();
        context.font = "bold 12px Arial";
        context.textAlign = "center";
        context.fillStyle = "red";
        context.fillText(this.label, this.x, this.y+4);
    }

    traverse(lambda) {
        this.marker = 1;
        lambda(this);
        for(const [label, node] of this.neighbors) {
            if (node.marker == 0) {
                node.traverse(lambda);
            }
        }
    }

    isTarget(x, y) {
        let d = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
        return d <= RADIUS;
    }

    hasEdge(node) {
        return this.neighbors.has(node.label);
    }

    addEdge(node) {
        this.neighbors.set(node.label, node);
    }

    removeEdge(node) {
        this.neighbors.delete(node.label);
    }
}
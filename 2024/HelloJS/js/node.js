import { Graphics } from "./graphics.js"

/**
 * Models a node in the Graph
 */
export const RADIUS = 16;
export const ARROW_WIDTH = 5;
export const ARROW_LENGTH = 8;

export class Node {
    /*
    Class members:
        graphics - the graphics engine
        x, y     - coordinates of the center of this node
        label    - text to be printed inside the node
    */

    constructor(graphics, label, x, y) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.label = label;
        this.marker = 0;
        this.neighbors = new Map();
    }

    repaint() {
        for(const [label, neighbor] of this.neighbors) {
            if (neighbor.marker == 0) {
                this.graphics.drawLine(this.x, this.y, neighbor.x, neighbor.y, 'black');
                this.graphics.drawArrow(this.x, this.y, neighbor.x, neighbor.y, RADIUS, ARROW_LENGTH, ARROW_WIDTH, 'black');
            }
        }
        this.graphics.drawNode(this.label,this.x, this.y, RADIUS);
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
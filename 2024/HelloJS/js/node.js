import { Graphics } from "./graphics.js"

/**
 * Models a node in the Graph
 */
export const RADIUS = 16;
export const ARROW_WIDTH = 5;
export const ARROW_LENGTH = 8;

const DEFAULT_FILL = '#EBEBEB';
const FILL_PALLETE = ['#9AF4F5', '#94F594', '#F5DC93', '#C999F5'];

export class Node {

    /*
    Class members:
        graphics    - the graphics engine
        x, y        - coordinates of the center of this node
        label       - text to be printed inside the node
        marker      - state holder for this node
        neigbhors   - map of neighbors, indexed by the neighbor's label
        fillColor   - color to be used for filling the ndoe
        fillIndex   - index of the last custom filling color used
    */

    constructor(graphics, label, x, y) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.label = label;
        this.marker = 0;
        this.neighbors = new Map();
        this.fillIndex = 0;
        this.fillColor = DEFAULT_FILL;
    }

    toggleFill(deltaIndex) {
        deltaIndex = Math.sign(deltaIndex) + FILL_PALLETE.length;
        if (deltaIndex != FILL_PALLETE.length) {
            this.fillIndex = (this.fillIndex + deltaIndex) % FILL_PALLETE.length;
        }
        this.fillColor =  (this.fillColor === DEFAULT_FILL || deltaIndex != FILL_PALLETE.length) 
                        ? FILL_PALLETE[this.fillIndex]
                        : DEFAULT_FILL;
    }

    repaint() {
        for(const [label, neighbor] of this.neighbors) {
            if (neighbor.marker == 0 || !neighbor.hasEdge(this)) {
                this.graphics.drawLine(this.x, this.y, neighbor.x, neighbor.y, RADIUS, RADIUS, 'black');
            }
            this.graphics.drawArrow(this.x, this.y, neighbor.x, neighbor.y, RADIUS, ARROW_LENGTH, ARROW_WIDTH, 'black');
        }
        this.graphics.drawNode(this.label,this.x, this.y, RADIUS, this.fillColor);
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
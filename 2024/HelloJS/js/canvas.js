import { Node } from "./node.js"

/**
 * Models the drawing canvas in the html page
 */
export class Canvas {
    constructor(hCanvas, width, height) {
        this.hCanvas = hCanvas;
        this.label = 'A';
        this.nodes = new Map();
        this.repaint(width, height);
    }

    repaint(width, height) {
        this.hCanvas.width = width;
        this.hCanvas.height = height;
        let context = this.hCanvas.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, width, height);
        this.nodes.forEach((node, label) => {
            node.repaint();
        });
    }

    onMouseClickEvent(event) {
        // get the canvas x,y coordinates of the click
        let x = event.clientX - this.hCanvas.offsetLeft;
        let y = event.clientY - this.hCanvas.offsetTop;

        // create a new node for that location and paint it on the canvas
        let node = new Node(this.hCanvas, this.label, x, y);
        this.nodes.set(this.label, node);
        node.repaint();

        // increment label
        this.label = String.fromCharCode(this.label.charCodeAt(0) + 1);
    }
}

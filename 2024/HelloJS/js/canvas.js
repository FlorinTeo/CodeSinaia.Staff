import { Node } from "./node.js"

export class Canvas {
    constructor(hCanvas, width, height) {
        this.hCanvas = hCanvas;
        this.repaint(width, height);
    }

    repaint(width, height) {
        this.label = 'A';
        this.hCanvas.width = width;
        this.hCanvas.height = height;
        let context = this.hCanvas.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, width, height);
        context.font = "bold 12px Arial";
        context.textAlign = "center";
    }

    onMouseClickEvent(event) {
        // get the canvas x,y coordinates of the click
        let x = event.clientX - this.hCanvas.offsetLeft;
        let y = event.clientY - this.hCanvas.offsetTop;

        // create a new node for that location and paint it on the canvas
        let node = new Node(this.hCanvas, this.label, x, y);
        node.repaint();

        // increment label
        this.label = String.fromCharCode(this.label.charCodeAt(0) + 1);
    }
}

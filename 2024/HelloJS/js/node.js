/**
 * Models a node in the Graph
 */
export class Node {
    constructor(hCanvas, label, x, y) {
        this.hCanvas = hCanvas;
        this.x = x;
        this.y = y;
        this.label = label;
    }

    repaint() {
        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.arc(this.x, this.y, 16, 0, 2 * Math.PI, false);
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
}
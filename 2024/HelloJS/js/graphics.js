export class Graphics {

    constructor(hCanvas) {
        this.hCanvas = hCanvas;
    }

    resize(width, height) {
        this.hCanvas.width = width;
        this.hCanvas.height = height;
        this.clear();
    }

    // draws a line between the given coordinates, in given color
    drawLine(fromX, fromY, toX, toY, color) {
        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.strokeStyle = color;
        context.lineWidth = 2;
        context.moveTo(fromX, fromY);
        context.lineTo(toX, toY);
        context.stroke();
    }

    drawNode(label, x, y, radius) {
        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.arc(x, y, radius, 0, 2 * Math.PI, false);
        context.fillStyle = 'lightgray';
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = 'black';
        context.stroke();
        context.font = "bold 12px Arial";
        context.textAlign = "center";
        context.fillStyle = "red";
        context.fillText(label, x, y + 4);
    }

    clear() {
        let context = this.hCanvas.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, this.hCanvas.width, this.hCanvas.height);
    }
}
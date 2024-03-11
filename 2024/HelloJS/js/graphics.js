export class Graphics {

    constructor(hCanvas) {
        this.hCanvas = hCanvas;
    }

    // resizes the drawing canvas
    resize(width, height) {
        this.hCanvas.width = width;
        this.hCanvas.height = height;
        this.clear();
    }

    // draws a line between the given coordinates, in given color
    drawLine(fromX, fromY, toX, toY, marginFrom, marginTo, color) {
        let dX = toX - fromX;
        let dY = toY - fromY;
        let length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        if (length == 0) {
            return;
        }
        fromX += dX * marginFrom/length;
        fromY += dY * marginFrom/length;
        toX -= dX * marginTo/length;
        toY -= dY * marginTo/length;

        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.strokeStyle = color;
        context.lineWidth = 1;
        context.moveTo(fromX, fromY);
        context.lineTo(toX, toY);
        context.stroke();
    }

    // draws a node as a labeled circle
    drawNode(label, x, y, radius, fillColor) {
        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.arc(x, y, radius, 0, 2 * Math.PI, false);
        context.fillStyle = fillColor;
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = 'black';
        context.stroke();
        context.font = "bold 12px Arial";
        context.textAlign = "center";
        context.fillStyle = 'black';
        context.fillText(label, x, y + 4);
    }

    drawArrow(fromX, fromY, toX, toY, marginTo, arrowLength, arrowWidth, color) {
        let dX = toX - fromX;
        let dY = toY - fromY;
        let length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        let pointX = toX - dX * marginTo / length;
        let pointY = toY - dY * marginTo / length;
        length -= marginTo;
        dX = pointX - fromX;
        dY = pointY - fromY;
        let baseX = pointX - dX * arrowLength / length;
        let baseY = pointY - dY * arrowLength / length;
        let xA = baseX + arrowWidth * dY / length;
        let yA = baseY - arrowWidth * dX / length;
        let xB = baseX - arrowWidth * dY / length;
        let yB = baseY + arrowWidth * dX / length;
        let context = this.hCanvas.getContext("2d");
        context.beginPath();
        context.strokeStyle = color;
        context.lineWidth = 1;
        context.moveTo(xA, yA);
        context.lineTo(pointX, pointY);
        context.lineTo(xB, yB);
        context.stroke();
    }

    // clears the drawing canvas
    clear() {
        let context = this.hCanvas.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, this.hCanvas.width, this.hCanvas.height);
    }
}
export class Canvas {
    constructor(hCanvas, width, height) {
        this.hElement = hCanvas;
        this.resize(width, height);
    }

    resize(width, height) {
        this.label = 'A';
        this.hElement.width = width;
        this.hElement.height = height;
        let context = this.hElement.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, width, height);
        context.font = "bold 12px Arial";
        context.textAlign = "center";
    }

    onMouseClickEvent(event) {
        const x = event.clientX - this.hElement.offsetLeft;
        const y = event.clientY - this.hElement.offsetTop;
        let context = this.hElement.getContext("2d");
        context.beginPath();
        context.arc(x, y, 16, 0, 2 * Math.PI, false);
        context.fillStyle = 'lightgray';
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = 'black';
        context.stroke();
        context.fillStyle = "red";
        context.fillText(this.label, x, y+4);
        this.label = String.fromCharCode(this.label.charCodeAt(0) + 1);
    }
}

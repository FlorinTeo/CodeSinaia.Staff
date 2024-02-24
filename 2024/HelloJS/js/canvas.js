export class Canvas {
    constructor(hCanvas, width, height) {
        this.hElement = hCanvas;
        this.resize(width, height);
    }

    resize(width, height) {
        this.hElement.width = width;
        this.hElement.height = height;
        let context = this.hElement.getContext("2d");
        context.fillStyle = "white";
        context.fillRect(0, 0, width, height);
    }

    onMouseClickEvent(event) {
        const x = event.clientX - this.hElement.offsetLeft;
        const y = event.clientY - this.hElement.offsetTop;
        let context = this.hElement.getContext("2d");
        context.beginPath();
        context.arc(x, y, 10, 0, 2 * Math.PI, false);
        context.fillStyle = 'lightgray';
        context.fill();
        context.lineWidth = 2;
        context.strokeStyle = 'red';
        context.stroke();
    }
}

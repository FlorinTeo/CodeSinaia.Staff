export class Item {
    /*
    Class members:
        graphics - the graphics engine
        data     - the data payload in this item.
        prev     - the previous item in this double linked list.
        next     - the next item in this double linked list.
    */

    constructor(graphics, data) {
        this.graphics = graphics;
        this.data = data;
        this.prev = null;
        this.next = null;
    }

    repaint(fromX, fromY, height, color) {
        return this.graphics.drawHText(fromX, fromY, height, color, this.data) + 4;
    }
}
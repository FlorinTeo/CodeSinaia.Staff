import { Item } from "./item.js"
export class Queue {
    /*
    Class members:
        graphics  - the graphics engine
        head    - the head item in the Queue double linked list (or null if queue is empty)
        size    - the number of items in the queue
    */

    constructor(graphics) {
        this.graphics = graphics;
        this.head = null;
        this.size = 0;
    }

    repaint() {
        if (this.size > 0) {
            let crtX = this.graphics.width - 10;
            crtX -= this.graphics.drawVMargin(crtX, 10, 20, 'black') + 4;
            let crtItem = this.head.prev;
            while(crtItem != this.head) {
                crtX -= crtItem.repaint(crtX, 10, 20, 'gray');
                crtItem = crtItem.prev;
            } 
            crtX -= this.head.repaint(crtX, 10, 20, 'black') + 4;
        }
    }

    enqueue(data) {
        let item = new Item(this.graphics, data);

        if (this.head == null) {
            item.next = item;
            item.prev = item;
            this.head = item;
        } else {
            item.next = this.head;
            item.prev = this.head.prev;
            item.next.prev = item;
            item.prev.next = item;
        }
        this.size++;
    }

    dequeue() {
        if (this.head == null) {
            return null;
        }
        let item = this.head;
        item.prev.next = item.next;
        item.next.prev = item.prev;
        this.size--;
        this.head = (this.size == 0) ? null : item.next;
        return item.data;
    }

    peek() {
        return (this.head != null) ? this.head.data : null;
    }

    clear() {
        this.head = null;
        this.size = 0;
    }

    reLabel(prevLabel, newLabel) {
        if (this.head != null) {
            let crtItem = this.head;
            do {
                if (crtItem.data === prevLabel) {
                    crtItem.data = newLabel;
                    return prevLabel;
                }
                crtItem = crtItem.next;
            } while (crtItem != this.head);
        }
        return null;
    }
}

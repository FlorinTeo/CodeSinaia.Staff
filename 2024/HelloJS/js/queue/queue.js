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

    enqueue(item) {
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
        this.head = (item.prev == item.next) ? null : item.next;
        this.size--;
        item.next = item.prev = null;
        return item;
    }
}

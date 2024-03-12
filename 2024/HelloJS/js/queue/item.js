export class Item {
    /*
    Class members:
        data    - the data payload in this item.
        prev    - the previous item in this double linked list.
        next    - the next item in this double linked list.
    */

    constructor(data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
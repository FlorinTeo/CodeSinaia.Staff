export class ContextMenu {
    /*
    Class members:
    hCtxMenu    - html element for this context menu <div>
    menuEntries - Map of {html id, {hP:paragraph, hLabel:label, hInput:input, fOnClick:lambda}}
    fOnClose    - Lambda to be called when menu is closed  
    */

    constructor(ctxMenuId) {
        this.hCtxMenu = document.getElementById(ctxMenuId);
        this.menuEntries = new Map();
        this.loadMenuEntries();
        this.addListeners();
    }

    loadMenuEntries() {
        const hAllElements = this.hCtxMenu.getElementsByTagName('*');
        for (const hElement of hAllElements) {
            if (hElement.id === 'undefined') {
                continue;
            }
            if (hElement.tagName === 'P') {
                this.menuEntries.set(
                    hElement.id,
                    {
                        hP: hElement,
                        hLabel: null,
                        hInput: null,
                        fOnClick: null
                    });
            } else {
                let lastMenuKVP = [...this.menuEntries].pop();
                if (hElement.tagName === 'LABEL') {
                    lastMenuKVP[1].hLabel = hElement;
                } else if (hElement.tagName === 'INPUT') {
                    lastMenuKVP[1].hInput = hElement;
                }
            }
        }
    }

    addListeners() {
        this.hCtxMenu.addEventListener('contextmenu', (event) => { event.preventDefault(); });
        this.hCtxMenu.addEventListener('mouseleave', (event) => { this.onClose(event); });

        for(const menuEntry of this.menuEntries.values()) {
            if (menuEntry.input != null) {
            }
            menuEntry.hP.addEventListener('click', this.onClick);
        }
    }

    onClose(event) {
        if (this.fOnClose != null) {
            this.fOnClose(event);
        }
        this.hide();
        this.fOnClose = null;
    }

    onClick(event) {
        // locate the menu being clicked and process the click
        //this.onClose(event);
    }

    show(x, y, fOnClose) {
        this.hCtxMenu.style.left = x;
        this.hCtxMenu.style.top = y;
        this.hCtxMenu.style.display = 'block';
        this.fOnClose = fOnClose;
    }

    hide() {
        this.hCtxMenu.style.display = 'none';
    }
}
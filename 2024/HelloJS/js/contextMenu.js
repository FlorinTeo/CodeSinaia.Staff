export class ContextMenu {
    /*
    Class members:
    hCtxMenu    - html element for this context menu <div>
    menuEntries - Map of {html id, {hP:paragraph, hLabel:label, hInput:input, fOnClick:lambda}}
    fOnClose    - Lambda to be called when menu is closed
    */

    // #region - setup
    constructor(ctxMenuId) {
        this.hCtxMenu = document.getElementById(ctxMenuId);
        this.menuEntries = new Map();
        this.loadMenuEntries();
        this.setupListeners();
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

    setupListeners() {
        this.hCtxMenu.addEventListener('contextmenu', (event) => { event.preventDefault(); });
        this.hCtxMenu.addEventListener('mouseleave', (event) => { this.onClose(event); });
        for(const menuEntry of this.menuEntries.values()) {
            if (menuEntry.hInput != null) {
                menuEntry.hInput.addEventListener('click', (event) => { this.onClick(event); });
            } else {
                menuEntry.hP.addEventListener('click', (event) => { this.onClick(event); });
            }
        }
    }
    // #endregion - setup

    // #region - internal event handlers
    onClose(event) {
        if (this.fOnClose && this.fOnClose != null) {
            this.fOnClose(event);
            this.fOnClose = null;
        }
        this.hide();
    }

    onClick(event) {
        // locate the menu being clicked and process the click
        this.onClose(event);
    }
    // #endregion - internal event handlers

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
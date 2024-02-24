import { Canvas } from "./canvas.js"

// html elements
export let hDiv = document.getElementById("hMainDiv")
export let hCanvas = document.getElementById("hMainCanvas")

// custom objects
export let canvas = new Canvas(hCanvas, hDiv.clientWidth, hDiv.clientHeight)

// hook resize event handlers
const resizeObserver = new ResizeObserver(entries => {
    for(const entry of entries) {
        switch(entry.target.id) {
        case hDiv.id:
            canvas.resize(entries[0].contentRect.width, entries[0].contentRect.height);
            return;
        }
    }
});
resizeObserver.observe(hDiv);

// hook click event handlers
hCanvas.addEventListener('click', canvas.onMouseClickEvent.bind(canvas));
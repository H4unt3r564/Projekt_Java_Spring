function createResource(id) {
    const el = document.getElementById(id);

    return {
        el,
        value: Number(el.dataset.value),
        rate: Number(el.dataset.rate),
        max: Number(el.dataset.max),
        last: new Date(el.dataset.last).getTime()
    };
}

function calculate(r) {
    const now = Date.now();
    const elapsedSeconds = (now - r.last) / 1000;

    let value = r.value + (r.rate * elapsedSeconds / 3600);

    if (value > r.max) value = r.max;

    return value;
}

function updateModal() {
    document.querySelectorAll(".modal.open .wood-value")
        .forEach(el => {
            el.innerText = Math.floor(wood.display);
        });

    document.querySelectorAll(".modal.open .clay-value")
        .forEach(el => {
            el.innerText = Math.floor(clay.display);
        });

    document.querySelectorAll(".modal.open .iron-value")
        .forEach(el => {
            el.innerText = Math.floor(iron.display);
        });
}

function render(r) {
    r.el.innerText = Math.floor(r.display);
}

const wood = createResource("wood");
const clay = createResource("clay");
const iron = createResource("iron");

setInterval(() => {
    [wood, clay, iron].forEach(r => {
        r.display = calculate(r);
        render(r);
    });

    updateModal();
}, 1000);

const menuBtn = document.getElementById("menuBtn");
const sidebar = document.getElementById("sidebar");


menuBtn.addEventListener("click", () => {

    sidebar.classList.add("open");

});

document.querySelectorAll(".building-node").forEach(tile => {

    tile.addEventListener("click", () => {

        document
            .querySelectorAll(".modal")
            .forEach(m => m.classList.remove("open"));

        tile
            .querySelector(".modal")
            .classList.add("open");

    });

});

document.querySelectorAll(".modal").forEach(menu => {

    menu.addEventListener("click", e => {

        e.stopPropagation();

    });

});

document.querySelectorAll(".modal_close").forEach(btn => {

    btn.addEventListener("click", () => {

        btn.closest(".modal")
            .classList.remove("open");

    });

});

document.getElementById("sidebar_close").addEventListener("click", () => {
    document.getElementById("sidebar").classList.remove("open");
})
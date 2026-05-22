export function createBridgeFrame() {

    return new Promise((resolve) => {
        const iframe = document.createElement("iframe");
        iframe.id = "bridge-frame";
        iframe.src = "http://localhost:5173/bridge.html";
        iframe.style.display = "none";
        iframe.onload = () => {
            resolve();
        };

        document.body.appendChild(iframe);
    });
}
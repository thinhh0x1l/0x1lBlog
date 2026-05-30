export function createBridgeFrame() {

    return new Promise((resolve) => {
        const iframe = document.createElement("iframe");
        iframe.id = "bridge-frame";
        iframe.src = `${import.meta.env.VITE_BRIDGE_FE}/bridge.html`;
        iframe.style.display = "none";
        iframe.onload = () => {
            resolve();
        };

        document.body.appendChild(iframe);
    });
}
export function createBridgeFrame() {

    return new Promise((resolve) => {
        const iframe = document.createElement("iframe");
        iframe.id = "bridge-frame";
        const bridgeUrl = import.meta.env.VITE_BRIDGE_FE;

        iframe.src =
            `${bridgeUrl}/bridge.html?origin=${
                encodeURIComponent(location.origin)
            }`;
        // iframe.src = `${import.meta.env.VITE_BRIDGE_FE}/bridge.html`;
        iframe.style.display = "none";
        iframe.onload = () => {
            resolve();
        };

        document.body.appendChild(iframe);
    });
}
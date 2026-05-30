const BRIDGE_ORIGIN = import.meta.env.VITE_BRIDGE_FE;

export function getGuestToken() {

    return new Promise((resolve, reject) => {

        // iframe bridge
        const iframe = document.getElementById("bridge-frame");

        if (!iframe) {
            reject("Không tìm thấy iframe");
            return;
        }
        console.log("origin: "+origin)
        console.log("BRIDGE_ORIGIN: "+BRIDGE_ORIGIN)
        // timeout
        const timeout =
            setTimeout(() => {
                window.removeEventListener("message", listener);
                reject("Lấy token timeout");
            }, 5000);

        // nhận message
        function listener(event) {
            // sai origin
            if (normalize(event.origin) !==
                normalize(BRIDGE_ORIGIN))
                return;

            const data = event.data;

            // nhận token
            if (data.type === "TOKEN_RESPONSE") {
                clearTimeout(timeout);
                window.removeEventListener("message", listener);
                resolve(data.token);
            }
        }
        const normalize = (url) =>
            url.replace(/\/$/, "").trim()
        // lắng nghe message
        window.addEventListener("message", listener);

        // gửi yêu cầu lấy token
        iframe.contentWindow
            ?.postMessage(
                {
                    type: "GET_TOKEN"
                },
                BRIDGE_ORIGIN
            );
        }
    );
}
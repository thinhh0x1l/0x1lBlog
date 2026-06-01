const GUEST_KEY = "guest";

export function initGuest() {

    const params = new URLSearchParams(window.location.search);
    const guestFromUrl = params.get("guest");
    if (guestFromUrl) {

        localStorage.setItem(GUEST_KEY, guestFromUrl);

        const cleanUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, "", cleanUrl);
    }

    const guest = localStorage.getItem(GUEST_KEY);


    if (!guest) {
        const redirect = encodeURIComponent(window.location.href);

        if (!sessionStorage.getItem("redirecting")) {
            sessionStorage.setItem("redirecting", "1");

            window.location.href =
                `https://cache-matter-checklist-sep.trycloudflare.com/guest?redirect=${redirect}`;
        }

        return false;
    }


    sessionStorage.removeItem("redirecting");

    return true;
}
import {useGuestStore} from "@/store/guessStore.js";
import router from "@/router/index.js";
import {pinia} from "@/store/pinia/pinia.js";

export async function initGuest() {
    const guestStore = useGuestStore(pinia);
    if(guestStore.guestToken || localStorage.getItem(guestStore.TOKEN_KEY)) return true;

    const route = router.currentRoute.value;
    const guestTokenFromUrl = route.query.guest;
    if (guestTokenFromUrl) {
        guestStore.setToken(guestTokenFromUrl);
        const query = { ...route.query };
        delete query.guest;
        await router.replace({ query });  // default có path
        return true;
    }
    const redirect = encodeURIComponent(window.location.href);
    window.location.href =
       `${import.meta.env.VITE_API_URL}guest?redirect=${redirect}`;
    return false;
}
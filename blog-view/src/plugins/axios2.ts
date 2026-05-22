
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {useGuestStore} from "@/store/guessStore";
import {pinia} from "@/store/pinia/pinia";

export const request: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8090/',
    timeout: 10000,
    // withCredentials: true
})

request.interceptors.request.use(
    (
        config: InternalAxiosRequestConfig
    ): InternalAxiosRequestConfig => {

        NProgress.start();

        const guestStore =
            useGuestStore(pinia);

        const isYourApi =  !(config.url)?.includes("http");

        if (  isYourApi && guestStore.guestToken ) {
            config.headers["X-Guest-Token"] = guestStore.guestToken;
        }
        return config;
    },

    // (error: AxiosError) => {
    //
    //     NProgress.done();
    //
    //     return Promise.reject(error);
    // }
);

request.interceptors.response.use(
    (response: AxiosResponse) => {

        NProgress.done();
        const guestStore =
            useGuestStore(pinia);

        const newToken =response.headers[ "x-guest-token" ] as string | undefined;
        if (newToken) {
            guestStore.setToken( newToken  );
        }

        return response.data;
    },

    // (error: AxiosError) => {
    //
    //     NProgress.done();
    //
    //     return Promise.reject(error);
    // }
);

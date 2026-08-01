// composables/useStreamPost.ts

import { ref } from "vue";

export function useStreamPost() {

    const post = ref({
        id: null,
        title: "",
        views: 0,
        readTime: 0,
        content: "",
        category: null,
        tags: []
    });

    const sections = ref<string[]>([]);

    const loading = ref(false);

    const progress = ref(0);

    const error = ref("");

    async function fetchPost(id: number) {

        loading.value = true;
        error.value = "";
        progress.value = 0;
        sections.value = [];

        const response = await fetch(`http://localhost:8090/blog?id=${id}`);
        if (!response.ok) {
            throw new Error("Cannot load post");
        }

        if (!response.body) {
            throw new Error("Browser doesn't support stream");
        }

        const reader = response.body.getReader();

        const decoder = new TextDecoder();

        let buffer = "";

        while (true) {

            const { done, value } = await reader.read();

            if (done) {
                break;
            }

            buffer += decoder.decode(value, {
                stream: true
            });

            const lines = buffer.split("\n");

            buffer = lines.pop() ?? "";

            for (const line of lines) {

                if (!line.trim()) {
                    continue;
                }

                const chunk = JSON.parse(line);

                switch (chunk.id) {

                    case "meta":

                        Object.assign(post.value, chunk.data);

                        break;

                    case "section":

                        sections.value.push(chunk.data.html);

                        post.value.content = sections.value.join("");

                        progress.value =
                            Math.round(
                                chunk.data.current /
                                chunk.data.total *
                                100
                            );

                        break;

                    case "category":

                        post.value.category = chunk.data;

                        break;

                    case "tags":

                        post.value.tags = chunk.data;

                        break;

                    case "__error":

                        error.value = chunk.data.message;

                        break;

                    case "__done":

                        loading.value = false;

                        break;
                }
            }
        }

        loading.value = false;
    }

    return {
        post,
        loading,
        progress,
        error,
        fetchPost
    };
}
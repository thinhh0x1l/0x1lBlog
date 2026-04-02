<template>
  <div style="z-index: 10">
    <div class="pb-2 px-4 pt-4  surface-card shadow-2 mb-5 relative m-box"
         v-for="blog in blogList"
         :key="blog.id">


      <PinTop v-if="blog.top"/>

      <!-- Container với flex layout -->
      <div class="flex flex-column">
        <div >
          <!-- Tiêu đề -->
          <div class="col-12 text-center" style="padding-top: 0">
            <h2 class="header m-scaleup">
              <a href="#" class="text-900 hover:text-primary transition-colors no-underline">
                {{ blog.title }}
              </a>
            </h2>
          </div>

          <!-- Thông tin bài viết -->
          <div class="col-12 text-center">
            <div class="flex flex-wrap justify-content-center gap-4">
              <div class="flex align-items-center m-datetime">
                <font-awesome-icon icon="calendar-alt" class="mr-2" />
                <span>{{ formatDate(blog.createTime) }}</span>
              </div>

              <div class="flex align-items-center m-views">
                <font-awesome-icon icon="eye" class="mr-2" />
                <span>{{ blog.views }}</span>
              </div>

              <div class="flex align-items-center m-common-black">
                <font-awesome-icon icon="pencil-alt" class="mr-2" />
                <span>Số chữ ≈ {{ blog.words }} từ</span>
              </div>

              <div class="flex align-items-center m-common-black">
                <font-awesome-icon icon="clock" class="mr-2" />
                <span>Thời gian đọc ≈ {{ blog.readTime }} phút</span>
              </div>
            </div>
          </div>

          <Ribbon :category="blog?.category"/>
          <div class="px-3 py-2">
            <!-- Mô tả bài viết -->
            <div class="m-padded-tb-small line-numbers match-braces rainbow-braces typo " v-html="blog.description"></div>

            <!-- Nút đọc toàn bộ -->
            <div class="col-12">
              <div class="flex align-items-cente">
                <router-link :to="`/blog/${blog.id}`" class="color-btn">
                  <span >Đọc toàn bộ</span>
                </router-link>
              </div>
            </div>

            <!-- Divider -->
            <div class="col-12">
              <div class="border-top-1 surface-border my-2"></div>
            </div>

            <!-- Tags -->
            <div class="m-padded-tb-no">
              <Tag v-if="blog.tags" :list-tag="blog.tags"></Tag>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {formatDate} from "@/util/dateTimeFormatUtils.js";
import Tag from '@/components/blogList/Tag.vue'
import Ribbon from "@/components/blogList/Ribbon.vue";
import PinTop from "@/components/blogList/PinTop.vue";

defineProps({
  blogList: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.header{
  border: none;
  margin: 0 1rem;
  top: 13px;
  padding: 0 0;
  font-size: 1.71428571rem;
  font-family: Lato, 'Helvetica Neue', Arial, Helvetica, sans-serif;
  font-weight: 700;
  line-height: 1.28571429em;
  text-transform: none;
  color: rgba(0, 0, 0, .87);
}
</style>
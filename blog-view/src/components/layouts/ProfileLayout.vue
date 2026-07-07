<template>
  <div class="profile-layout">
    <div class="profile-cover-area">
      <slot name="cover" />
    </div>
    <div class="profile-body">
      <aside class="profile-sidebar">
        <slot name="sidebar" />
      </aside>
      <main class="profile-main">
        <slot />
      </main>
    </div>
  </div>
</template>
<style scoped lang="scss">
.profile-layout {
  max-width: var(--page-max-width, 1060px);
  margin: 0 auto;
  width: 100%;
}
.profile-cover-area {
  margin-bottom: 24px;
}
.profile-body {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
}
.profile-sidebar {
  position: sticky;
  top: calc(var(--header-height, 64px) + 24px);
  height: fit-content;
}
.profile-main {
  min-width: 0;
}
@include respond-to(lg) {
  .profile-body {
    grid-template-columns: 240px 1fr;
  }
}
@include respond-to(md) {
  .profile-body {
    grid-template-columns: 1fr;
  }
  .profile-sidebar {
    display: none;
  }
}
</style>

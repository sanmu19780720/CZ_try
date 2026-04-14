<template>
  <div class="app-shell">
    <router-view />
    <nav class="bottom-nav">
      <router-link to="/" :class="{ active: $route.path === '/' }">首页</router-link>
      <router-link to="/products" :class="{ active: $route.path.startsWith('/products') }">产品</router-link>
      <router-link to="/cases" :class="{ active: $route.path.startsWith('/cases') }">案例</router-link>
      <router-link to="/inquiry" :class="{ active: $route.path === '/inquiry' }">咨询</router-link>
    </nav>
  </div>
</template>

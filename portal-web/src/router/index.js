import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/products', name: 'Products', component: () => import('../views/Products.vue') },
  { path: '/products/:id', name: 'ProductDetail', component: () => import('../views/ProductDetail.vue') },
  { path: '/cases', name: 'Cases', component: () => import('../views/Cases.vue') },
  { path: '/cases/:id', name: 'CaseDetail', component: () => import('../views/CaseDetail.vue') },
  { path: '/inquiry', name: 'Inquiry', component: () => import('../views/Inquiry.vue') },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
